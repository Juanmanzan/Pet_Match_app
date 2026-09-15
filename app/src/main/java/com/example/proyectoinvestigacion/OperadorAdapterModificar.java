package com.example.proyectoinvestigacion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperadorAdapterModificar extends ArrayAdapter<OperadorModificar> {

    private Context context;
    private List<OperadorModificar> operadorList;
    String Miurl = new URL().Miurl();

    public OperadorAdapterModificar(Context context, List<OperadorModificar> list, List<String> rolesDisponibles) {
        super(context, 0, list);
        this.context = context;
        this.operadorList = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.operadormodificarvista, parent, false);
        }

        OperadorModificar operador = operadorList.get(position);

        // Vincular vistas
        EditText nombre = convertView.findViewById(R.id.nombreModificarOperador);
        EditText apellido = convertView.findViewById(R.id.apellidoModificarOperador);
        EditText correo = convertView.findViewById(R.id.CorreoModificarOperador);
        EditText userName = convertView.findViewById(R.id.NombreUsuaModificarOperador);
        EditText contrasena = convertView.findViewById(R.id.contrasenaOperadorModificar);
        Button Guardarcambios = convertView.findViewById(R.id.GuardarCambiosOperador);
        Button modificar = convertView.findViewById(R.id.ModificarOperador);
        Button eliminar = convertView.findViewById(R.id.EliminarOperador);

        // Mostrar datos actuales
        nombre.setText(operador.nombreU);
        apellido.setText(operador.apellido);
        correo.setText(operador.correoElectronico);
        userName.setText(operador.nombreUsuario);
        contrasena.setText(operador.contrasena);
        int idconsulta = operador.idUsuario;

        // Deshabilitar campos por defecto
        nombre.setEnabled(false);
        apellido.setEnabled(false);
        correo.setEnabled(false);
        userName.setEnabled(false);
        contrasena.setEnabled(false);
        Guardarcambios.setVisibility(View.GONE);

        // Guardar valores originales
        final String[] originalNombre = {operador.nombreU};
        final String[] originalApellido = {operador.apellido};
        final String[] originalCorreo = {operador.correoElectronico};
        final String[] originalUsername = {operador.nombreUsuario};
        final String[] originalContrasena = {operador.contrasena};

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean habilitado = nombre.isEnabled();

                if (!habilitado) {

                    originalNombre[0] = nombre.getText().toString();
                    originalApellido[0] = apellido.getText().toString();
                    originalCorreo[0] = correo.getText().toString();
                    originalUsername[0] = userName.getText().toString();
                    originalContrasena[0] = contrasena.getText().toString();


                    nombre.setEnabled(true);
                    apellido.setEnabled(true);
                    correo.setEnabled(true);
                    userName.setEnabled(true);
                    contrasena.setEnabled(true);
                    Guardarcambios.setVisibility(View.VISIBLE);

                } else {

                    nombre.setText(originalNombre[0]);
                    apellido.setText(originalApellido[0]);
                    correo.setText(originalCorreo[0]);
                    userName.setText(originalUsername[0]);
                    contrasena.setText(originalContrasena[0]);

                    nombre.setEnabled(false);
                    apellido.setEnabled(false);
                    correo.setEnabled(false);
                    userName.setEnabled(false);
                    contrasena.setEnabled(false);
                    Guardarcambios.setVisibility(View.GONE);

                    Toast.makeText(context, "Cambios descartados", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Guardarcambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nombre.getText().toString().isEmpty() ||
                        apellido.getText().toString().isEmpty() ||
                        correo.getText().toString().isEmpty() ||
                        userName.getText().toString().isEmpty() ||
                        contrasena.getText().toString().isEmpty()) {

                    if (nombre.getText().toString().isEmpty()) nombre.setError("Obligatorio");
                    if (apellido.getText().toString().isEmpty()) apellido.setError("Obligatorio");
                    if (correo.getText().toString().isEmpty()) correo.setError("Obligatorio");
                    if (userName.getText().toString().isEmpty()) userName.setError("Obligatorio");
                    if (contrasena.getText().toString().isEmpty()) contrasena.setError("Obligatorio");

                } else {
                    // Actualizar
                    Actualizardatos(Miurl + "actualizar_operador.php", idconsulta, nombre.getText().toString(), apellido.getText().toString(),
                            userName.getText().toString(), contrasena.getText().toString(), correo.getText().toString(),
                            nombre, apellido, correo, userName, contrasena, Guardarcambios);
                }
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                new android.app.AlertDialog.Builder(context)
                        .setTitle("Confirmar eliminación")
                        .setMessage("¿Estás seguro de que deseas eliminar este operador?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            eliminaroperador(Miurl + "eliminar_operador.php", operador.idUsuario);
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });

        return convertView;
    }



    @Override
    public int getCount() {
        return operadorList != null ? operadorList.size() : 0;
    }

    public void Actualizardatos(String URL, int id, String nombreV, String apellidoV, String usernameV, String contrasenaV, String correoV,
                                EditText nombre, EditText apellido, EditText correo, EditText userName, EditText contrasena, Button Guardarcambios) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    String respuesta = response.trim();

                    switch (respuesta) {
                        case "usuario_repetido":
                            Toast.makeText(getContext(), "Nombre de usuario ya registrado", Toast.LENGTH_SHORT).show();
                            break;
                        case "correo_repetido":
                            Toast.makeText(getContext(), "Correo ya registrado", Toast.LENGTH_SHORT).show();
                            break;
                        case "success":
                            Toast.makeText(getContext(), "Operador actualizado correctamente", Toast.LENGTH_SHORT).show();

                            nombre.setEnabled(false);
                            apellido.setEnabled(false);
                            correo.setEnabled(false);
                            userName.setEnabled(false);
                            contrasena.setEnabled(false);
                            Guardarcambios.setVisibility(View.GONE);
                            break;
                        default:
                            Toast.makeText(getContext(), "Error inesperado: " + response, Toast.LENGTH_SHORT).show();
                            break;
                    }
                },
                error -> Toast.makeText(getContext(), "Error al actualizar: " + error, Toast.LENGTH_SHORT).show()
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("id", String.valueOf(id));
                parameters.put("nombre", nombreV);
                parameters.put("apellido", apellidoV);
                parameters.put("correo", correoV);
                parameters.put("usuario", usernameV);
                parameters.put("contrasena", contrasenaV);
                return parameters;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void eliminaroperador(String URL, int id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    Toast.makeText(getContext(), "Operador eliminado", Toast.LENGTH_SHORT).show();

                    // Eliminar el operador de la lista y actualizar la vista
                    for (int i = 0; i < operadorList.size(); i++) {
                        if (operadorList.get(i).idUsuario == id) {
                            operadorList.remove(i);
                            notifyDataSetChanged();
                            break;
                        }
                    }
                },
                error -> Toast.makeText(getContext(), "Error al eliminar: " + error.toString(), Toast.LENGTH_SHORT).show()
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("id", String.valueOf(id));
                return parameters;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }






}
