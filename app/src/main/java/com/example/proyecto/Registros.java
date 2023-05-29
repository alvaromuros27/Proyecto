package com.example.proyecto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Registros {
    @SerializedName("count")
    @Expose
    public int count;
    @SerializedName("previous")
    @Expose
    public String previous;
    @SerializedName("next")
    @Expose
    public String next;
    @SerializedName("results")
    @Expose
    public List<Registro> registroList;

    public Registros(int count, String previous, String next, List<Registro> registroList) {
        this.count = count;
        this.previous = previous;
        this.next = next;
        this.registroList = registroList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<Registro> getRegistroList() {
        return registroList;
    }

    public void setRegistroList(List<Registro> registroList) {
        this.registroList = registroList;
    }
}
