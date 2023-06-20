package com.example.proyecto;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
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

public class EditDeleteActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    Registro registro, registro1;
    private ListMedicamentos listMedicamentos;
    private EditText nivel, insulina, medicamentos;
    private TextInputLayout textInputLayoutNivel;
    private Button aplicar;
    private Spinner spinnerEdit;
    private List<String> itemList;
    private List<String> selectedItemsList;
    private Toolbar toolbarEdit;
    AlertDialog.Builder dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete);

        toolbarEdit = (Toolbar)findViewById(R.id.toolbarMostrar);
        setSupportActionBar(toolbarEdit);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setTitle("Mostrar" );
        }

        sharedPref = getDefaultSharedPreferences(getApplicationContext());

        Bundle datos = getIntent().getExtras();
        registro = new Gson().fromJson(datos.getString("registro"), Registro.class);
        registro1 = new Gson().fromJson(registro.toString(), Registro.class);

        String jsonFileString = getJsonFromAssets(this, "Medicamentos.json");
        listMedicamentos = new Gson().fromJson(jsonFileString, ListMedicamentos.class);

        nivel = findViewById(R.id.editNivel);
        nivel.setText(" "+registro1.getNivel());
        nivel.setEnabled(false);

        textInputLayoutNivel = findViewById(R.id.textInputLayoutNivel);

        insulina = findViewById(R.id.editInsulina);
        insulina.setText(" "+registro1.getInsulina());
        insulina.setEnabled(false);

        itemList = listMedicamentos.getListMedicamentos();
        selectedItemsList = new ArrayList<>();

        spinnerEdit = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEdit.setAdapter(adapter);

        medicamentos = findViewById(R.id.editMedicamentos);
        medicamentos.setText(registro1.getMedicamentos());
        medicamentos.setEnabled(false);

        aplicar = findViewById(R.id.botonAplicar);
        aplicar.setVisibility(View.INVISIBLE);

        //Comprobado lo anterior se inicializan las siguientes variables.
        String inputNivel = String.valueOf(nivel.getText());
        inputNivel = inputNivel.trim(); // Elimino los espacios que se quedan
        String inputInsulina = String.valueOf(insulina.getText());
        inputInsulina = inputInsulina.trim();

        int comprobacionNivel = Integer.parseInt(inputNivel);
        if (comprobacionNivel>90&&comprobacionNivel<300) {
            textInputLayoutNivel.setBoxStrokeColor(ContextCompat.getColor(this, R.color.black)); // Cambia el color del borde
            textInputLayoutNivel.setBoxBackgroundColor(ContextCompat.getColor(this, R.color.green)); // Cambia el color del fondo
        } else {
            textInputLayoutNivel.setBoxStrokeColor(ContextCompat.getColor(this, R.color.black)); // Restaura el color del borde por defecto
            textInputLayoutNivel.setBoxBackgroundColor(ContextCompat.getColor(this, R.color.red)); // Restaura el color del fondo por defecto
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
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
        aplicar.setOnClickListener(this::onClickInitEdit);
        spinnerEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    }

    public void onClickInitEdit(View view) {
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
                }else if(number<=30){
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
                                paramObject.addProperty("nivel", nivelFinal);
                                paramObject.addProperty("insulina", insulinaFinal);
                                paramObject.addProperty("medicamentos", medicamentos.getText().toString());
                                patchRegitros(paramObject, registro1.getId());
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

    public void initDialog(String frase, EditText focus){
        dialog = new AlertDialog.Builder(EditDeleteActivity.this);
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
    private void updateSelectedItemsText() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : selectedItemsList) {
            stringBuilder.append(item).append("\n");
        }
        medicamentos.setText(registro1.getMedicamentos()+"\n"+stringBuilder.toString());
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