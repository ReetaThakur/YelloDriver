package com.app.yellodriver.service;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface ApiService {
    @PUT
    Call<Void> uploadProfileData(@Url String url, @Body RequestBody file);
}