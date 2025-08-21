package com.example.todoappapi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TokenApiService {
    @POST("/api/FirebaseTokens")
    Call<Void> sendToken(@Body TokenRequest tokenRequest);
}
