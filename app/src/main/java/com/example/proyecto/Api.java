package com.example.proyecto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    String BASE_URL = "http://192.168.14.85:8000/";
    @GET("registros/")
    Call<Registro> getRegistro();
}
