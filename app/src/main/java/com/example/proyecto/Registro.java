package com.example.proyecto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Registro {

    @SerializedName("nivel")
    @Expose
    private int nivel;
    @SerializedName("insulina")
    @Expose
    private int insulina;
    @SerializedName("medicamentos")
    @Expose
    private String medicamentos;

    public Registro(int nivel, int insulina, String medicamentos){
        this.nivel=nivel;
        this.insulina=insulina;
        this.medicamentos=medicamentos;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getInsulina() {
        return insulina;
    }

    public void setInsulina(int insulina) {
        this.insulina = insulina;
    }

    public String getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(String medicamentos) {
        this.medicamentos = medicamentos;
    }
}
