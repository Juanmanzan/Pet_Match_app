package com.example.proyectoinvestigacion.vistasusuarios;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.proyectoinvestigacion.MainActivity6;
import com.example.proyectoinvestigacion.MainActivity7;
import com.example.proyectoinvestigacion.R;
import com.example.proyectoinvestigacion.URL;
import com.example.proyectoinvestigacion.databinding.FragmentDetallesAdicionalesBinding;
import com.example.proyectoinvestigacion.databinding.FragmentInicioUsuarioBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link detallesAdicionalesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class detallesAdicionalesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentDetallesAdicionalesBinding binding;
    String Miurl = new URL().Miurl(); //

    public detallesAdicionalesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment detallesAdicionalesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static detallesAdicionalesFragment newInstance(String param1, String param2) {
        detallesAdicionalesFragment fragment = new detallesAdicionalesFragment();
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
        binding = FragmentDetallesAdicionalesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        int idMascota = getArguments() != null ? getArguments().getInt("idMascota", -1) : -1;

        if (idMascota != -1) {

                cargarDetallesMascota(Miurl+"detalles_mascota.php", idMascota);

        }

    }

    public void cargarDetallesMascota(String URL, int id) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + "?idMascota=" + id,
                response -> {
                    try {
                        JSONObject mascota = new JSONObject(response);

                        // Verifica si existe un campo de error
                        if (mascota.has("error")) {
                            Toast.makeText(getContext(), "Mascota no encontrada", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Setea los campos
                        binding.NombreDetalles.setText(mascota.getString("nombre"));
                        binding.Razadetalles.setText("Raza: " + mascota.getString("raza"));
                        binding.EdadDetalles.setText("Edad: " + mascota.getString("edad") + " año(s)");
                        binding.GeneroDetalles.setText("Género: " + mascota.getString("genero"));
                        binding.DescipcionDetalles.setText(mascota.getString("descripcion"));

                        Glide.with(requireContext())
                                .load(mascota.getString("rutaImagen"))
                                .into(binding.imagenDetalles);

                        // Acción del botón
                        binding.VerVideoDetalles.setOnClickListener(v -> {
                            Intent video = new Intent(getContext(), MainActivity7.class);
                            try {
                                video.putExtra("rutaVideo", mascota.getString("rutaVideo"));
                                startActivity(video);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "No se pudo cargar el video", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Error de conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }




}