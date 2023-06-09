package com.example.proyecto;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFragmentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        DrawerLayout.DrawerListener{
    Button paginasAlante, paginasAtras;
    TextView paginas;
    int page;
    String token;
    SharedPreferences sharedPref;

    Registros allRegistros;
    ListView mLeadsList;
    ListAdapter mLeadsAdapter;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    ActivityResultLauncher<Intent> activityAddResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        sharedPref = getDefaultSharedPreferences(
                getApplicationContext());
        token = sharedPref.getString("token", " ");


        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setTitle("List Fragment" );
        }


        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        MenuItem menuItem = navigationView.getMenu().getItem(0);
        onNavigationItemSelected(menuItem);
        navigationView.getMenu().getItem(0).setChecked(false);
        navigationView.getMenu().getItem(0).setCheckable(false);

        View header = navigationView.getHeaderView(0);
        TextView headerTitle = header.findViewById(R.id.header_title);


        mLeadsList = findViewById(R.id.superListView);
        mLeadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Registro r = mLeadsAdapter.getItem(position);
                pulsarEdit(r);
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



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDrawerSlide(@NonNull View view, float v) {
        //cambio en la posición del drawer
    }

    @Override
    public void onDrawerOpened(@NonNull View view) {
        //el drawer se ha abierto completamente
        //Toast.makeText(this, getString(R.string.navigation_drawer_open),
        // Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDrawerClosed(@NonNull View view) {
        //el drawer se ha cerrado completamente
    }

    @Override
    public void onDrawerStateChanged(int i) {
        //cambio de estado, puede ser STATE_IDLE, STATE_DRAGGING or STATE_SETTLING
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnPerfil:
                System.out.println("Pulsado");
                pulsarProfile();
                break;
            case R.id.btnChange:
                pulsarChangePassword();
                break;
            case R.id.btnSalir:
                System.out.println("Pulsado");
                postLogout();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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

    public void postLogout(){
        Call<JsonElement> call = RetrofitClient.getInstance().getMyApi().postLogout();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                System.out.println(response.body());
                finish();
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.btnAñadir:
                pulsarAdd();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void pulsarEdit(Registro registro){
        Intent intent = new Intent(getApplicationContext(), MostrarACtivity.class);
        intent.putExtra("registro", registro.toString());
        activityAddResultLauncher.launch(intent);
    }

    public void pulsarAdd(){
        Intent intent = new Intent(getApplicationContext(), AddActivity.class);
        activityAddResultLauncher.launch(intent);
    }
    public void pulsarProfile(){
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        activityAddResultLauncher.launch(intent);
    }
    public void pulsarChangePassword(){
        Intent intent = new Intent(getApplicationContext(), Change_Password_Activity.class);
        activityAddResultLauncher.launch(intent);
    }



}