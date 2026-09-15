package com.example.proyectoinvestigacion.vistasusuarios;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoinvestigacion.InicioMascotasAdapter;
import com.example.proyectoinvestigacion.R;
import com.example.proyectoinvestigacion.URL;
import com.example.proyectoinvestigacion.databinding.FragmentInicioUsuarioBinding;
import com.example.proyectoinvestigacion.databinding.FragmentReportemascotasBinding;
import com.example.proyectoinvestigacion.inicioVistaMascota;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link inicioUsuarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class inicioUsuarioFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentInicioUsuarioBinding binding;
    String Miurl = new URL().Miurl(); //

     ListView listView;
    ArrayList<inicioVistaMascota> mascotaList;
    InicioMascotasAdapter adapter;

    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder alerta1 = new AlertDialog.Builder(getContext());
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


    public inicioUsuarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment inicioUsuarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static inicioUsuarioFragment newInstance(String param1, String param2) {
        inicioUsuarioFragment fragment = new inicioUsuarioFragment();
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
        binding = FragmentInicioUsuarioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = binding.ListViewMascotasInicio;
        mascotaList = new ArrayList<>();

        cargarMascotasPorBusqueda("sin_busqueda");


        binding.button4.setOnClickListener(v -> {

            String valorBuscar = binding.editTextText7.getText().toString().trim();

            if (valorBuscar.isEmpty()) {
                cargarMascotasPorBusqueda("sin_busqueda");
            } else {
                cargarMascotasPorBusqueda(valorBuscar);
            }
        });


    }


    public void cargarMascotasPorBusqueda(String busqueda) {

        String URL = "https://powderblue-lemur-595097.hostingersite.com/petmatch/listar_inicio_mascotas.php?busqueda=" + busqueda;

        mascotaList.clear(); // Limpia la lista antes de recargar

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject mascotaJson = response.getJSONObject(i);

                            inicioVistaMascota mascota = new inicioVistaMascota();
                            mascota.idMascota = mascotaJson.getInt("idmascota");
                            mascota.nombreMInicio = mascotaJson.getString("nombreMInicio");
                            mascota.razaMInicio = mascotaJson.getString("razaMInicio");
                            mascota.edadMInicio = mascotaJson.getString("edadMInicio");
                            mascota.generoMInicio = mascotaJson.getString("generoMInicio");
                            mascota.rutaimagenMInicio = mascotaJson.getString("rutaimagenMInicio");

                            mascotaList.add(mascota);
                        }

                        if (adapter == null) {
                            adapter = new InicioMascotasAdapter(requireContext(), mascotaList);
                            listView.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }

                        if (mascotaList.isEmpty()) {
                            Toast.makeText(requireContext(), "No se encontraron resultados.", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        if (isAdded()) {
                            Toast.makeText(requireContext(), "Error al procesar los datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                error -> {
                    if (isAdded()) {
                        Toast.makeText(requireContext(), "Error de conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }







}