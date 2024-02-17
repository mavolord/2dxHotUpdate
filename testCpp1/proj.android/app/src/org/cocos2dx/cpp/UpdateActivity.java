package org.cocos2dx.cpp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateActivity extends Activity {
    static public String fileUrl = "http://ziyi.fun:5684/libTest.so";
    static public String md5Url = "http://ziyi.fun:5684/soMd5";
    static public String localSoPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("update activity 启动");
        checkUpdate();
    }

    public void checkUpdate(){
        File writeablePath =  getFilesDir();
        System.out.println("可读写路径是:" + writeablePath);
        localSoPath = writeablePath + "/libTest.so";

        File osFile = new File(localSoPath);
        if(osFile.exists()){
            System.out.println(localSoPath + ":文件存在");
            checkSoMd5();
        }else{
            System.out.println(osFile.getAbsolutePath() + ":文件不存在");
            downloadLib();
        }
    }

    public void checkSoMd5(){
        OkHttpClient client = new OkHttpClient();
        try {
            Request req = new Request.Builder().url(md5Url).build();
            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println("请求发送失败");
                    startGame();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String serverMd5 = response.body().string();
                    System.out.println("服务器上的so文件Md5是:" + serverMd5);
                    String localMd5 = calcMd5(localSoPath);
                    System.out.println("本地的so文件md5是:" + localMd5);

                    if(serverMd5.equals(localMd5)){
                        startGame();
                    }else{
                        downloadLib();
                    }
                }
            });
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public String calcMd5(String path) {
        try{
            MessageDigest md = MessageDigest.getInstance("md5");
            InputStream inputStream = new FileInputStream(localSoPath);
            byte[] buffer =  new byte[8192];
            int read;
            while((read = inputStream.read(buffer)) != -1){
                md.update(buffer, 0, read);
            }
            byte[] md5sum = md.digest();
            inputStream.close();
            StringBuilder hexString = new StringBuilder();
            for(byte b: md5sum){
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1){
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        }catch (Exception e){
            System.out.println(e);
            return "";
        }
    }

    public void downloadLib(){
        Request req = new Request.Builder().url(fileUrl).build();
        FileDownloader downloader = new FileDownloader();
        downloader.downloadFile(fileUrl, localSoPath, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("文件下载失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("文件下载成功");
                startGame();
            }
        });
    }

    public void startGame(){
        Intent intent = new Intent(this, AppActivity.class);
        startActivity(intent);
    }
}