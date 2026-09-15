package com.example.proyectoinvestigacion.vistas;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.example.proyectoinvestigacion.MainActivity3;
import com.example.proyectoinvestigacion.MascotaAdapterModificar;
import com.example.proyectoinvestigacion.MascotasModificar;
import com.example.proyectoinvestigacion.R;
import com.example.proyectoinvestigacion.URL;
import com.example.proyectoinvestigacion.databinding.FragmentMacotascrearBinding;
import com.example.proyectoinvestigacion.databinding.FragmentModicarmascotasBinding;

import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link modificarmascotasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class modificarmascotasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentModicarmascotasBinding binding;



    String Miurl = new URL().Miurl();


    int idUsuario;
    String nombreUsuario;
    String nombre;
    String apellido;
    String correo;
    String nivel;
    List<String> listaGenero;
    List<String> listaEspecie;

    boolean desplegago;


    ListView listView;
    List<MascotasModificar> listaMascotas = new ArrayList<>();
    MascotaAdapterModificar adapter;

    private final int PICK_IMAGE_REQUEST = 1;
    private final int PICK_VIDEO_REQUEST = 2;
    private int posicionSeleccionada = -1;
    private static final String URL_SUBIDA_IMAGEN = "https://powderblue-lemur-595097.hostingersite.com/petmatch/subir_imagen.php";
    private static final String URL_SUBIDA_VIDEO = "https://powderblue-lemur-595097.hostingersite.com/petmatch/subir_video.php";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public modificarmascotasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment modicarmascotasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static modificarmascotasFragment newInstance(String param1, String param2) {
        modificarmascotasFragment fragment = new modificarmascotasFragment();
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

        binding = FragmentModicarmascotasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void abrirSelectorDeImagen() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    private void abrirSelectorDeVideo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "Selecciona un video"), PICK_VIDEO_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedUri = data.getData();
            if (posicionSeleccionada >= 0 && posicionSeleccionada < listaMascotas.size()) {
                MascotasModificar mascota = listaMascotas.get(posicionSeleccionada);

                if (requestCode == PICK_IMAGE_REQUEST) {
                    mascota.uriImagen = selectedUri;
                    Toast.makeText(getContext(), "Imagen seleccionada", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                } else if (requestCode == PICK_VIDEO_REQUEST) {
                    mascota.uriVideo = selectedUri;
                    Toast.makeText(getContext(), "Video seleccionado", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireContext().getSharedPreferences("usuario_sesion", Context.MODE_PRIVATE);

        idUsuario = prefs.getInt("idUsuario", -1);
        nombreUsuario = prefs.getString("nombreUsuario", "");
        nombre = prefs.getString("nombre", "");
        apellido = prefs.getString("apellido", "");
        correo = prefs.getString("correo", "");
        nivel = prefs.getString("nivel", "");


        listView = binding.listViewMascotas;
        listaMascotas = new ArrayList<>();
        cargarDatos("sin_busqueda");
        List<String> listaGenero = Arrays.asList("Seleccionar género", "Macho", "Hembra");
        List<String> listaEspecie = Arrays.asList("Seleccionar especie", "Gato", "Perro");
        if (adapter == null) {
            adapter = new MascotaAdapterModificar(requireContext(), listaMascotas, listaGenero, listaEspecie);

            adapter.setOnCargarImagenClickListener(position -> {
                posicionSeleccionada = position;
                abrirSelectorDeImagen();
            });

            adapter.setOnCargarVideoClickListener(position -> {
                posicionSeleccionada = position;
                abrirSelectorDeVideo();
            });

            listView.setAdapter(adapter);
        }

        adapter.setOnGuardarClickListener((int position, MascotasModificar mascota) -> {
            if (mascota.uriImagen != null) {
                subirArchivoAHostinger(mascota.uriImagen, URL_SUBIDA_IMAGEN, "imagen", linkImagen -> {
                    if (linkImagen != null) {
                        mascota.rutaimagen = linkImagen;

                        if (mascota.uriVideo != null) {
                            subirArchivoAHostinger(mascota.uriVideo, URL_SUBIDA_VIDEO, "video", linkVideo -> {
                                if (linkVideo != null) {
                                    mascota.rutavideo = linkVideo;
                                    actualizarMascotaEnBase(mascota);
                                }
                            });
                        } else {
                            actualizarMascotaEnBase(mascota);
                        }
                    }
                });
            } else {
                actualizarMascotaEnBase(mascota);
            }
        });

        listView.setAdapter(adapter);

        binding.botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String valorBuscar = binding.valorBuscarMascotas.getText().toString();

                if (valorBuscar.isEmpty())
                {
                    cargarDatos("sin_busqueda");
                }
                else
                {
                    cargarDatos(valorBuscar);
                }

            }
        });

    }

    private void cargarDatos(String busqueda) {


        String URL = "https://powderblue-lemur-595097.hostingersite.com/petmatch/listar_mascotas.php?busqueda=" + busqueda;

        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    listaMascotas.clear();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            MascotasModificar mascota = new MascotasModificar();

                            mascota.idMascota = obj.getInt("idMascota");
                            mascota.nombre = obj.getString("nombre");
                            mascota.raza = obj.getString("raza");
                            mascota.descripcion = obj.getString("descripcion");
                            mascota.edad = obj.getString("edad");
                            mascota.especie = obj.getString("especie");
                            mascota.genero = obj.getString("genero");
                            mascota.estado = obj.getString("estado");
                            mascota.rutaimagen = obj.getString("rutaImagen");
                            mascota.rutavideo = obj.getString("rutaVideo");
                            mascota.fechaingreso = obj.getString("fechaIngreso");
                            mascota.registradoPor = obj.getString("registradoPor");

                            listaMascotas.add(mascota);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (adapter == null) {
                        adapter = new MascotaAdapterModificar(requireContext(), listaMascotas, listaGenero, listaEspecie);
                        listView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }

                    if (listaMascotas.isEmpty()) {
                        Toast.makeText(getContext(), "No se encontraron resultados para la búsqueda.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Error al cargar mascotas", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);

    }

    public void subirArchivoAHostinger(Uri archivoUri, String urlServidor, String tipo, Consumer<String> callback) {
        new Thread(() -> {
            try {
                String nombreArchivo = tipo + "-" + System.currentTimeMillis() + (tipo.equals("video") ? ".mp4" : ".png");

                String tipoArchivo = requireContext().getContentResolver().getType(archivoUri);
                if (tipoArchivo == null) tipoArchivo = "application/octet-stream";

                String boundary = "------------------------" + System.currentTimeMillis();

                HttpURLConnection connection = (HttpURLConnection) new java.net.URL(urlServidor).openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                OutputStream output = connection.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"), true);

                writer.append("--").append(boundary).append("\r\n");
                writer.append("Content-Disposition: form-data; name=\"archivo\"; filename=\"").append(nombreArchivo).append("\"\r\n");
                writer.append("Content-Type: ").append(tipoArchivo).append("\r\n\r\n");
                writer.flush();

                InputStream input = requireContext().getContentResolver().openInputStream(archivoUri);
                byte[] buffer = new byte[65536];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                input.close();
                output.flush();
                writer.append("\r\n--").append(boundary).append("--\r\n");
                writer.flush();
                writer.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String respuestaServidor = reader.readLine().trim();
                reader.close();

                new Handler(Looper.getMainLooper()).post(() -> {
                    if (respuestaServidor.equals("ERROR_AL_SUBIR")) {
                        Toast.makeText(getContext(), "Error al subir " + tipo, Toast.LENGTH_SHORT).show();
                        callback.accept(null);
                    } else {
                        callback.accept(respuestaServidor); // Retorna el link
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(getContext(), "Error al subir " + tipo, Toast.LENGTH_SHORT).show()
                );
                callback.accept(null);
            }
        }).start();
    }

    private void actualizarMascotaEnBase(MascotasModificar mascota) {

        String url = "https://powderblue-lemur-595097.hostingersite.com/petmatch/actualizar_mascota.php";

        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.equalsIgnoreCase("actualizado")) {
                        Toast.makeText(getContext(), "Mascota actualizada correctamente", Toast.LENGTH_SHORT).show();
                        cargarDatos("sin_busqueda"); // vuelve a cargar la lista
                    } else {
                        Toast.makeText(getContext(), "Error al actualizar mascota", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idMascota", String.valueOf(mascota.idMascota));
                params.put("nombre", mascota.nombre);
                params.put("raza", mascota.raza);
                params.put("descripcion", mascota.descripcion);
                params.put("edad", mascota.edad);
                params.put("especie", mascota.especie);
                params.put("genero", mascota.genero);
                params.put("estado", mascota.estado);
                params.put("rutaImagen", mascota.rutaimagen);
                params.put("rutaVideo", mascota.rutavideo);
                params.put("fechaIngreso", mascota.fechaingreso);
                params.put("registradoPor", mascota.registradoPor);
                return params;
            }
        };

        queue.add(stringRequest);
    }

}