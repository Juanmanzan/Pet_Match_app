package com.example.proyectoinvestigacion;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoinvestigacion.databinding.ActivityMain2Binding;
import com.example.proyectoinvestigacion.databinding.ActivityMain4Binding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {


    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder alerta1 = new AlertDialog.Builder(MainActivity2.this);
        alerta1.setIcon(R.drawable.cerrar);
        alerta1.setTitle("Aviso");
        alerta1.setMessage("Desea salir de la aplicacion");
        alerta1.setCancelable(false);
        alerta1.setNegativeButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        alerta1.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity2.this, "Estas de regreso", Toast.LENGTH_SHORT).show();
            }
        });
        alerta1.show();
    }







    private ActivityMain2Binding binding;
    String Miurl = new URL().Miurl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences prefs = getSharedPreferences("usuario_sesion", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            String nivel = prefs.getString("nivel", "");

            if (nivel.equalsIgnoreCase("admin")) {
                Intent admin = new Intent(MainActivity2.this, MainActivity3.class);
                Toast.makeText(MainActivity2.this, "Bienvenido administrador", Toast.LENGTH_SHORT).show();
                startActivity(admin);
                finish();

            } else if (nivel.equalsIgnoreCase("operador")) {

                Intent usuario = new Intent(MainActivity2.this, MainActivity8.class);
                Toast.makeText(MainActivity2.this, "Bienvenido operador", Toast.LENGTH_SHORT).show();
                startActivity(usuario);
                finish();

            } else if (nivel.equalsIgnoreCase("usuario")) {
                Intent usuario = new Intent(MainActivity2.this, MainActivity6.class);
                Toast.makeText(MainActivity2.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                startActivity(usuario);
                finish();
            }
        }

        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registro = new Intent(MainActivity2.this, MainActivity4.class);
                startActivity(registro);

            }
        });

        binding.inicioid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(binding.usuarioId.getText().toString().isEmpty() || binding.Contrasenaid.getText().toString().isEmpty())
                {

                    if (binding.usuarioId.getText().toString().isEmpty())
                    {
                        binding.usuarioId.setError("obligatorio");
                    }
                    if (binding.Contrasenaid.getText().toString().isEmpty())
                    {
                        binding.Contrasenaid.setError("obligatorio");
                    }
                }
                else
                {
                        loginUsuario(Miurl+"validar_usuarios.php");
                }

            }
        });



    }
    // metodo para validar usuarios
    private void loginUsuario(String URL) {
        String usuario = binding.usuarioId.getText().toString().trim();
        String contrasena = binding.Contrasenaid.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        String respuesta = response.trim();

                        if (respuesta.startsWith("{")) {
                            JSONObject jsonObject = new JSONObject(respuesta);

                            if (jsonObject.getString("status").equalsIgnoreCase("LOGIN_CORRECTO")) {

                                SharedPreferences prefs = getSharedPreferences("usuario_sesion", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.clear(); // Limpia cualquier dato anterior
                                editor.apply();

                                editor.putInt("idUsuario", jsonObject.getInt("idUsuario"));
                                editor.putString("nombreUsuario", jsonObject.getString("nombreUsuario"));
                                editor.putString("nombre", jsonObject.getString("nombre"));
                                editor.putString("apellido", jsonObject.getString("apellido"));
                                editor.putString("correo", jsonObject.getString("correo"));
                                editor.putString("nivel", jsonObject.getString("nivel"));
                                editor.putBoolean("isLoggedIn", true);
                                editor.apply();

                                String nivel = jsonObject.getString("nivel");

                                if (nivel.equalsIgnoreCase("admin")) {
                                    Intent admin = new Intent(MainActivity2.this, MainActivity3.class);
                                    Toast.makeText(MainActivity2.this, "Bienvenido administrador", Toast.LENGTH_SHORT).show();
                                    startActivity(admin);
                                    finish();

                                } else if (nivel.equalsIgnoreCase("operador")) {
                                    Intent operador = new Intent(MainActivity2.this, MainActivity8.class);
                                    Toast.makeText(MainActivity2.this, "Bienvenido operador", Toast.LENGTH_SHORT).show();
                                    startActivity(operador);
                                    finish();

                                } else {
                                    Intent usuarioVista = new Intent(MainActivity2.this, MainActivity6.class);
                                    Toast.makeText(MainActivity2.this, "Bienvenidor", Toast.LENGTH_SHORT).show();
                                    startActivity(usuarioVista);
                                    finish();
                                }

                            }

                        } else {
                            if (respuesta.equals("USUARIO_NO_EXISTE")) {
                                Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show();

                            } else if (respuesta.equals("CONTRASENA_INCORRECTA")) {
                                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(this, "Respuesta desconocida: " + respuesta, Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (Exception e) {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Error de conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nombreUsuario", usuario);
                params.put("contrasena", contrasena);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}