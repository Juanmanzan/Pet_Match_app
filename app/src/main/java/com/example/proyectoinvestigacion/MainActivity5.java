package com.example.proyectoinvestigacion;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoinvestigacion.databinding.ActivityMain4Binding;
import com.example.proyectoinvestigacion.databinding.ActivityMain5Binding;

import java.util.HashMap;
import java.util.Map;

public class MainActivity5 extends AppCompatActivity {

    String Miurl = new URL().Miurl();

    private ActivityMain5Binding binding;

    String nombres, apellidos, usuario, correo, contrasena;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMain5Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nombres = getIntent().getStringExtra("nombres");
        apellidos = getIntent().getStringExtra("apellidos");
        usuario = getIntent().getStringExtra("usuario");
        correo = getIntent().getStringExtra("correo");
        contrasena = getIntent().getStringExtra("contrasena");



        binding.validacionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (binding.Codigoid.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity5.this, "Ingrese un código", Toast.LENGTH_SHORT).show();
                } else if (binding.Codigoid.length() != 4) {
                    Toast.makeText(MainActivity5.this, "El código debe tener 4 dígitos", Toast.LENGTH_SHORT).show();
                } else {
                    verificarCodigo(Miurl + "verificar_codigo.php", correo);
                }

            }
        });

        binding.CorreoEquivocadoId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText input = new EditText(MainActivity5.this);
                input.setHint("Ingrese nuevo correo");
                input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                // Crear el diálogo
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity5.this);
                builder.setTitle("Correo Nuevo");
                builder.setMessage("Por favor, ingresa un nuevo correo:");
                builder.setView(input);

                // Botón "Aceptar"
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nuevoCorreo = input.getText().toString().trim();

                        if (!nuevoCorreo.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(nuevoCorreo).matches()) {
                            correo = (nuevoCorreo);
                            generarCodigo(Miurl+"generarcodigo.php");

                        } else {
                            Toast.makeText(MainActivity5.this, "Correo inválido", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // Botón "Cancelar"
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                // Mostrar el diálogo
                builder.show();
            }

        });



    }

    // funcion de verificacion

    public void verificarCodigo(String URL, String correo) {

        String codigo = binding.Codigoid.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    String clean = response.trim();

                    if (clean.equalsIgnoreCase("VALIDO")) {
                        Toast.makeText(this, "Código verificado", Toast.LENGTH_SHORT).show();

                        insertarUsuario(Miurl + "registrar_usuario.php");

                    } else {
                        Toast.makeText(this, "Código incorrecto o expirado", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("correoElectronico", correo);
                params.put("codigo", codigo);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    // metodo para insertar el usuario

    public void insertarUsuario(String URL) {
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    if (response.trim().equalsIgnoreCase("REGISTRADO")) {
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        Intent loguin = new Intent(MainActivity5.this, MainActivity2.class);
                        startActivity(loguin);
                        finish();
                    } else {
                        Toast.makeText(this, "Error al registrar: " + response, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", nombres);
                params.put("apellido", apellidos);
                params.put("usuario", usuario);
                params.put("correo", correo);
                params.put("contrasena", contrasena);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    public void generarCodigo(String URL) {


        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    String cleanResponse = response.trim();


                    if (cleanResponse.equalsIgnoreCase("CORREO_EXISTE")) {
                        Toast.makeText(MainActivity5.this, "Ya existe una cuenta asociada a este correo", Toast.LENGTH_SHORT).show();
                    }
                    if (cleanResponse.equalsIgnoreCase("USUARIO_EXISTE")) {
                        Toast.makeText(MainActivity5.this, "Usuario no disponible", Toast.LENGTH_SHORT).show();
                    }
                    if (cleanResponse.equalsIgnoreCase("ENVIADO")) {
                        Toast.makeText(MainActivity5.this, "Código enviado al correo", Toast.LENGTH_SHORT).show();

                    }
                },
                error -> Toast.makeText(MainActivity5.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("correoElectronico", correo);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(MainActivity5.this);
        queue.add(request);
    }



}