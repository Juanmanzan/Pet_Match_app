package com.example.proyectoinvestigacion.vistas;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.proyectoinvestigacion.MainActivity3;
import com.example.proyectoinvestigacion.OperadorAdapterModificar;
import com.example.proyectoinvestigacion.OperadorModificar;
import com.example.proyectoinvestigacion.R;
import com.example.proyectoinvestigacion.URL;
import com.example.proyectoinvestigacion.databinding.FragmentMacotascrearBinding;
import com.example.proyectoinvestigacion.databinding.FragmentModificarOperadoresBinding;
import com.example.proyectoinvestigacion.databinding.FragmentOperariocrearBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link modificarOperadoresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class modificarOperadoresFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentModificarOperadoresBinding binding;

    String Miurl = new URL().Miurl();

    int idUsuario;
    String nombreUsuario;
    String nombre;
    String apellido;
    String correo;
    String nivel;

    // vincular el adapter al frame

    List<OperadorModificar> listaOperadores = new ArrayList<>();
    ListView listViewOperadores;
    OperadorAdapterModificar adapter;

    String Busqueda;


    public modificarOperadoresFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment modificarOperadoresFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static modificarOperadoresFragment newInstance(String param1, String param2) {
        modificarOperadoresFragment fragment = new modificarOperadoresFragment();
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
        binding = FragmentModificarOperadoresBinding.inflate(inflater, container, false);
        return  binding.getRoot();

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listViewOperadores = view.findViewById(R.id.operadorlista);

        adapter = new OperadorAdapterModificar(requireContext(), listaOperadores, null);
        listViewOperadores.setAdapter(adapter);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Busqueda = binding.editTextText.getText().toString();

               if(Busqueda.isEmpty())
               {
                   cargarOperadores("sin_busqueda");
               }
               else {

                   cargarOperadores(Busqueda);
               }


            }
        });

        cargarOperadores("sin_busqueda");




    }



    public  void cargarOperadores (String busqueda)
    {

        String URL = "https://powderblue-lemur-595097.hostingersite.com/petmatch/listar_operador.php?busqueda=" + busqueda;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    listaOperadores.clear();

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);

                            OperadorModificar operador = new OperadorModificar();
                            operador.idUsuario = jsonObject.getInt("idUsuario");
                            operador.nombreU = jsonObject.getString("nombre");
                            operador.apellido = jsonObject.getString("apellido");
                            operador.nombreUsuario = jsonObject.getString("nombreUsuario");
                            operador.contrasena = jsonObject.getString("contrasena");
                            operador.correoElectronico = jsonObject.getString("correoElectronico");
                            operador.nivel = jsonObject.getString("nivel");

                            listaOperadores.add(operador);
                        }

                        adapter.notifyDataSetChanged(); // Actualiza la vista

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error al procesar datos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show()
        );

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);


    }





}