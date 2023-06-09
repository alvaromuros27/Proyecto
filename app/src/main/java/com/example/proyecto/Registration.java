package com.example.proyecto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Registration {
    @SerializedName("username")
    @Expose
    private List<String> username;
    @SerializedName("email")
    @Expose
    private List<String> email;
    @SerializedName("password1")
    @Expose
    private List<String> password;
    @SerializedName("non_field_errors")
    @Expose
    private List<String> non_field_errors;

    public List<String> getUsername() {
        return username;
    }

    public void setUsername(List<String> username) {
        this.username = username;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public List<String> getPassword() {
        return password;
    }

    public void setPassword(List<String> password) {
        this.password = password;
    }

    public List<String> getNon_field_errors() {
        return non_field_errors;
    }

    public void setNon_field_errors(List<String> non_field_errors) {
        this.non_field_errors = non_field_errors;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "username=" + username +
                ", email=" + email +
                ", password=" + password +
                ", non_field_errors=" + non_field_errors +
                '}';
    }
}
