package com.example.proyecto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {

    String BASE_URL = "http://192.168.101.85:8000/";

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("dj-rest-auth/login/")
    Call<JsonElement> postLogins(@Body JsonObject body);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("dj-rest-auth/registration/")
    Call<JsonElement> postRegister(@Body JsonObject body);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("registros/")
    Call<JsonElement> getRegistro(@Header("Authorization") String token, @Body JsonObject body);


}
