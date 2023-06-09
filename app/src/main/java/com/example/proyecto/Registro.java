package com.example.proyecto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Registro {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("created")
    @Expose
    private String creacion;
    @SerializedName("nivel")
    @Expose
    private int nivel;
    @SerializedName("insulina")
    @Expose
    private int insulina;
    @SerializedName("medicamentos")
    @Expose
    private String medicamentos;

    public Registro(int id,int nivel, int insulina, String medicamentos){
        this.id=id;
        this.nivel=nivel;
        this.insulina=insulina;
        this.medicamentos=medicamentos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCreacion() {
        String fechaISO = creacion;
        String fechaNormal = null;
        try {
            SimpleDateFormat formatoISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
            Date fecha = formatoISO8601.parse(fechaISO);

            // Sumar dos horas a la fecha
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fecha);
            calendar.add(Calendar.HOUR_OF_DAY, 2);
            Date fechaConDosHorasMas = calendar.getTime();

            SimpleDateFormat formatoFechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            fechaNormal = formatoFechaHora.format(fechaConDosHorasMas);

            System.out.println(fechaNormal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechaNormal;
    }

    public void setCreacion(String creacion) {
        this.creacion = creacion;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", creacion='" + creacion + '\'' +
                ", nivel=" + nivel +
                ", insulina=" + insulina +
                ", medicamentos='" + medicamentos + '\'' +
                '}';
    }
}
