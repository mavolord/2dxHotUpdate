package org.cocos2dx.cpp;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FileDownloader {
    private final OkHttpClient client = new OkHttpClient();

    public void downloadFile(String url, String dest, Callback cb){
        Request req = new Request.Builder().url(url).build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                cb.onFailure(call, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(InputStream inputStream = response.body().byteStream();
                    FileOutputStream outputStream = new FileOutputStream(new File(dest))){
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while((bytesRead = inputStream.read(buffer)) != -1){
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();
                    cb.onResponse(call, response);
                }catch (IOException e){
                    cb.onFailure(call, e);
                }
            }
        });
    }
}
