package com.example.proyecto;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFragmentActivity extends AppCompatActivity {
    Button paginasAlante, paginasAtras;
    TextView paginas;
    int page;
    SharedPreferences sharedPref;

    Registros allRegistros;
    ListView mLeadsList;
    ListAdapter mLeadsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        sharedPref = getDefaultSharedPreferences(
                getApplicationContext());
        String token = sharedPref.getString("token", " ");
        System.out.println(token);
        getRegitro(token);



        mLeadsList = findViewById(R.id.superListView);
        mLeadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Registro r = mLeadsAdapter.getItem(position);


            }
        });


    }
    public void setAdapter(){
        //Inicializar el adaptador con la fuente de datos.
        mLeadsAdapter = new ListAdapter(this, allRegistros.getRegistroList());

        //Relacionando la lista con el adaptador
        mLeadsList.setAdapter(mLeadsAdapter);

    }

    public void getRegitro(String token){
        Call<JsonElement> call = RetrofitClient.getInstance().getMyApi().getRegistro(token);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    System.out.println( response.body().toString());
                    JsonElement body = response.body();
                    System.out.println(body);


                    allRegistros = new Gson().fromJson(body, Registros.class);
                    //setButtons(allSnippets.getNext(),allSnippets.getPrevious());
                    setAdapter();

                }else{
                    System.out.println(response.hashCode());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                System.out.println(t);
                System.out.println("An error has occurred");
            }

        });
    }

}