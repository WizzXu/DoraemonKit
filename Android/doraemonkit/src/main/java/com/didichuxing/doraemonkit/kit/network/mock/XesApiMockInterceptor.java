package com.didichuxing.doraemonkit.kit.network.mock;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: xuweiyu
 * Date: 2020/3/22
 * Description:
 */
public class XesApiMockInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(getRequest(chain.request()));
    }

    private Request getRequest(Request request) {
        if (!MockUtil.getInstance().isOpen()) {
            return request;
        }
        if (MockUtil.getInstance().interfaceInfoConcurrentHashMap.get(request.url().url().getPath()) != null
                && "undone".equals(MockUtil.getInstance().interfaceInfoConcurrentHashMap.get(request.url().url().getPath()).getStatus())) {
            Log.d("--->", "获取mock数据" + request.url().url().getPath());
            StringBuffer sb = new StringBuffer();
            sb.append(MockUtil.getInstance().getUrl());
            sb.append(MockUtil.getInstance().getProjectSpace());
            sb.append(request.url().url().getPath());
            return request.newBuilder().url(sb.toString()).build();
        }
        return request;
    }
}
