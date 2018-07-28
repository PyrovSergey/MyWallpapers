package com.example.pyrov.mywallpapers;

import android.app.Application;
import android.content.Context;

import com.example.pyrov.mywallpapers.model.PixabayApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private static Context context;
    public static final String BASE_URL = "https://pixabay.com/api/";
    private static PixabayApi pixabayApi;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        pixabayApi = retrofit.create(PixabayApi.class);
    }

    public static Context getContext() {
        return context;
    }

    public static PixabayApi getPixabayApi() {
        return pixabayApi;
    }

}
