package com.example.pyrov.mywallpapers.model;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PixabayApi {
    // "?key=9665918-5c24fd510db60ddcbef834d2e&image_type=photo&order=popular&pretty=true&orientation=vertical&per_page=200"
    @GET("?key=9665918-5c24fd510db60ddcbef834d2e&image_type=photo&order=popular&pretty=true&safesearch=true&per_page=200")
    Call<Response> getData(@Query("q") String q);
}
