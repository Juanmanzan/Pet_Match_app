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

import com.example.proyectoinvestigacion.databinding.ActivityMain8Binding;
import com.example.proyectoinvestigacion.vistas.MacotascrearFragment;
import com.example.proyectoinvestigacion.vistas.ReportemascotasFragment;
import com.example.proyectoinvestigacion.vistas.modificarmascotasFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity8 extends AppCompatActivity {




    private ActivityMain8Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMain8Binding.inflate(getLayoutInflater());
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
                            .replace(R.id.frame_vistaOperador, new modificarmascotasFragment())
                            .commit();
                    binding.bottonNavegationOperador.setSelectedItemId(R.id.crearMascotas);
                    break;
                default:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_vistaOperador, new MacotascrearFragment())
                            .commit();
                    binding.bottonNavegationOperador.setSelectedItemId(R.id.crearMascotas);
                    break;
            }
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_vistaOperador, new MacotascrearFragment())
                    .commit();
            binding.bottonNavegationOperador.setSelectedItemId(R.id.crearMascotas);
        }


        binding.bottonNavegationOperador.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int id = item.getItemId();

                if (id == R.id.Nav_creareditarMascotas) {

                    selectedFragment = new MacotascrearFragment();

                } else if (id == R.id.nav_tablaMascotas) {
                    selectedFragment = new ReportemascotasFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_vistaOperador, selectedFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                    return true;
                }

                return false;
            }
        });




    }
}