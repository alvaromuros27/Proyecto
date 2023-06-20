package com.example.proyecto;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbarPerfil;
    SharedPreferences sharedPref;
    Login parametros;
    private String body;
    private EditText username,email,  firstname, lastname;
    private Button actualizar, cambiarPass;
    ActivityResultLauncher<Intent> activityAddResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPref = getDefaultSharedPreferences(
                getApplicationContext());

        body = sharedPref.getString("body", " ");
        parametros= new Gson().fromJson(body, Login.class);

        toolbarPerfil = (Toolbar)findViewById(R.id.toolbarPerfil);
        setSupportActionBar(toolbarPerfil);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setTitle("Perfil");
        }

        username = findViewById(R.id.usernamePerfil);
        username.setText(parametros.getUser().get("username").getAsString());
        username.setEnabled(false);

        email = findViewById(R.id.emailPerfil);
        email.setText(parametros.getUser().get("email").getAsString());
        email.setEnabled(false);

        firstname = findViewById(R.id.nombrePerfil);
        firstname.setText(parametros.getUser().get("first_name").getAsString());

        lastname = findViewById(R.id.apellidoPerfil);
        lastname.setText(parametros.getUser().get("last_name").getAsString());

        actualizar = findViewById(R.id.botonActualizar);
        actualizar.setOnClickListener(this::setActualizar);

        cambiarPass = findViewById(R.id.botonCambiar);
        cambiarPass.setOnClickListener(this::initChange);

        activityAddResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        System.out.println(result);
                        if (result.getResultCode() == Activity.RESULT_OK) {

                        }else{

                        }
                    }
                });
    }


    public void initChange(View v) {
        Intent intent = new Intent(ProfileActivity.this, Change_Password_Activity.class);
         activityAddResultLauncher.launch(intent);
    }

    public void setActualizar(View v) {
        if(username.getText().toString().equals("") || firstname.getText().toString().equals("") || lastname.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Necesita rellenar los datos", Toast.LENGTH_LONG).show();

        }else {
            JsonObject paramObject = new JsonObject();
            try {
                if(username.getText().toString().equals(parametros.getUser().get("username").getAsString())){
                    paramObject.addProperty("first_name", firstname.getText().toString());
                    paramObject.addProperty("last_name", lastname.getText().toString());
                }else{
                    paramObject.addProperty("username", username.getText().toString());
                    paramObject.addProperty("first_name", firstname.getText().toString());
                    paramObject.addProperty("last_name", lastname.getText().toString());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            patchUser(paramObject);
        }
    }

    private void patchUser(JsonObject body){
        Call<JsonElement> call = RetrofitClient.getInstance().getMyApi().patchUser(parametros.getToken(),body);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    parametros.setUser((JsonObject) response.body());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("body", parametros.toString());
                    editor.apply();
                    setResult(Activity.RESULT_OK, getIntent());
                    finish();
                }else{
                    try {
                        String errorBody = response.errorBody().string();
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