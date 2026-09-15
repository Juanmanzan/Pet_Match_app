package com.example.proyectoinvestigacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoinvestigacion.databinding.ActivityMain3Binding;
import com.example.proyectoinvestigacion.databinding.ActivityMain4Binding;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity4 extends AppCompatActivity {

    String Miurl = new URL().Miurl();

    private ActivityMain4Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMain4Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        binding.button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(binding.Correoid.getText().toString().isEmpty() || binding.nombreregistroid.getText().toString().isEmpty() ||
                        binding.ApellidoId.getText().toString().isEmpty() || binding.NombreUsuarioid.getText().toString().isEmpty()
                           || binding.Correoid.getText().toString().isEmpty()  ||  binding.ConfirmacionContrasenaId.getText().toString().isEmpty()  )
                {

                    if (binding.Correoid.getText().toString().isEmpty()){binding.Correoid.setError("Obligatorio"); }
                    if (binding.nombreregistroid.getText().toString().isEmpty()){binding.nombreregistroid.setError("Obligatorio");}
                    if (binding.ApellidoId.getText().toString().isEmpty()){binding.ApellidoId.setError("Obligatorio");}
                    if (binding.NombreUsuarioid.getText().toString().isEmpty()){binding.NombreUsuarioid.setError("Obligatorio");}
                    if (binding.Contrasenaid.getText().toString().isEmpty()){binding.Contrasenaid.setError("Obligatorio");}
                    if (binding.ConfirmacionContrasenaId.getText().toString().isEmpty()){binding.ConfirmacionContrasenaId.setError("Obligatorio");}

                }
                else
                {
                    if(binding.Contrasenaid.getText().toString().equals(binding.ConfirmacionContrasenaId.getText().toString())) {

                                    isValidEmail(binding.Correoid.getText().toString());

                        if (!isValidEmail(binding.Correoid.getText().toString())) {

                            Toast.makeText(MainActivity4.this, "Correo no válido", Toast.LENGTH_SHORT).show();
                        } else {
                            generarCodigo(Miurl + "generarcodigo.php");
                        }


                    }
                    else
                    {
                        Toast.makeText(MainActivity4.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    // procedimientos de registro

    public void generarCodigo(String URL) {

        String correo = binding.Correoid.getText().toString();
        String usuario = binding.NombreUsuarioid.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    String cleanResponse = response.trim();


                    if (cleanResponse.equalsIgnoreCase("CORREO_EXISTE")) {
                        Toast.makeText(MainActivity4.this, "Ya existe una cuenta asociada a este correo", Toast.LENGTH_SHORT).show();
                    }
                    if (cleanResponse.equalsIgnoreCase("USUARIO_EXISTE")) {
                        Toast.makeText(MainActivity4.this, "Usuario no disponible", Toast.LENGTH_SHORT).show();
                    }
                    if (cleanResponse.equalsIgnoreCase("ENVIADO")) {
                        Toast.makeText(MainActivity4.this, "Código enviado al correo", Toast.LENGTH_SHORT).show();

                        // Solo cuando el código fue enviado correctamente, pasamos a la siguiente pantalla
                        Intent validarcorreo = new Intent(MainActivity4.this, MainActivity5.class);
                        validarcorreo.putExtra("nombres", binding.nombreregistroid.getText().toString());
                        validarcorreo.putExtra("apellidos", binding.ApellidoId.getText().toString());
                        validarcorreo.putExtra("usuario", binding.NombreUsuarioid.getText().toString());
                        validarcorreo.putExtra("correo", correo);
                        validarcorreo.putExtra("contrasena", binding.Contrasenaid.getText().toString());
                        startActivity(validarcorreo);
                        finish();

                    }
                },
                error -> Toast.makeText(MainActivity4.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("correoElectronico", correo);
                params.put("nombreUsuario",usuario);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(MainActivity4.this);
        queue.add(request);
    }

    public boolean isValidEmail(String emailToReview) {
        final String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return emailToReview.matches(regex);
    }



}