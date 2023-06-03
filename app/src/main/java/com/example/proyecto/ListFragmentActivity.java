package com.example.proyecto;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFragmentActivity extends AppCompatActivity {
    Button paginasAlante, paginasAtras;
    TextView paginas;
    int page;
    String token;
    SharedPreferences sharedPref;

    Registros allRegistros;
    ListView mLeadsList;
    ListAdapter mLeadsAdapter;

    ActivityResultLauncher<Intent> activityAddResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        sharedPref = getDefaultSharedPreferences(
                getApplicationContext());
        token = sharedPref.getString("token", " ");


        mLeadsList = findViewById(R.id.superListView);
        mLeadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Registro r = mLeadsAdapter.getItem(position);
                pulsarMostrar(r);
            }
        });

        paginasAlante = findViewById(R.id.alante);
        paginasAtras = findViewById(R.id.atras);
        paginas = findViewById(R.id.PAGINA);
        paginasAlante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRegitro(token, page+=1);
                paginas.setText(""+page);
            }
        });

        paginasAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRegitro(token, page-=1);
                paginas.setText(""+page);
            }
        });

        activityAddResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        System.out.println(result);
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            loadFirstPage();
                        }else{
                            getRegitro(token,page);
                        }
                    }
                });

        loadFirstPage();
    }

    public void loadFirstPage (){
        page=1;
        paginas.setText(""+page);
        getRegitro(token, page);
    }

    public void setButtons(String next, String previous){
        System.out.println("Pagina: "+page);
        if (previous == null){
            paginasAtras.setEnabled(false);
        } else{
            paginasAtras.setEnabled(true);
        }
        if (next == null){
            paginasAlante.setEnabled(false);
        }else {
            paginasAlante.setEnabled(true);
        }

    }
    public void setAdapter(){
        //Inicializar el adaptador con la fuente de datos.
        mLeadsAdapter = new ListAdapter(this, allRegistros.getRegistroList());

        //Relacionando la lista con el adaptador
        mLeadsList.setAdapter(mLeadsAdapter);
    }

    public void getRegitro(String token, int page){
        Call<JsonElement> call;
        if(page>1){
            call = RetrofitClient.getInstance().getMyApi().getRegistros(token, page);
        }else{
            call = RetrofitClient.getInstance().getMyApi().getRegistro(token);
        }
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    JsonElement body = response.body();
                    allRegistros = new Gson().fromJson(body, Registros.class);
                    setButtons(allRegistros.getNext(),allRegistros.getPrevious());
                    setAdapter();

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
                Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
            }

        });
    }

    public void pulsarMostrar(Registro registro){
        Intent intent = new Intent(getApplicationContext(), MostrarACtivity.class);
        intent.putExtra("registro", registro.toString());
        activityAddResultLauncher.launch(intent);
    }

}