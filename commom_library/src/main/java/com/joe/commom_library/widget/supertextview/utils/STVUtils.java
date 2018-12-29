/*
 * Copyright (C) 2018 CoorChice <icechen_@outlook.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * <p>
 * Last modified 18-5-13 上午9:39
 */

package com.joe.commom_library.widget.supertextview.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Looper;

/**
 * Project Name:SuperTextView
 * Author:CoorChice
 * Date:2018/5/13
 * Notes:
 */

/**
 * @hide
 */
public class STVUtils {

  /**
   * 把Drawable转换成一个Bitmap
   *
   * @param drawable drawable对象
   * @return
   */
  public static Bitmap drawableToBitmap(Drawable drawable) {
    Bitmap bitmap = Bitmap.createBitmap(
      drawable.getIntrinsicWidth(),
      drawable.getIntrinsicHeight(),
      drawable.getOpacity() != PixelFormat.OPAQUE
        ? Bitmap.Config.ARGB_8888
        : Bitmap.Config.RGB_565);
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    drawable.draw(canvas);
    return bitmap;
  }

  public static boolean isOnMainThread() {
    return Looper.myLooper() == Looper.getMainLooper();
  }
}
