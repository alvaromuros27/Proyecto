package com.example.proyecto;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText user, pass;
    Button enter, register;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = findViewById(R.id.nombre);

        pass = findViewById(R.id.password);

        register = findViewById(R.id.btnregister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("PULSADO EL BOTON");
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        enter = findViewById(R.id.btnentrar);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("PULSADO EL BOTON");
                if(user.getText().toString().equals("")||pass.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Campos vacios, debe de rellenarlos", Toast.LENGTH_LONG).show();
                }else{
                    JsonObject paramObject = new JsonObject();
                    try {

                        paramObject.addProperty("username", "alvaro");
                        paramObject.addProperty("password", "alvaro");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    postLogins(paramObject);

                }

            }
        });

    }



    public void postLogins(JsonObject body){
        Call<JsonElement> call = RetrofitClient.getInstance().getMyApi().postLogins(body);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    //Borrar los datos del editText en user
                    user.getText().clear();
                    System.out.println( response.body().toString());

                    Intent intent = new Intent(getApplicationContext(), ListFragmentActivity.class);
                    //intent.putExtra("variable",  response.body().toString());
                    startActivity(intent);

                    sharedPref= getDefaultSharedPreferences(
                            getApplicationContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("body", response.body().toString());
                    editor.apply();


                }else{
                    Toast.makeText(getApplicationContext(), "Credenciales incorrectas", Toast.LENGTH_LONG).show();
                }
                //Borrar los datos del editText en password
                pass.getText().clear();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }


}