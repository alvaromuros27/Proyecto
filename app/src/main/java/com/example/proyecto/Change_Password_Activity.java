package com.example.proyecto;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Change_Password_Activity extends AppCompatActivity {
    EditText password1, password2;

    Button changePassword;

    SharedPreferences sharedPreferences;

    AlertDialog.Builder dialog;

    Toolbar toolbarChange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        toolbarChange = findViewById(R.id.toolbarChangePassword);
        setSupportActionBar(toolbarChange);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setTitle("Cambiar Contraseña");
        }

        sharedPreferences = getDefaultSharedPreferences(getApplicationContext());

        password1 = findViewById(R.id.changePassword);

        password2 = findViewById(R.id.changePassword2);

        changePassword = findViewById(R.id.botonCambiar);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject paramObject = new JsonObject();
                try {
                    String pass1 = password1.getText().toString();
                    pass1 = pass1.trim();
                    if(!pass1.isEmpty()) {
                        String pass2 = password2.getText().toString();
                        pass2 = pass2.trim();
                        if(!pass2.isEmpty()){
                            if(pass1.length()>8 || pass2.length()>8){
                                if(pass1.equals(pass2)){
                                    paramObject.addProperty("new_password1", password1.getText().toString());
                                    paramObject.addProperty("new_password2", password2.getText().toString());
                                    postChangePassword(paramObject);
                                }else{
                                    initDialog("Los dos campos no coinciden", password1);
                                }

                            }else{
                                initDialog("Contraseña demasiado corta", password1);
                            }
                        }else{
                            initDialog("No deje ningún campo vacio", password2);
                        }
                    }else{
                        initDialog("No deje ningún campo vacio", password1);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input format");
                    e.printStackTrace();
                }
            }
        });

    }

    public void initDialog(String frase, EditText focus){
        dialog = new AlertDialog.Builder(Change_Password_Activity.this);
        dialog.setTitle(frase);
        dialog.setPositiveButton("Continuar", (dialogo, id) ->{
            dialogo.cancel();
            focus.requestFocus();
        });
        dialog.setNegativeButton("Salir de la APP", (dialogo, id) ->{
            finishAffinity();
        });
        dialog.show();
    }

    private void postChangePassword(JsonObject body){
        Call<JsonElement> call = RetrofitClient.getInstance().getMyApi().postChangePassword(sharedPreferences.getString("token", ""),body);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Contraseña cambiada correctamente", Toast.LENGTH_LONG).show();
                    setResult(Activity.RESULT_OK, getIntent());
                    finish();
                }else{
                    try {
                        // Maneja el cuerpo del error aquí
                        String errorBody = response.errorBody().string();
                        System.out.println(errorBody);
                        Toast.makeText(getApplicationContext(), errorBody, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }
}