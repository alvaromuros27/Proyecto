package com.example.proyecto;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    ListMedicamentos listMedicamentos;
    private EditText nivel, insulina, medicamentos;
    private Button añadir;
    private Spinner spinnerAdd;
    private List<String> itemList;
    private List<String> selectedItemsList;
    private Toolbar toolbarAdd;
    String token;
    AlertDialog.Builder dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        toolbarAdd = (Toolbar)findViewById(R.id.toolbarAñadir);
        setSupportActionBar(toolbarAdd);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setTitle("Añadir");
        }

        sharedPref = getDefaultSharedPreferences(getApplicationContext());

        String jsonFileString = getJsonFromAssets(this, "Medicamentos.json");
        listMedicamentos = new Gson().fromJson(jsonFileString, ListMedicamentos.class);

        nivel = findViewById(R.id.addNivel);

        insulina = findViewById(R.id.addInsulina);

        itemList = listMedicamentos.getListMedicamentos();
        selectedItemsList = new ArrayList<>();

        spinnerAdd = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdd.setAdapter(adapter);

        spinnerAdd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = itemList.get(position);
                selectedItemsList.add(selectedItem);
                updateSelectedItemsText();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        medicamentos = findViewById(R.id.addMedicamentos);

        token= sharedPref.getString("token", " ");

        añadir = findViewById(R.id.botonAplicar);
        añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Comprobado lo anterior se inicializan las siguientes variables.
                String inputNivel = String.valueOf(nivel.getText());
                inputNivel = inputNivel.trim(); // Elimino los espacios que se quedan
                String inputInsulina = String.valueOf(insulina.getText());
                inputInsulina = inputInsulina.trim();

                //Aqui creo el JsonObject
                JsonObject paramObject = new JsonObject();
                try {
                    if(!inputNivel.isEmpty()){
                        int number = Integer.parseInt(inputNivel);
                        int insulinaAdministrada;
                        int nivelFinal = 0;
                        int insulinaFinal=0;
                        if (number>=600){
                            initDialog("Acuda inmediatamente al hospital", nivel);
                        }else if(number<=40){
                            initDialog("Acuda inmediatamente al hospital", nivel);
                        }else{
                            nivelFinal = number;
                            if(!inputInsulina.isEmpty()){
                                insulinaAdministrada = Integer.parseInt(inputInsulina);
                                if(insulinaAdministrada>30){
                                    initDialog("Debe de controlar su dosis de insulina", insulina);
                                }else {
                                    System.out.println(insulinaAdministrada);
                                    insulinaFinal=insulinaAdministrada;
                                    String medi = medicamentos.getText().toString();
                                    medi = medi.trim();
                                    if(!medi.isEmpty()){
                                        paramObject.addProperty("nivel", nivel.getText().toString());
                                        paramObject.addProperty("insulina", insulina.getText().toString());
                                        paramObject.addProperty("medicamentos", medicamentos.getText().toString());
                                        postRegistros(paramObject);
                                    }else{
                                        initDialog("No se puede dejar el campo vacio", medicamentos);
                                    }
                                }
                            }else{
                                initDialog("No se puede dejar el campo vacio", insulina);
                            }
                        }
                    }else{
                        initDialog("No se puede dejar el campo vacio", nivel);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input format: " + inputNivel);
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        System.out.println("aquiiii");
        System.out.println(sharedPref.getString("token", " "));
    }

    public void initDialog(String frase, EditText focus){
        dialog = new AlertDialog.Builder(AddActivity.this);
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

    private void postRegistros(JsonObject body){
        Call<JsonElement> call = RetrofitClient.getInstance().getMyApi().postRegistros(token,body);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    System.out.println(response);
                    setResult(Activity.RESULT_OK, getIntent());
                    finish();
                }else{
                    try {
                        // Maneja el cuerpo del error aquí
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

    private void updateSelectedItemsText() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : selectedItemsList) {
            stringBuilder.append(item).append("\n");
        }
        medicamentos.setText(stringBuilder.toString());
    }

    static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }
}