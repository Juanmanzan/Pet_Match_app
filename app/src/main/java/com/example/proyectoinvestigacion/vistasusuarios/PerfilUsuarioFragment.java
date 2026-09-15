package com.example.proyectoinvestigacion.vistasusuarios;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.proyectoinvestigacion.MainActivity;
import com.example.proyectoinvestigacion.MainActivity2;
import com.example.proyectoinvestigacion.R;
import com.example.proyectoinvestigacion.URL;
import com.example.proyectoinvestigacion.databinding.FragmentDetallesAdicionalesBinding;
import com.example.proyectoinvestigacion.databinding.FragmentPerfilUsuarioBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilUsuarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilUsuarioFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentPerfilUsuarioBinding binding;

    int idUsuario;
    String nombreUsuario;
    String nombre;
    String apellido;
    String correo;
    String nivel;
    boolean isLoggedIn;

    String Miurl = new URL().Miurl(); //

    public PerfilUsuarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilUsuarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilUsuarioFragment newInstance(String param1, String param2) {
        PerfilUsuarioFragment fragment = new PerfilUsuarioFragment();
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
        binding = FragmentPerfilUsuarioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        androidx.appcompat.app.AlertDialog.Builder alerta1 = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
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


        SharedPreferences prefs = requireContext().getSharedPreferences("usuario_sesion", Context.MODE_PRIVATE);

        idUsuario = prefs.getInt("idUsuario", -1);
        nombreUsuario = prefs.getString("nombreUsuario", "");
        nombre = prefs.getString("nombre", "");
        apellido = prefs.getString("apellido", "");
        correo = prefs.getString("correo", "");
        nivel = prefs.getString("nivel", "");
        isLoggedIn = prefs.getBoolean("isLoggedIn", false);


       binding.UsuarioCambio.setText(nombre + " " + apellido);
       binding.CorreoUsuarioCambio.setText(correo);
       binding.UsernameConsulta.setText(nombreUsuario);


        binding.button17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(requireContext())
                        .setTitle("Cerrar sesión")
                        .setIcon(R.drawable.update)
                        .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                        .setPositiveButton("Sí", (dialog, which) -> {

                            SharedPreferences prefs = requireContext().getSharedPreferences("usuario_sesion", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.clear();
                            editor.apply();

                            // Redirigir al login o MainActivity
                            Intent intent = new Intent(requireContext(), MainActivity2.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        })
                        .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });

        binding.cerrarAplicacionUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder alerta1 = new androidx.appcompat.app.AlertDialog.Builder(getContext());
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
                        Toast.makeText(getContext(), "Estas de regreso", Toast.LENGTH_SHORT).show();
                    }
                });
                alerta1.show();
            }
        });

    }
}