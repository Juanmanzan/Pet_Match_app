package com.example.proyectoinvestigacion.vistas;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoinvestigacion.MainActivity3;
import com.example.proyectoinvestigacion.R;
import com.example.proyectoinvestigacion.URL;
import com.example.proyectoinvestigacion.databinding.FragmentMacotascrearBinding;
import com.example.proyectoinvestigacion.databinding.FragmentOperariocrearBinding;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OperariocrearFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OperariocrearFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentOperariocrearBinding binding;

    String Miurl = new URL().Miurl();

    int idUsuario;
    String nombreUsuario;
    String nombre;
    String apellido;
    String correo;
    String nivel;
    boolean isLoggedIn;

    public OperariocrearFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OperariocrearFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OperariocrearFragment newInstance(String param1, String param2) {
        OperariocrearFragment fragment = new OperariocrearFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOperariocrearBinding.inflate(inflater, container, false);
        return  binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(requireContext());
                        alerta1.setIcon(R.drawable.cerrar);
                        alerta1.setTitle("Aviso");
                        alerta1.setMessage("¿Desea salir de la aplicación?");
                        alerta1.setCancelable(false);
                        alerta1.setNegativeButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requireActivity().finishAffinity(); // mejor que System.exit(0)
                            }
                        });
                        alerta1.setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(requireContext(), "Estás de regreso", Toast.LENGTH_SHORT).show();
                            }
                        });
                        alerta1.show();
                    }
                }
        );






        binding.ModificarRegistrosOperador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cambioModificar = new Intent(getContext(), MainActivity3.class);
                cambioModificar.putExtra("modo", "modificarOperador");
                startActivity(cambioModificar);

            }
        });



        binding.BtnRegistrarOperador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (binding.nombreOperadoR.getText().toString().isEmpty() ||
                        binding.ApellidosOperadorR.getText().toString().isEmpty() ||
                        binding.NombredeUsuarioOperadorR.getText().toString().isEmpty() ||
                        binding.ContrasenaOperador1R.getText().toString().isEmpty() ||
                        binding.ContrasenaOperador2R.getText().toString().isEmpty()) {

                    if (binding.nombreOperadoR.getText().toString().isEmpty()) {
                        binding.nombreOperadoR.setError("Campo obligatorio");
                    }

                    if (binding.ApellidosOperadorR.getText().toString().isEmpty()) {
                        binding.ApellidosOperadorR.setError("Campo obligatorio");
                    }

                    if (binding.NombredeUsuarioOperadorR.getText().toString().isEmpty()) {
                        binding.NombredeUsuarioOperadorR.setError("Campo obligatorio");
                    }

                    if (binding.ContrasenaOperador1R.getText().toString().isEmpty()) {
                        binding.ContrasenaOperador1R.setError("Campo obligatorio");
                    }

                    if (binding.ContrasenaOperador2R.getText().toString().isEmpty()) {
                        binding.ContrasenaOperador2R.setError("Campo obligatorio");
                    }

                }
                else
                {
                    if(binding.ContrasenaOperador1R.getText().toString().equals( binding.ContrasenaOperador2R.getText().toString()))
                    {
                        registrarOperador(Miurl+"insertar_operador.php");
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });


    }

    public void registrarOperador(String URL) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.trim().equalsIgnoreCase("usuario_repetido")) {
                    Toast.makeText(getContext(), "El nombre de usuario ya está registrado", Toast.LENGTH_SHORT).show();
                } else if (response.trim().equalsIgnoreCase("correo_repetido")) {
                    Toast.makeText(getContext(), "El correo ya está registrado", Toast.LENGTH_SHORT).show();
                } else if (response.trim().equalsIgnoreCase("success")) {
                    Toast.makeText(getContext(), "Operador insertado correctamente", Toast.LENGTH_SHORT).show();

                    // Limpiar campos
                    binding.NombredeUsuarioOperadorR.setText("");
                    binding.ApellidosOperadorR.setText("");
                    binding.nombreOperadoR.setText("");
                    binding.ContrasenaOperador2R.setText("");
                    binding.ContrasenaOperador1R.setText("");
                    binding.CorreoOperadorR.setText("");
                } else {
                    Toast.makeText(getContext(), "Respuesta inesperada: " + response, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error de conexión: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("nombre", binding.nombreOperadoR.getText().toString());
                parameters.put("apellido", binding.ApellidosOperadorR.getText().toString());
                parameters.put("correo", binding.CorreoOperadorR.getText().toString());
                parameters.put("usuario", binding.NombredeUsuarioOperadorR.getText().toString());
                parameters.put("contrasena", binding.ContrasenaOperador2R.getText().toString());
                return parameters;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }




}