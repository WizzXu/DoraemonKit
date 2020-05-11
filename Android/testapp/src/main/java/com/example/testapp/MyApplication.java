package com.example.testapp;

import android.app.Application;

import com.didichuxing.doraemonkit.DoraemonKit;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DoraemonKit.install(this, null, "d2627b8ccaad75c47893167a7b87313a",
                //"http://10.26.67.3:3000",
                "http://10.1.1.145:3000",
                //"a11c6559df4a946c323eb53d5e8185e4fda27546e6d175cecb93fcf76ea0d4f4",
                "c0900951f002db1dee34d58f038b109170cdefff64d9866e4435fa959b00eb74",
                "11");
        //DoraemonKit.install(this, null);

    }
}