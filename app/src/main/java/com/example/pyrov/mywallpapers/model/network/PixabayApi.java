package com.example.pyrov.mywallpapers.model.network;

import com.example.pyrov.mywallpapers.model.dto.MyResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PixabayApi {
    @GET("?key={MY_KEY}&image_type=photo&pretty=true")
    Call<MyResponse> getData(@Query("q") String q, @Query("order") String order, @Query("orientation") String orientation, @Query("safesearch") String safeSearch, @Query("per_page") String perPage);
}
