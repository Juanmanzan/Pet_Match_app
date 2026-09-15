package com.example.proyectoinvestigacion.vistas;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoinvestigacion.MainActivity2;
import com.example.proyectoinvestigacion.MainActivity3;
import com.example.proyectoinvestigacion.MainActivity8;
import com.example.proyectoinvestigacion.R;
import com.example.proyectoinvestigacion.URL;
import com.example.proyectoinvestigacion.databinding.FragmentMacotascrearBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import com.example.proyectoinvestigacion.R;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MacotascrearFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MacotascrearFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public FragmentMacotascrearBinding binding;
     Uri rutaimagen ; // permite guardar la ruta que de la imagen que el usuario ingreso
     Uri rutavideo;

    String Miurl = new URL().Miurl();

    String urlImagen;
    String urlVideo;
    TextView errorTextview;

    boolean imagenCargada = false;
    boolean videoCargado = false;


    int idUsuario;
    String nombreUsuario;
    String nombre;
    String apellido;
    String correo;
    String nivel;
    boolean isLoggedIn;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MacotascrearFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MacotascrearFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MacotascrearFragment newInstance(String param1, String param2) {
        MacotascrearFragment fragment = new MacotascrearFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private final ActivityResultLauncher<String> seleccionarImagenLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    rutaimagen = uri;
                    binding.imagenvistaid.setImageURI(uri);
                    binding.imagenvistaid.setVisibility(View.VISIBLE);
                }
            });

    private final ActivityResultLauncher<String> seleccionarVideoLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {

                    rutavideo = uri;

                    binding.videoViewid.setVideoURI(uri);
                    binding.videoViewid.setVisibility(View.VISIBLE);
                    android.widget.MediaController mediaController = new android.widget.MediaController(requireContext());
                    mediaController.setAnchorView(binding.videoViewid);
                    binding.videoViewid.setMediaController(mediaController);
                    binding.videoViewid.requestFocus();
                    binding.videoViewid.start();
                }
            });

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

        binding = FragmentMacotascrearBinding.inflate(inflater, container, false);
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


        // Accede al SharedPreferences para recuperar los datos

        SharedPreferences prefs = requireContext().getSharedPreferences("usuario_sesion", Context.MODE_PRIVATE);

        idUsuario = prefs.getInt("idUsuario", -1);
        nombreUsuario = prefs.getString("nombreUsuario", "");
        nombre = prefs.getString("nombre", "");
        apellido = prefs.getString("apellido", "");
        correo = prefs.getString("correo", "");
        nivel = prefs.getString("nivel", "");
        isLoggedIn = prefs.getBoolean("isLoggedIn", false);


        binding.usuarioBien.setText("Bienvenido: "+ nombre);

        binding.usuarioBien.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(requireContext(), v);
            popupMenu.getMenuInflater().inflate(R.menu.menu_usuario, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();


                if(id == R.id.nav_cerrarAplicacaion)
                {

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

                if (id == R.id.menu_cerrar_sesion) {

                    AlertDialog.Builder alerta1 = new AlertDialog.Builder(getContext());
                    alerta1.setIcon(R.drawable.update);
                    alerta1.setTitle("Aviso");
                    alerta1.setMessage("Desea salir de cesión");
                    alerta1.setCancelable(false);
                    alerta1.setNegativeButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.clear();
                            editor.apply();
                            eliminarCacheRecursivamente(requireContext().getCacheDir());
                            Intent intent = new Intent(requireContext(), MainActivity2.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
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

                return false;
            });

            popupMenu.show();
        });

        binding.modificaridbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nivel.equals("operador"))
                {
                    Intent cambioModificar = new Intent(getContext(), MainActivity8.class);
                    cambioModificar.putExtra("modo", "modificar");
                    startActivity(cambioModificar);
                }
                else {

                    Intent cambioModificar = new Intent(getContext(), MainActivity3.class);
                    cambioModificar.putExtra("modo", "modificar");
                    startActivity(cambioModificar);
                }
            }
        });

        binding.botonimagenid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarImagenLauncher.launch("image/*");
            }
        });

        binding.botonvideoid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarVideoLauncher.launch("video/*");
            }
        });

        listarespecies(Miurl + "listar_especie.php");

        String sexo [] = {"Seleccione el sexo","Macho", "Hembra"};
        ArrayAdapter<String> adaptersexo = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sexo );
        binding.spinnerGenero.setAdapter(adaptersexo);

        binding.fechaingresoid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int anio = cal.get(Calendar.YEAR);
                int mes = cal.get(Calendar.MONTH);
                int dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        month = month+1;
                        String fecha = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);

                        binding.vistaingresoid.setText(fecha);
                    }

                },anio,mes,dia);

                dpd.show();


            }
        });

        binding.botoncrearmascotaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (rutaimagen == null || rutavideo == null || binding.nombre.getText().toString().isEmpty()
                        || binding.spinnerGenero.getSelectedItemId() == 0 || binding.spinnerEspecie.getSelectedItemId() == 0
                            || binding.razaid.getText().toString().isEmpty() || binding.editTextNumber.getText().toString().isEmpty()
                                || binding.descripcionid.getText().toString().isEmpty() || binding.estadoid.getText().toString().isEmpty()
                                    || binding.vistaingresoid.getText().toString().isEmpty())
                {

                    if(rutavideo == null)
                    {
                        Toast.makeText(getContext(), "Falta seleccionar un video", Toast.LENGTH_SHORT).show();
                    }
                    if (rutaimagen == null)
                    {
                        Toast.makeText(getContext(), "Falta seleccionar una imagen", Toast.LENGTH_SHORT).show();
                    }
                    if (binding.nombre.getText().toString().isEmpty()){ binding.nombre.setError("Obligatorio");}
                    if (binding.razaid.getText().toString().isEmpty()){ binding.razaid.setError("Obligatorio");}
                    if (binding.editTextNumber.getText().toString().isEmpty()){ binding.editTextNumber.setError("Obligatorio");}
                    if (binding.estadoid.getText().toString().isEmpty()){ binding.estadoid.setError("Obligatorio");}
                    if (binding.descripcionid.getText().toString().isEmpty()){ binding.descripcionid.setError("Obligatorio");}
                    if (binding.spinnerEspecie.getSelectedItemId() == 0) { errorTextview=(TextView) binding.spinnerEspecie.getSelectedView(); errorTextview.setError("obligatorio");}
                    if (binding.spinnerGenero.getSelectedItemId() == 0) { errorTextview=(TextView) binding.spinnerGenero.getSelectedView(); errorTextview.setError("obligatorio");}
                    if(binding.vistaingresoid.getText().toString().isEmpty()){ binding.vistaingresoid.setError("Obligatorio");}


                }
                else
                {
                    binding.botoncrearmascotaid.setEnabled(false);
                    cargarimagenshostinger(rutaimagen, Miurl+"subir_imagen.php");
                    cargarvideosshostinger(rutavideo, Miurl+"subir_video.php");


                }


            }
        });

    }

    public void listarespecies(String url) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        ArrayList<String> especiesList = new ArrayList<>();
                        especiesList.add("Seleccionar especie"); // <-- Agregado al principio

                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject especie = response.getJSONObject(i);
                                String nombreEspecie = especie.getString("nombre");
                                especiesList.add(nombreEspecie); // Agregamos el nombre a la lista
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, especiesList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            binding.spinnerEspecie.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    error.printStackTrace();
                });

        Volley.newRequestQueue(getContext()).add(request);
    }

    public void cargarimagenshostinger(Uri imagen, String url) {
        new Thread(() -> {
            try {

                String nombreArchivo = "imagen-"+System.currentTimeMillis()+".png";

                String tipoArchivo = requireContext().getContentResolver().getType(imagen); // a partir de la URI devuele el tipo de archivo
                if (tipoArchivo == null) tipoArchivo = "image/png"; // comprueba si se extrajo el tipo de archivo

                String limite = "======================="; // permite limitar cada parte del mensaje HTTP
                HttpURLConnection conexion = (HttpURLConnection) new java.net.URL(url).openConnection(); // establece una conexion HTTP hacia el servidor
                // en hostinger

                conexion.setDoOutput(true); // habre el canal de salida de datos para enviarlos
                conexion.setRequestMethod("POST"); // que metodo voy a emplear, Post para el envio de datos.

                // crea un mensaje con donde definimos que tipo de contenido se va a enviar, que el mensaje va a estar dividio
                // en partes y que cada parte del mensaje va a tener un comienzo y fin
                conexion.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + limite);

                // se crea una variable que representa el canal de conexion hacia el servidor
                OutputStream output = conexion.getOutputStream();

                // Crea un traductor de texto a bytes con codificación UTF-8
                //  Esto para enviar la informacion que queremos hacia al servidor a través del canal de salida (OutputStream)
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"), true);


                // construccion de los mensaje para construir la iamgen

                // escribe el siguiente mensaje al servidor -- + el limitador el mensaje y un salto de linea, es decir comienza una
                // parte del mesaje
                writer.append("--").append(limite).append("\r\n");
                // escribe el siguiente mensaje al servidor en los encabezados se va a enviar un campo llamado "archivo" y un
                // campo llamado filename donde su valor es + el nombre obtenido anteriormente de la URI de la iamgen
                writer.append("Content-Disposition: form-data; name=\"archivo\"; filename=\"").append(nombreArchivo).append("\"\r\n");
                // escribe el siguiente mensaje al servidor el valor del campo archivos es el tipo de archivo que es la imagen ya sea png, jpg
                writer.append("Content-Type: ").append(tipoArchivo).append("\r\n\r\n");
                // envia esta parte del mensaje inmediatamente por el canal de salida (OutputStream)
                writer.flush();
                // traducido quedaria:
                // voy a enviar un campo llamado archivo donde su valor es por jemplo imagen/jpg y otro campo
                // llamado filename con el valor del nombre del archivo obtenido de la URI


                // Envío de imagen

                // la imagen selecciona por el usuario conviertale en bits y guardalas en input
                InputStream input = requireContext().getContentResolver().openInputStream(imagen);
                // crea un caja para guardar 4096 bits de la imagen
                byte[] buffer = new byte[8192];
                // cuantos bits fueron leidos
                int bytesRead;
                // mientras el numero de bits leidos sea igual a 4096 y mientras
                // el archaivo de buffer sea diferente de -1
                // el archaivo de buffer se pone automaticamente en -1 cuando ya no tiene bits
                while ((bytesRead = input.read(buffer)) != -1) {

                    // escribimos en el canal abierto los 4096 bits de la foto
                    // y asi sucesivamente hasta llegar a -1 y que el ciclo se acabe
                    output.write(buffer, 0, bytesRead);
                }
                // cerramos la conexion entre el input y la iamgen seleccionada
                input.close();
                // Envia al servidor datos mediante la instancia del canal de conexion output de forma inmediata
                output.flush();

                // Cierre del mensaje

                // escribe al servidor  -- salta una linea para avisar que ya acabe de escribir esa parte
                // y salta dos lineas para avisar que ya se acabo el encabezado
                writer.append("\r\n--").append(limite).append("--\r\n");
                // envia todo lo escrito al servidor
                writer.flush();
                // cerramos el interprete
                writer.close();

                // En si que se hace

                // abrimos una conexion hacia el archivo subir_archivos.php donde envias parte por parte un mensaje HTTP
                // la primera parte define el nombre y extension del archivo
                // la tercera parte trasfiere los bits de la foto hacia el servidor

                //resouesta servidor
                BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String respuestaServidor = reader.readLine().trim(); // solo lee una línea
                reader.close();

                new Handler(Looper.getMainLooper()).post(() -> {
                    if (respuestaServidor.trim().equals("ERROR_AL_SUBIR")) {
                        Toast.makeText(getContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Imagen subida con éxito", Toast.LENGTH_SHORT).show();
                        urlImagen = respuestaServidor;
                        imagenCargada = true;
                        verificarSiAmbosListos();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(getContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    public void cargarvideosshostinger(Uri imagen, String url) {
        new Thread(() -> {
            try {

                String nombreArchivo = "video-"+System.currentTimeMillis()+".mp4";

                String tipoArchivo = requireContext().getContentResolver().getType(imagen);
                if (tipoArchivo == null) tipoArchivo = "image/png";
                String limite = "=======================";
                HttpURLConnection conexion = (HttpURLConnection) new java.net.URL(url).openConnection();
                conexion.setDoOutput(true);
                conexion.setRequestMethod("POST");
                conexion.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + limite);
                OutputStream output = conexion.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"), true);
                writer.append("--").append(limite).append("\r\n");
                writer.append("Content-Disposition: form-data; name=\"archivo\"; filename=\"").append(nombreArchivo).append("\"\r\n");
                writer.append("Content-Type: ").append(tipoArchivo).append("\r\n\r\n");
                writer.flush();
                InputStream input = requireContext().getContentResolver().openInputStream(imagen);
                byte[] buffer = new byte[65536];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                input.close();
                output.flush();
                writer.append("\r\n--").append(limite).append("--\r\n");
                writer.flush();
                writer.close();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String respuestaServidor = reader.readLine().trim(); // solo lee una línea
                reader.close();
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (respuestaServidor.trim().equals("ERROR_AL_SUBIR")) {
                        Toast.makeText(getContext(), "Error al subir el video", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Video subido con éxito", Toast.LENGTH_SHORT).show();
                        urlVideo = respuestaServidor;
                        videoCargado = true;
                        verificarSiAmbosListos();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(getContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    public void verificarSiAmbosListos() {

        if (imagenCargada && videoCargado && urlImagen != null && urlVideo != null) {
            insertarMascota(Miurl+"insertar_mascota.php");
        }
    }
    public void insertarMascota (String URL)
    {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getContext(), "Mascota ingresada", Toast.LENGTH_SHORT).show();
                binding.botoncrearmascotaid.setEnabled(true);

                // limpiar cambios

                binding.nombre.setText("");
                binding.descripcionid.setText("");
                binding.estadoid.setText("");
                binding.vistaingresoid.setText("");
                binding.editTextNumber.setText("");
                binding.spinnerGenero.setSelection(0);
                binding.spinnerEspecie.setSelection(0);
                binding.razaid.setText("");

                rutaimagen = null;
                rutavideo = null;

                urlImagen = "";
                urlVideo = "";

                binding.videoViewid.setVisibility(View.GONE);
                binding.imagenvistaid.setVisibility(View.GONE);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "error"+error, Toast.LENGTH_SHORT).show();

            }
        }){

            // mapeo mandar las variables al web services

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String > parameters = new HashMap<String, String>();
                parameters.put("nombre",binding.nombre.getText().toString());
                parameters.put("especie", binding.spinnerEspecie.getSelectedItem().toString());
                parameters.put("genero", binding.spinnerGenero.getSelectedItem().toString());
                parameters.put("raza",binding.razaid.getText().toString());
                parameters.put("edad", binding.editTextNumber.getText().toString());
                parameters.put("descripcion", binding.descripcionid.getText().toString());
                parameters.put("estado", binding.estadoid.getText().toString());
                parameters.put("link_video", urlVideo);
                parameters.put("link_imagen", urlImagen);
                parameters.put("fecha", binding.vistaingresoid.getText().toString());
                parameters.put("ingresadopor", nombre + " " + apellido);

                return parameters;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }


    private void eliminarCacheRecursivamente(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    eliminarCacheRecursivamente(new File(dir, child));
                }
            }
        }
        dir.delete();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}