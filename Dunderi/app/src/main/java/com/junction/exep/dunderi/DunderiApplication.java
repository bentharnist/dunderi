package com.junction.exep.dunderi;

import android.app.Application;

public class DunderiApplication extends Application {
    private ApiManager api;
    private static FileManager files;

    @Override
    public void onCreate() {
        super.onCreate();
        api = new ApiManager(this);
        files = new FileManager(this);
    }

    public ApiManager getApi(){
        return api;
    }

    public FileManager getFiles(){
        return files;
    }

}
