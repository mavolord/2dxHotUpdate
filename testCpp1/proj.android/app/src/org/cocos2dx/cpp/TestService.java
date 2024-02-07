package org.cocos2dx.cpp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

public class TestService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("testService 被创建了");
        startTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("testService 被启动了？");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        System.out.println("testService被销毁");
        super.onDestroy();
    }
    private void startTask(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("定时任务执行中。。。");
            }
        };
        timer.schedule(task, 5000, 10000);
    }
}
