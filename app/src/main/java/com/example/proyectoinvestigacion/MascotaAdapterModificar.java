package com.example.proyectoinvestigacion;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MascotaAdapterModificar extends ArrayAdapter<MascotasModificar> {

    private Context context;
    private List<MascotasModificar> mascotaList;
    private List<String> listaGenero;
    private List<String> listaEspecie;
    private String Miurl = new URL().Miurl();


    static class ViewHolder {
        EditText nombre, raza, descripcion, estado, edad, fecha;
        Spinner especie, genero;
        WebView imagen, video;
        Button modificar, eliminar, btnCargarImagen, btnCargarVideo, btnGuardar, btnFecha;

        TextWatcher nombreWatcher, razaWatcher, edadWatcher, descripcionWatcher, estadoWatcher, fechaWatcher;

        boolean isUpdatingSpinners = false;
    }

    public MascotaAdapterModificar(Context context, List<MascotasModificar> list,
                                   List<String> listaGenero, List<String> listaEspecie) {
        super(context, 0, list);
        this.context = context;
        this.mascotaList = list;
        this.listaGenero = listaGenero;
        this.listaEspecie = listaEspecie;
    }


    public interface OnCargarImagenClickListener {
        void onCargarImagenClick(int position);
    }

    public interface OnCargarVideoClickListener {
        void onCargarVideoClick(int position);
    }

    public interface OnGuardarClickListener {
        void onGuardarClick(int position, MascotasModificar mascota);
    }

    private OnCargarImagenClickListener cargarImagenListener;
    private OnCargarVideoClickListener cargarVideoListener;
    private OnGuardarClickListener guardarClickListener;

    public void setOnCargarImagenClickListener(OnCargarImagenClickListener listener) {
        this.cargarImagenListener = listener;
    }

    public void setOnCargarVideoClickListener(OnCargarVideoClickListener listener) {
        this.cargarVideoListener = listener;
    }

    public void setOnGuardarClickListener(OnGuardarClickListener listener) {
        this.guardarClickListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.modificarvista, parent, false);
            holder = new ViewHolder();

            // Inicializar vistas
            holder.nombre = convertView.findViewById(R.id.editTextText2);
            holder.raza = convertView.findViewById(R.id.editTextNumber2);
            holder.especie = convertView.findViewById(R.id.spinner);
            holder.genero = convertView.findViewById(R.id.spinner2);
            holder.descripcion = convertView.findViewById(R.id.editTextText3);
            holder.estado = convertView.findViewById(R.id.editTextText4);
            holder.edad = convertView.findViewById(R.id.editTextText5);
            holder.fecha = convertView.findViewById(R.id.editTextText6);
            holder.imagen = convertView.findViewById(R.id.webView2);
            holder.video = convertView.findViewById(R.id.webView);
            holder.modificar = convertView.findViewById(R.id.button11);
            holder.eliminar = convertView.findViewById(R.id.button12);
            holder.btnCargarImagen = convertView.findViewById(R.id.button9);
            holder.btnCargarVideo = convertView.findViewById(R.id.button6);
            holder.btnGuardar = convertView.findViewById(R.id.button10);
            holder.btnFecha = convertView.findViewById(R.id.button7);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MascotasModificar mascota = mascotaList.get(position);

        removeTextWatchers(holder);
        holder.nombre.setText(mascota.nombre);
        holder.raza.setText(mascota.raza);
        holder.edad.setText(mascota.edad);
        holder.descripcion.setText(mascota.descripcion);
        holder.estado.setText(mascota.estado);
        holder.fecha.setText(mascota.fechaingreso);


        setupSpinners(holder, mascota);
        setupMediaContent(holder, mascota);
        setupFieldsVisibilityAndState(holder, mascota);
        setupTextWatchers(holder, mascota, position);
        setupButtonListeners(holder, mascota, position);

        return convertView;
    }

    private void removeTextWatchers(ViewHolder holder) {
        if (holder.nombreWatcher != null) {
            holder.nombre.removeTextChangedListener(holder.nombreWatcher);
        }
        if (holder.razaWatcher != null) {
            holder.raza.removeTextChangedListener(holder.razaWatcher);
        }
        if (holder.edadWatcher != null) {
            holder.edad.removeTextChangedListener(holder.edadWatcher);
        }
        if (holder.descripcionWatcher != null) {
            holder.descripcion.removeTextChangedListener(holder.descripcionWatcher);
        }
        if (holder.estadoWatcher != null) {
            holder.estado.removeTextChangedListener(holder.estadoWatcher);
        }
        if (holder.fechaWatcher != null) {
            holder.fecha.removeTextChangedListener(holder.fechaWatcher);
        }
    }

    private void setupSpinners(ViewHolder holder, MascotasModificar mascota) {
        holder.isUpdatingSpinners = true;


        ArrayAdapter<String> especieAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, listaEspecie);
        especieAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.especie.setAdapter(especieAdapter);


        ArrayAdapter<String> generoAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, listaGenero);
        generoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.genero.setAdapter(generoAdapter);


        int indexGenero = listaGenero.indexOf(mascota.genero);
        int indexEspecie = listaEspecie.indexOf(mascota.especie);

        if (indexGenero >= 0) {
            holder.genero.setSelection(indexGenero);
        }
        if (indexEspecie >= 0) {
            holder.especie.setSelection(indexEspecie);
        }

        holder.especie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!holder.isUpdatingSpinners && position > 0) {
                    mascota.especie = listaEspecie.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        holder.genero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!holder.isUpdatingSpinners && position > 0) {
                    mascota.genero = listaGenero.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        holder.isUpdatingSpinners = false;
    }

    private void setupMediaContent(ViewHolder holder, MascotasModificar mascota) {

        if (mascota.expandido && mascota.rutavideo != null && !mascota.rutavideo.isEmpty()) {
            WebSettings videoSettings = holder.video.getSettings();
            videoSettings.setJavaScriptEnabled(true);
            videoSettings.setDomStorageEnabled(true);
            holder.video.loadUrl(mascota.rutavideo);
        }

        if (mascota.uriImagen != null) {
            loadImageInWebView(holder.imagen, mascota.uriImagen.toString());
        } else if (mascota.rutaimagen != null && !mascota.rutaimagen.isEmpty()) {
            loadImageInWebView(holder.imagen, mascota.rutaimagen);
        }
    }

    private void loadImageInWebView(WebView webView, String imageUrl) {
        String htmlImagen = "<!DOCTYPE html>" +
                "<html><head><meta name='viewport' content='width=device-width, height=device-height, initial-scale=1.0'>" +
                "<style>" +
                "body, html { margin:0; padding:0; height:100%; width:100%; background:#f0f0f0; }" +
                "img { width:100%; height:100%; object-fit:contain; display:block; }" +
                "</style>" +
                "</head><body>" +
                "<img src='" + imageUrl + "' onerror='this.style.display=\"none\"; this.parentNode.innerHTML=\"<div style=\"text-align:center; padding:20px; color:#666;\">Error cargando imagen</div>\";' />" +
                "</body></html>";

        WebSettings settings = webView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(false);
        webView.loadDataWithBaseURL(null, htmlImagen, "text/html", "UTF-8", null);
    }

    private void setupFieldsVisibilityAndState(ViewHolder holder, MascotasModificar mascota) {
        int visibility = mascota.expandido ? View.VISIBLE : View.GONE;

        holder.descripcion.setVisibility(visibility);
        holder.estado.setVisibility(visibility);
        holder.fecha.setVisibility(visibility);
        holder.imagen.setVisibility(visibility);
        holder.video.setVisibility(visibility);
        holder.btnCargarImagen.setVisibility(visibility);
        holder.btnCargarVideo.setVisibility(visibility);
        holder.btnGuardar.setVisibility(visibility);

        boolean habilitar = mascota.expandido;
        holder.nombre.setEnabled(habilitar);
        holder.raza.setEnabled(habilitar);
        holder.edad.setEnabled(habilitar);
        holder.descripcion.setEnabled(habilitar);
        holder.estado.setEnabled(habilitar);
        holder.fecha.setEnabled(false);
        holder.btnCargarImagen.setEnabled(habilitar);
        holder.btnCargarVideo.setEnabled(habilitar);
        holder.btnGuardar.setEnabled(habilitar);
        holder.especie.setEnabled(habilitar);
        holder.genero.setEnabled(habilitar);
    }

    private void setupTextWatchers(ViewHolder holder, MascotasModificar mascota, int position) {
        holder.nombreWatcher = new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                mascota.nombre = s.toString();
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        };
        holder.nombre.addTextChangedListener(holder.nombreWatcher);

        holder.razaWatcher = new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                mascota.raza = s.toString();
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        };
        holder.raza.addTextChangedListener(holder.razaWatcher);

        holder.edadWatcher = new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                mascota.edad = s.toString();
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        };
        holder.edad.addTextChangedListener(holder.edadWatcher);

        holder.descripcionWatcher = new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                mascota.descripcion = s.toString();
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        };
        holder.descripcion.addTextChangedListener(holder.descripcionWatcher);

        holder.estadoWatcher = new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                mascota.estado = s.toString();
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        };
        holder.estado.addTextChangedListener(holder.estadoWatcher);

        holder.fechaWatcher = new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                mascota.fechaingreso = s.toString();
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        };
        holder.fecha.addTextChangedListener(holder.fechaWatcher);
    }

    private void setupButtonListeners(ViewHolder holder, MascotasModificar mascota, int position) {
        holder.modificar.setOnClickListener(v -> {
            mascota.expandido = !mascota.expandido;
            notifyDataSetChanged();
        });

        holder.eliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Estás seguro de que deseas eliminar esta mascota?")
                    .setPositiveButton("Sí, eliminar", (dialog, which) -> {
                        eliminarMascota(mascota.idMascota, Miurl + "eliminar_mascota.php", position);
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        holder.btnCargarImagen.setOnClickListener(v -> {
            if (cargarImagenListener != null) {
                cargarImagenListener.onCargarImagenClick(position);
            }
        });

        holder.btnCargarVideo.setOnClickListener(v -> {
            if (cargarVideoListener != null) {
                cargarVideoListener.onCargarVideoClick(position);
            }
        });

        holder.btnFecha.setOnClickListener(v -> {
            showDatePicker(holder.fecha);
        });

        holder.btnGuardar.setOnClickListener(v -> {
            if (validateAndSave(holder, mascota, position)) {
                if (guardarClickListener != null) {
                    guardarClickListener.onGuardarClick(position, mascota);
                }
            }
        });
    }

    private void showDatePicker(EditText fechaField) {
        String fechaActual = fechaField.getText().toString();
        int año = 2024, mes = 0, dia = 1;

        if (!fechaActual.isEmpty() && fechaActual.matches("\\d{4}-\\d{2}-\\d{2}")) {
            String[] partes = fechaActual.split("-");
            try {
                año = Integer.parseInt(partes[0]);
                mes = Integer.parseInt(partes[1]) - 1;
                dia = Integer.parseInt(partes[2]);
            } catch (NumberFormatException e) {
            }
        }

        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(context,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String fechaSeleccionada = year + "-" +
                            String.format("%02d", (monthOfYear + 1)) + "-" +
                            String.format("%02d", dayOfMonth);
                    fechaField.setText(fechaSeleccionada);
                }, año, mes, dia);

        datePickerDialog.show();
    }

    private boolean validateAndSave(ViewHolder holder, MascotasModificar mascota, int position) {
        boolean isValid = true;

        holder.nombre.setError(null);
        holder.raza.setError(null);
        holder.edad.setError(null);
        holder.descripcion.setError(null);
        holder.estado.setError(null);
        holder.fecha.setError(null);

        if (holder.nombre.getText().toString().trim().isEmpty()) {
            holder.nombre.setError("Obligatorio");
            isValid = false;
        }

        if (holder.raza.getText().toString().trim().isEmpty()) {
            holder.raza.setError("Obligatorio");
            isValid = false;
        }

        if (holder.edad.getText().toString().trim().isEmpty()) {
            holder.edad.setError("Obligatorio");
            isValid = false;
        }

        if (holder.descripcion.getText().toString().trim().isEmpty()) {
            holder.descripcion.setError("Obligatorio");
            isValid = false;
        }

        if (holder.estado.getText().toString().trim().isEmpty()) {
            holder.estado.setError("Obligatorio");
            isValid = false;
        }

        if (holder.fecha.getText().toString().trim().isEmpty()) {
            holder.fecha.setError("Obligatorio");
            isValid = false;
        }

        if (holder.especie.getSelectedItemPosition() == 0) {
            TextView errorTextView = (TextView) holder.especie.getSelectedView();
            if (errorTextView != null) {
                errorTextView.setError("Obligatorio");
            }
            isValid = false;
        }

        if (holder.genero.getSelectedItemPosition() == 0) {
            TextView errorTextView = (TextView) holder.genero.getSelectedView();
            if (errorTextView != null) {
                errorTextView.setError("Obligatorio");
            }
            isValid = false;
        }

        if (mascota.uriImagen == null && (mascota.rutaimagen == null || mascota.rutaimagen.isEmpty())) {
            Toast.makeText(context, "Falta seleccionar una imagen", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (mascota.rutavideo == null || mascota.rutavideo.isEmpty()) {
            Toast.makeText(context, "Falta seleccionar un video", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (isValid) {
            mascota.nombre = holder.nombre.getText().toString().trim();
            mascota.raza = holder.raza.getText().toString().trim();
            mascota.edad = holder.edad.getText().toString().trim();
            mascota.descripcion = holder.descripcion.getText().toString().trim();
            mascota.estado = holder.estado.getText().toString().trim();
            mascota.fechaingreso = holder.fecha.getText().toString().trim();
            mascota.genero = holder.genero.getSelectedItem().toString();
            mascota.especie = holder.especie.getSelectedItem().toString();
        }

        return isValid;
    }

    @Override
    public int getCount() {
        return mascotaList != null ? mascotaList.size() : 0;
    }

    private void eliminarMascota(int idMascota, String URL, int posicion) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    Toast.makeText(getContext(), "Mascota eliminada exitosamente", Toast.LENGTH_SHORT).show();
                    if (posicion < mascotaList.size()) {
                        mascotaList.remove(posicion);
                        notifyDataSetChanged();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Error al eliminar la mascota: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idMascota", String.valueOf(idMascota));
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }
}