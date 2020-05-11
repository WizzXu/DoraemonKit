package com.didichuxing.doraemonkit.kit.network.okhttp.interceptor;


import android.text.TextUtils;
import android.util.Log;

import com.didichuxing.doraemonkit.constant.DokitConstant;
import com.didichuxing.doraemonkit.kit.network.NetworkManager;
import com.didichuxing.doraemonkit.kit.network.bean.NetworkRecord;
import com.didichuxing.doraemonkit.kit.network.bean.WhiteHostBean;
import com.didichuxing.doraemonkit.kit.network.core.DefaultResponseHandler;
import com.didichuxing.doraemonkit.kit.network.core.NetworkInterpreter;
import com.didichuxing.doraemonkit.kit.network.core.RequestBodyHelper;
import com.didichuxing.doraemonkit.kit.network.core.ResourceType;
import com.didichuxing.doraemonkit.kit.network.core.ResourceTypeHelper;
import com.didichuxing.doraemonkit.kit.network.mock.ApiInterfaceListResponse;
import com.didichuxing.doraemonkit.kit.network.mock.MockUtil;
import com.didichuxing.doraemonkit.kit.network.okhttp.ForwardingResponseBody;
import com.didichuxing.doraemonkit.kit.network.okhttp.InterceptorUtil;
import com.didichuxing.doraemonkit.kit.network.okhttp.OkHttpInspectorRequest;
import com.didichuxing.doraemonkit.kit.network.okhttp.OkHttpInspectorResponse;
import com.didichuxing.doraemonkit.util.LogHelper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 抓包拦截器
 */
public class DoraemonInterceptor implements Interceptor {
    public static final String TAG = "DoraemonInterceptor";

    private final NetworkInterpreter mNetworkInterpreter = NetworkInterpreter.get();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (MockUtil.getInstance().isOpen() &&
                MockUtil.getInstance().interfaceInfoConcurrentHashMap.get(request.url().url().getPath()) != null
                && "undone".equals(MockUtil.getInstance().interfaceInfoConcurrentHashMap.get(request.url().url().getPath()).getStatus())) {
            Log.d("--->", "获取mock数据" + request.url().url().getPath());
            StringBuffer sb = new StringBuffer();
            sb.append(MockUtil.getInstance().getUrl());
            sb.append(MockUtil.getInstance().getProjectSpace());
            sb.append(request.url().url().getPath());
            String params = MockUtil.getInstance().interfaceInfoConcurrentHashMap.get(request.url().url().getPath()).getParams();
            sb.append("?");
            sb.append(params);
            request = request.newBuilder().url(sb.toString()).method("GET", null).build();
        }

        if (!NetworkManager.isActive()) {
            return chain.proceed(request);
        }

        Response response = chain.proceed(request);

        String strContentType = response.header("Content-Type");
        //如果是图片则不进行拦截
        if (InterceptorUtil.isImg(strContentType)) {
            return response;
        }
        //白名单过滤
        if (!matchWhiteHost(request)) {
            return response;
        }


        int requestId = mNetworkInterpreter.nextRequestId();

        RequestBodyHelper requestBodyHelper = new RequestBodyHelper();
        OkHttpInspectorRequest inspectorRequest =
                new OkHttpInspectorRequest(requestId, request, requestBodyHelper);
        NetworkRecord record = mNetworkInterpreter.createRecord(requestId, inspectorRequest);
        try {
            response = chain.proceed(request);
        } catch (IOException e) {
            mNetworkInterpreter.httpExchangeFailed(requestId, e.toString());
            throw e;
        }

        NetworkInterpreter.InspectorResponse inspectorResponse = new OkHttpInspectorResponse(
                requestId,
                request,
                response);

        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // request the entire body.
        Buffer buffer = source.buffer();
        String responseBodyString = buffer.clone().readString(Charset.forName("UTF-8"));

        mNetworkInterpreter.fetchResponseInfo(record, inspectorResponse);
        mNetworkInterpreter.fetchResponseBody(record, responseBodyString);

        ResponseBody body = response.body();
        InputStream responseStream = null;
        MediaType contentType = null;
        if (body != null) {
            contentType = body.contentType();
            responseStream = body.byteStream();
        }

        responseStream = mNetworkInterpreter.interpretResponseStream(
                contentType != null ? contentType.toString() : null,
                responseStream,
                new DefaultResponseHandler(mNetworkInterpreter, requestId, record));
        if (responseStream != null) {
            response = response.newBuilder()
                    .body(new ForwardingResponseBody(body, responseStream))
                    .build();
        }

        return response;
    }

    /**
     * 是否命中白名单规则
     *
     * @return bool
     */
    private boolean matchWhiteHost(Request request) {
        List<WhiteHostBean> whiteHostBeans = DokitConstant.WHITE_HOSTS;
        if (whiteHostBeans.isEmpty()) {
            return true;
        }

        for (WhiteHostBean whiteHostBean : whiteHostBeans) {
            if (TextUtils.isEmpty(whiteHostBean.getHost())) {
                continue;
            }
            String realHost = request.url().host();
            //正则判断
            if (whiteHostBean.getHost().equalsIgnoreCase(realHost)) {
                return true;
            }
        }

        return false;
    }


}