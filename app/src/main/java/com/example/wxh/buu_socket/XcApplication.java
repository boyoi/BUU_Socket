package com.example.wxh.buu_socket;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by bkrc_logobkrc on 2018/1/10.
 */

public class XcApplication extends Application {

    public  static String cameraip="192.168.1.101:81";

    private static XcApplication app;

    public static ExecutorService executorServicetor = Executors.newCachedThreadPool();

    public static XcApplication getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app =this;
        Intent sintent = new Intent();
        //ComponentName的参数1:目标app的包名,参数2:目标app的Service完整类名
        sintent.setComponent(new ComponentName("com.android.settings", "com.android.settings.ethernet.CameraInitService"));
        //设置要传送的数据
        sintent.putExtra("purecameraip","0.0.0.0");
        startService(sintent);

    }


}
