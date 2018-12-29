/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joe.commom_library.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxCacheUtil {

    public static Observable rxCreateDiskObservable(Context context, final String key, final Type typeToken) {
        return Observable.create((ObservableOnSubscribe<String>) e -> {
            String json = ACache.get(context).getAsString(key);
//            Log.i("zzz", "get data from disk finish , json==" + json);
            if (!TextUtils.isEmpty(json)) {
                e.onNext(json);
            }
            e.onComplete();
        })
                .map(s -> new Gson().fromJson(s, typeToken))
                .subscribeOn(Schedulers.io());
    }

    public static <T> ObservableTransformer<T, T> rxCacheListHelper(Context context, final String key) {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())//指定doOnNext执行线程是新线程
                .doOnNext(data -> Schedulers.io().createWorker().schedule(new Runnable() {
                    @Override
                    public void run() {
//                        Log.i("zzz", "get data from network finish ,start cache...");
                        //通过反射获取List,再判空决定是否缓存
                        if (data == null) {
                            return;
                        }
                        Class clazz = data.getClass();
                        Field[] fields = clazz.getFields();
                        for (Field field : fields) {
                            String className = field.getType().getSimpleName();
                            // 得到属性值
                            if (className.equalsIgnoreCase("List")) {
                                try {
                                    List list = (List) field.get(data);
                                    putListToAcache(context, clazz, list);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    private void putListToAcache(Context context, Class clazz, List list) {
                        if (list != null && !list.isEmpty()) {
//                            Log.i("zzz", "putListToAcache list==" + list);
                            ACache.get(context)
                                    .put(key, new Gson().toJson(data, clazz));
//                            Log.i("zzz", "cache finish");
                        }
                    }

                }))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<T, T> rxCacheBeanHelper(Context context, final String key) {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())//指定doOnNext执行线程是新线程
                .doOnNext(data -> Schedulers.io().createWorker().schedule(() -> {
//                    Log.i("zzz", "get data from network finish ,start cache...");
                    ACache.get(context)
                            .put(key, new Gson().toJson(data, data.getClass()));
//                    Log.i("zzz", "cache finish");
                }))
                .observeOn(AndroidSchedulers.mainThread());
    }
}
