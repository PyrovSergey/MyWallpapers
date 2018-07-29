package com.example.pyrov.mywallpapers.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PixabayApi {
    // "?key=9665918-5c24fd510db60ddcbef834d2e&image_type=photo&order=popular&pretty=true&orientation=vertical&per_page=200"
    @GET("?key=9665918-5c24fd510db60ddcbef834d2e&image_type=photo&pretty=true")
    Call<MyResponse> getData(@Query("q") String q, @Query("order") String order, @Query("orientation") String orientation, @Query("safesearch") String safeSearch, @Query("per_page") String perPage);
}
