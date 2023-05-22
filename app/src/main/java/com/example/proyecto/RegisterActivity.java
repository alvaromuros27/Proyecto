package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText name, email, pass, pass1;

    Button registrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);

        email = findViewById(R.id.email);

        pass = findViewById(R.id.pass);

        pass1 = findViewById(R.id.pass1);

        registrado = findViewById(R.id.btnregistrado);
        registrado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("PULSADO EL BOTON");
                //if(name.getText().toString().equals("")||email.getText().toString().equals("")||pass.getText().toString().equals("")||pass1.getText().toString().equals("")){
                  //  Toast.makeText(getApplicationContext(), "Campos vacios, debe de rellenarlos", Toast.LENGTH_LONG).show();
                //}else{
                    JsonObject paramObject = new JsonObject();
                    try {

                        paramObject.addProperty("username", "julia");
                        paramObject.addProperty("email", "julita18");
                        paramObject.addProperty("password1", "julia18");
                        paramObject.addProperty("password2", "julia18");

                    } catch (Exception e) {
                        System.out.println("se queda aqui");
                        e.printStackTrace();
                    }
                    postRegister(paramObject);

                //}
            }
        });
    }


    public void postRegister(JsonObject body){
        Call<JsonElement> call = RetrofitClient.getInstance().getMyApi().postRegister(body);
        System.out.println("AQUI1");
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    System.out.println("AQUI2");
                    System.out.println(response.body());
                }else{
                    System.out.println("AQUI3");
                    System.out.println(response.code());
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                System.out.println(t);
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }
}