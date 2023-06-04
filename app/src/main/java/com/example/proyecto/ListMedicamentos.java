package com.example.proyecto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListMedicamentos {

    @SerializedName("medicament")
    @Expose
    public List<String> listMedicamentos = new ArrayList<>();

    public List<String> getListMedicamentos() {
        return listMedicamentos;
    }

    public void setListMedicamentos(List<String> listMedicamentos) {
        this.listMedicamentos = listMedicamentos;
    }
}
