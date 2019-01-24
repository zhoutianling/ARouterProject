package com.joe.base.net;

import android.net.ParseException;
import android.text.TextUtils;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * @author Joe
 * @time 11/2/2018 10:08 AM
 */
public class ExceptionEngine {

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {             //HTTP错误
            ex = new ApiException(e, HttpError.HTTP_ERROR);
            ResponseBody body = ((HttpException) e).response().errorBody();
            try {
                ex.code = ((HttpException) e).code();
                String msg = body.string();
                if (!TextUtils.isEmpty(msg)) {
//                    ErrorResBody responseBody = new Gson().fromJson(msg, ErrorResBody.class);
                    switch (ex.code) {
                        case 400:
                            ex.message = "账号或密码错误";
                            break;
                        case 404:
                            ex.message = "资源不存在";
                            break;
                        default:
                            ex.message = "服务器错误";
                            break;
                    }

                }
            } catch (Exception o) {
                o.printStackTrace();
                ex.code = ((HttpException) e).code();
                ex.message = o.getMessage();
            }

            return ex;
        } else if (e instanceof ServerException) {    //服务器返回的错误
            ServerException resultException = (ServerException) e;
            ex = new ApiException(resultException, resultException.getCode());
            ex.message = resultException.getMessage();
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ApiException(e, HttpError.PARSE_ERROR);
            ex.message = "解析错误";            //均视为解析错误
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ApiException(e, HttpError.NETWORD_ERROR);
            ex.message = "连接失败";  //均视为网络错误
            return ex;
        } else if (e instanceof UnknownHostException) {
            ex = new ApiException(e, HttpError.UNKNOWN);
            ex.message = "网络不通";          //未知错误
            return ex;
        } else if (e instanceof SocketTimeoutException) {
            ex = new ApiException(e, HttpError.HTTP_TIMEOUT);
            ex.message = "连接超时";          //未知错误
            return ex;
        } else {
            ex = new ApiException(e, HttpError.UNKNOWN);
            ex.message = "未知错误";          //未知错误
            return ex;
        }
    }
}



