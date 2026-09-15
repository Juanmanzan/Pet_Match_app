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

import com.example.proyectoinvestigacion.databinding.ActivityMain6Binding;
import com.example.proyectoinvestigacion.vistasusuarios.AcercaDeNosotrosFragment;
import com.example.proyectoinvestigacion.vistasusuarios.PerfilUsuarioFragment;
import com.example.proyectoinvestigacion.vistasusuarios.detallesAdicionalesFragment;
import com.example.proyectoinvestigacion.vistasusuarios.inicioUsuarioFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity6 extends AppCompatActivity {


    private ActivityMain6Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMain6Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        int idRecibida = getIntent().getIntExtra("idMascota", -1);



        binding.BottonNavegationClientes.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int id = item.getItemId();

                if (id == R.id.nav_inicio) {

                    selectedFragment = new inicioUsuarioFragment();

                } else if (id == R.id.nav_perfil) {
                    selectedFragment = new PerfilUsuarioFragment();
                } else if (id == R.id.nav_acercaNosotros) {
                    selectedFragment = new AcercaDeNosotrosFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_cliente, selectedFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                    return true;
                }

                return false;
            }
        });
        if (idRecibida != -1) {
            detallesAdicionalesFragment fragment = new detallesAdicionalesFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("idMascota", idRecibida); // Pasar la ID al fragmento
            fragment.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_cliente, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_cliente, new inicioUsuarioFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

            binding.BottonNavegationClientes.setSelectedItemId(R.id.nav_inicio);
        }






    }
}