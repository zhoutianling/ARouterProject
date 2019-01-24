package com.joe.base.net;

import com.joe.base.bean.NewsData;
import com.joe.base.bean.RequestBean;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Joe on 2018/7/5.
 */

public interface IRestfulAPI {

    @GET("api/data/Android/{size}/{index}")
    Observable<NewsData> getNewsData(@Path("size") String size, @Path("index") String index);

}
