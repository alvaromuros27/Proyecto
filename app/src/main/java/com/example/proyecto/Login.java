package com.example.proyecto;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("access_token")
    @Expose
    private String token;
    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;
    @SerializedName("user")
    @Expose
    private JsonObject user;

    public Login(String token, String refreshToken, JsonObject user){

        this.token = token;
        this.refreshToken = refreshToken;
        this.user = user;

    }

    public String getToken() {
        return  "Bearer "+this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public JsonObject getUser() {
        return user;
    }

    public void setUser(JsonObject user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "{" +
                "token='" + token + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", user=" + user +
                '}';
    }
}
