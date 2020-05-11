package com.didichuxing.doraemonkit.kit.network.mock;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: xuweiyu
 * Date: 2020/3/22
 * Description:
 */
public class MockUtil {
    private static MockUtil instance = new MockUtil();

    public static MockUtil getInstance() {
        return instance;
    }

    private boolean loading = false;

    private boolean isOpen = false;

    private String url;
    private String token;

    public String getProjectSpace() {
        return projectSpace;
    }

    public String getUrl() {
        return url;
    }

    private String projectSpace;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    MockDataCallback callback;

    public void init(String url, String token, String projectSpace, boolean isOpen) {
        this.url = url;
        this.token = token;
        this.projectSpace = projectSpace;
        this.isOpen = isOpen;
    }

    public void init(String url, String token, String projectSpace, boolean isOpen, MockDataCallback callback) {
        this.url = url;
        this.token = token;
        this.projectSpace = projectSpace;
        this.isOpen = isOpen;
        this.callback = callback;
        getInterfaceInfo(url, token);
    }


    public ConcurrentHashMap<String, ApiInterfaceListResponse.DataBean.ListBean> interfaceInfoConcurrentHashMap = new ConcurrentHashMap<>();
    public ArrayList<ApiInterfaceListResponse.DataBean> apiTypeList = new ArrayList<>();

    private OkHttpClient client = new OkHttpClient();

    private void getInterfaceInfo(String url, String token) {
        if (!isOpen) {
            return;
        }
        if (url.isEmpty()) {
            return;
        }

        StringBuffer sb = new StringBuffer(url);
        sb.append("/api/interface/list_menu?token=");
        sb.append(token);

        final Request request = new Request.Builder()
                .get()
                .addHeader("Content-Type", "application/json")
                .url(sb.toString())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("mock-->", e.getMessage());
                if (callback != null) {
                    callback.call(e.getMessage());
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body().string();
                Log.d("mock-->", resp);
                Gson gson = new Gson();
                Type type = new TypeToken<ApiInterfaceListResponse>() {
                }.getType();
                ApiInterfaceListResponse response1 = gson.fromJson(resp, type);
                if (response1 != null && response1.getErrcode() == 0) {
                    if (response1.getData() != null && response1.getData() != null &&
                            response1.getData().size() > 0) {
                        MockUtil.getInstance().interfaceInfoConcurrentHashMap.clear();
                        for (ApiInterfaceListResponse.DataBean data : response1.getData()) {
                            for (ApiInterfaceListResponse.DataBean.ListBean d : data.getList()) {
                                MockUtil.getInstance().interfaceInfoConcurrentHashMap.put(d.getPath(), d);
                            }
                        }

                        MockUtil.getInstance().apiTypeList.clear();
                        MockUtil.getInstance().apiTypeList.add(new ApiInterfaceListResponse.DataBean("全部接口", 0));
                        for (int i = 0; i < response1.getData().size(); i++) {
                            MockUtil.getInstance().apiTypeList.add(response1.getData().get(i));
                        }
                    }
                    if (callback != null) {
                        callback.call("{}");
                    }
                    return;

                }
                if (callback != null) {
                    callback.call("获取数据失败");
                }
            }
        });
    }
}
