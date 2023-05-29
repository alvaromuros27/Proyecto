package com.example.proyecto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

    String BASE_URL = "http://192.168.1.73:8000/";

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("dj-rest-auth/login/")
    Call<JsonElement> postLogins(@Body JsonObject body);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("dj-rest-auth/registration/")
    Call<JsonElement> postRegister(@Body JsonObject body);

    @GET("registros/")
    Call<JsonElement> getRegistro(@Header("Authorization") String token);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @DELETE("registros/{id}/")
    Call<JsonElement> deleteSnippets(@Header("Authorization") String token, @Path("id")int id);


}
