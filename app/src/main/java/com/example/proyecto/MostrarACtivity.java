package com.example.proyecto;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostrarACtivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    Registro registro, registro1;
    EditText nivel, insulina, medicamentos;
    Button aplicar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar);

        sharedPref = getDefaultSharedPreferences(getApplicationContext());

        Bundle datos = getIntent().getExtras();
        registro = new Gson().fromJson(datos.getString("registro"), Registro.class);
        registro1 = new Gson().fromJson(registro.toString(), Registro.class);


        nivel = findViewById(R.id.editNivel);
        nivel.setText(" "+registro1.getNivel());
        nivel.setEnabled(false);

        insulina = findViewById(R.id.editInsulina);
        insulina.setText(" "+registro1.getInsulina());
        insulina.setEnabled(false);

        medicamentos = findViewById(R.id.editMedicamentos);
        medicamentos.setText(registro1.getMedicamentos());
        medicamentos.setEnabled(false);

        aplicar = findViewById(R.id.botonAplicar);
        aplicar.setVisibility(View.INVISIBLE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.btnEditar:
                initEdit();
                return true;

            case R.id.btnBorrar:
                deleteRegistros(registro1.getId());
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initEdit(){
        nivel.setEnabled(true);
        insulina.setEnabled(true);
        medicamentos.setEnabled(true);
        aplicar.setVisibility(View.VISIBLE);
        aplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nivel.getText().equals("")||insulina.getText().equals("")||medicamentos.getText().equals("")){
                    System.out.println("No deje los campos vacios");
                }else{
                    String inputNivel = String.valueOf(nivel.getText());
                    inputNivel = inputNivel.trim(); // Remove leading and trailing spaces
                    String inputInsulina = String.valueOf(insulina.getText());
                    inputInsulina = inputInsulina.trim();
                    JsonObject paramObject = new JsonObject();

                    try {
                        int number = Integer.parseInt(inputNivel);
                        int insulinaAdministrada;
                        int nivelFinal = 0;
                        int insulinaFinal=0;
                        if (number>600){
                            System.out.println("Acuda al hospital inmediatamente");
                        }else if(number<30){
                            System.out.println("ACuda al hospital inmediatamente");
                        }else{
                            nivelFinal = number;
                            insulinaAdministrada = Integer.parseInt(inputInsulina);
                            if(insulinaAdministrada>24){
                                System.out.println("No se puede administrar mas insulina de 24");
                            }else {
                                System.out.println(insulinaAdministrada);
                                insulinaFinal=insulinaAdministrada;
                                paramObject.addProperty("nivel", nivelFinal);
                                paramObject.addProperty("insulina", insulinaFinal);
                                paramObject.addProperty("medicamentos", medicamentos.getText().toString());
                            }
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input format: " + inputNivel);
                        e.printStackTrace();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    patchRegitros(paramObject, registro1.getId());
                }

            }
        });
    }


    private void deleteRegistros(int num){
        System.out.println(sharedPref.getString("token", " "));
        Call<JsonElement> call = RetrofitClient.getInstance().getMyApi().deleteRegistros(sharedPref.getString("token", " "),num);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    setResult(Activity.RESULT_OK, getIntent());
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Algo ha ido mal", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }

    private void patchRegitros(JsonObject body, int num){
        Call<JsonElement> call = RetrofitClient.getInstance().getMyApi().patchRegistros(sharedPref.getString("token", " "),body, num);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                System.out.println( response.body());
                if (response.isSuccessful()) {
                    Intent i = getIntent();
                    i.putExtra("registro", response.body().toString());
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Algo ha ido mal", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }
}