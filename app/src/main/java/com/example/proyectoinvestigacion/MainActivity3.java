package com.example.proyectoinvestigacion;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.proyectoinvestigacion.databinding.ActivityMain3Binding;
import com.example.proyectoinvestigacion.vistas.MacotascrearFragment;
import com.example.proyectoinvestigacion.vistas.OperariocrearFragment;
import com.example.proyectoinvestigacion.vistas.ReportemascotasFragment;
import com.example.proyectoinvestigacion.vistas.ReporteusuariosFragment;
import com.example.proyectoinvestigacion.vistas.modificarOperadoresFragment;
import com.example.proyectoinvestigacion.vistas.modificarmascotasFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity3 extends AppCompatActivity {



    private ActivityMain3Binding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMain3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        String modo = getIntent().getStringExtra("modo");

        if (modo != null) {
            switch (modo) {
                case "modificar":
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_vista, new modificarmascotasFragment())
                            .commit();
                    binding.bottonNavegation.setSelectedItemId(R.id.crearMascotas); // <<< AÑADIDO
                    break;
                case "modificarOperador":
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_vista, new modificarOperadoresFragment())
                            .commit();
                    binding.bottonNavegation.setSelectedItemId(R.id.crearOperador); // <<< AÑADIDO
                    break;
                default:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_vista, new MacotascrearFragment())
                            .commit();
                    binding.bottonNavegation.setSelectedItemId(R.id.crearMascotas); // <<< AÑADIDO
                    break;
            }
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_vista, new MacotascrearFragment())
                    .commit();
            binding.bottonNavegation.setSelectedItemId(R.id.crearMascotas); // <<< AÑADIDO
        }


        binding.bottonNavegation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int id = item.getItemId();

                if (id == R.id.crearMascotas) {

                    selectedFragment = new MacotascrearFragment();


                } else if (id == R.id.crearOperador) {
                    selectedFragment = new OperariocrearFragment();
                } else if (id == R.id.verReportesMascotas) {
                    selectedFragment = new ReportemascotasFragment();
                } else if (id == R.id.verReportesUsuarios) {
                    selectedFragment = new ReporteusuariosFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_vista, selectedFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                    return true;
                }

                return false;
            }
        });



    }





}