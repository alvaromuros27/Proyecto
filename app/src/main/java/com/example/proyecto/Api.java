package com.example.proyecto;

import android.content.SharedPreferences;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {


    String BASE_URL = "http://192.168.55.5:8000/";

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("dj-rest-auth/login/")
    Call<JsonElement> postLogins(@Body JsonObject body);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("dj-rest-auth/registration/")
    Call<JsonElement> postRegister(@Body JsonObject body);

    @POST("dj-rest-auth/logout/")
    Call<JsonElement> postLogout();

    @GET("registros/")
    Call<JsonElement> getRegistro(@Header("Authorization") String token);

    @GET("registros/")
    Call<JsonElement> getRegistros(@Header("Authorization") String token,@Query("page") int page);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @DELETE("registros/{id}/")
    Call<JsonElement> deleteRegistros(@Header("Authorization") String token, @Path("id")int id);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @PATCH("registros/{id}/")
    Call<JsonElement> patchRegistros(@Header("Authorization") String token, @Body JsonObject body,@Path("id")int id);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("registros/")
    Call<JsonElement> postRegistros(@Header("Authorization") String token, @Body JsonObject body);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @PATCH("dj-rest-auth/user/")
    Call<JsonElement> patchUser(@Header("Authorization") String token, @Body JsonObject body);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("dj-rest-auth/password/change/")
    Call<JsonElement> postChangePassword(@Header("Authorization") String token, @Body JsonObject body);

}
