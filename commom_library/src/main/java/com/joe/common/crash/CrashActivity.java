package com.joe.common.crash;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.joe.common.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * desc: CrashActivity.java
 * author: Joe
 * created at: 2019/1/23 下午2:17
 */
public class CrashActivity extends AppCompatActivity {

    public static final String CRASH_MODEL = "crash_model";
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private CrashModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);
        model = getIntent().getParcelableExtra(CRASH_MODEL);
        if (model == null) {
            return;
        }
        TextView tv_packageName = findViewById(R.id.tv_packageName);
        TextView textMessage = findViewById(R.id.textMessage);
        TextView tv_className = findViewById(R.id.tv_className);
        TextView tv_methodName = findViewById(R.id.tv_methodName);
        TextView tv_lineNumber = findViewById(R.id.tv_lineNumber);
        TextView tv_exceptionType = findViewById(R.id.tv_exceptionType);
        TextView tv_fullException = findViewById(R.id.tv_fullException);
        TextView tv_time = findViewById(R.id.tv_time);
        TextView tv_model = findViewById(R.id.tv_model);
        TextView tv_brand = findViewById(R.id.tv_brand);
        TextView tv_version = findViewById(R.id.tv_version);
        ImageView iv_more = findViewById(R.id.iv_more);
//
        tv_packageName.setText(model.getPackageName());
        textMessage.setText(model.getExceptionMsg());
        tv_className.setText(model.getFileName());
        tv_methodName.setText(model.getMethodName());
        tv_lineNumber.setText(String.valueOf(model.getLineNumber()));
        tv_exceptionType.setText(model.getExceptionType());
        tv_fullException.setText(model.getFullException());
        tv_time.setText(df.format(model.getTime()));

        tv_model.setText(model.getDevice().getModel());
        tv_brand.setText(model.getDevice().getBrand());
        tv_version.setText(model.getDevice().getVersion());

        iv_more.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(CrashActivity.this, v);
            menu.inflate(R.menu.menu_more);
            menu.show();
            menu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.menu_copy_text) {
                    String crashText = getShareText(model);
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    if (cm != null) {
                        ClipData mClipData = ClipData.newPlainText("crash", crashText);
                        cm.setPrimaryClip(mClipData);
                        showToast("拷贝成功");
                    }
                } else if (id == R.id.menu_share_text) {
                    String crashText = getShareText(model);
                    shareText(crashText);
                } else if (id == R.id.menu_share_image) {
                    if (ContextCompat.checkSelfPermission(CrashActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                            ContextCompat.checkSelfPermission(CrashActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    } else {
                        shareImage();
                    }
                }
                return true;
            });

        });

    }

    private String getShareText(CrashModel model) {
        StringBuilder builder = new StringBuilder();

        builder.append("崩溃信息:")
                .append("\n")
                .append(model.getExceptionMsg())
                .append("\n");
        builder.append("\n");

        builder.append("类名:")
                .append(model.getFileName()).append("\n");
        builder.append("\n");

        builder.append("包名:")
                .append(model.getPackageName()).append("\n");
        builder.append("\n");

        builder.append("方法:").append(model.getMethodName()).append("\n");
        builder.append("\n");

        builder.append("行数:").append(model.getLineNumber()).append("\n");
        builder.append("\n");

        builder.append("类型:").append(model.getExceptionType()).append("\n");
        builder.append("\n");

        builder.append("时间:").append(df.format(model.getTime())).append("\n");
        builder.append("\n");

        builder.append("设备名称:").append(model.getDevice().getModel()).append("\n");
        builder.append("\n");

        builder.append("设备厂商:").append(model.getDevice().getBrand()).append("\n");
        builder.append("\n");

        builder.append("系统版本:").append(model.getDevice().getVersion()).append("\n");
        builder.append("\n");

        builder.append("全部信息:")
                .append("\n")
                .append(model.getFullException()).append("\n");

        return builder.toString();
    }

    private void shareText(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "崩溃日志：");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "分享到"));
    }


    @SuppressLint("CheckResult")
    private void requestPermission(String... permissions) {
        new RxPermissions(this).request(permissions)
                .subscribe(granted -> {
                    if (granted) {
                        // I can control the camera now
                    } else {
                        // Oups permission denied
                    }
                });
    }


    public Bitmap getBitmapByView(ScrollView view) {
        if (view == null) return null;
        int height = 0;
        for (int i = 0; i < view.getChildCount(); i++) {
            height += view.getChildAt(i).getHeight();
        }
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRGB(255, 255, 255);
        view.draw(canvas);
        return bitmap;
    }

    private File BitmapToFile(Bitmap bitmap) {
        if (bitmap == null) return null;
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .getAbsolutePath();
        File imageFile = new File(path, "crash-" + df.format(model.getTime()) + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            bitmap.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    private void shareImage() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            showToast("未插入sd卡");
            return;
        }
        File file = BitmapToFile(getBitmapByView(findViewById(R.id.scrollView)));
        if (file == null || !file.exists()) {
            showToast("图片文件不存在");
            return;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(),
                    getApplicationContext().getPackageName() + ".crashfileprovider", file);
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
        } else {
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "分享图片"));
    }

    private void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
