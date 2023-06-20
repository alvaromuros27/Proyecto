package com.example.proyecto;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText name, email, pass, pass1;

    Button registrado;

    Registration registration;
    AlertDialog.Builder dialog;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        sharedPref= getDefaultSharedPreferences(
                getApplicationContext());

        name = findViewById(R.id.name);

        email = findViewById(R.id.email);

        pass = findViewById(R.id.pass);

        pass1 = findViewById(R.id.pass1);

        registrado = findViewById(R.id.btnregistrado);
        registrado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("PULSADO EL BOTON");
                if(name.getText().toString().equals("")||email.getText().toString().equals("")||pass.getText().toString().equals("")||pass1.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Campos vacios, debe de rellenarlos", Toast.LENGTH_LONG).show();
                }else{
                    JsonObject paramObject = new JsonObject();
                    try {

                        paramObject.addProperty("username", name.getText().toString());
                        paramObject.addProperty("email", email.getText().toString());
                        paramObject.addProperty("password1", pass.getText().toString());
                        paramObject.addProperty("password2", pass1.getText().toString());
                        postRegister(paramObject);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void postRegister(JsonObject body){
        Call<JsonElement> call = RetrofitClient.getInstance().getMyApi().postRegister(body);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    System.out.println(response.body().toString());
                    Intent intent = new Intent(RegisterActivity.this, ListFragmentActivity.class);
                    startActivity(intent);
                    Login parametros = new Gson().fromJson(response.body().toString(), Login.class);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("body", response.body().toString());
                    editor.putString("token", parametros.getToken());
                    editor.apply();

                    name.getText().clear();
                    email.getText().clear();
                    pass.getText().clear();
                    pass1.getText().clear();
                }else{
                    try {
                        // Maneja el cuerpo del error aquí
                        String errorBody = response.errorBody().string();
                        registration = new Gson().fromJson(errorBody, Registration.class);

                        dialog = new AlertDialog.Builder(RegisterActivity.this);
                        dialog.setTitle("Error");
                        if(registration.getUsername()!=null){
                            dialog.setMessage(" "+registration.getUsername());
                        }else if(registration.getEmail()!=null){
                            dialog.setMessage(" "+registration.getEmail());
                        }else if(registration.getPassword()!=null){
                            dialog.setMessage(" "+registration.getPassword());
                        }else if(registration.getNon_field_errors()!=null){
                            dialog.setMessage(" "+registration.getNon_field_errors());
                        }

                        dialog.setPositiveButton("Continuar", (dialogo, id) ->{
                            dialogo.cancel();
                            if(registration.getUsername()!=null){
                                name.requestFocus();
                            }else if(registration.getEmail()!=null){
                                email.requestFocus();
                            }else if(registration.getPassword()!=null){
                                pass.requestFocus();
                            }else if(registration.getNon_field_errors()!=null){
                                pass1.requestFocus();
                            }
                        });
                        dialog.setNegativeButton("Salir de la APP", (dialogo, id) ->{
                            finishAffinity();
                        });
                        dialog.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }
}