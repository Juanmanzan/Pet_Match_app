package com.example.proyectoinvestigacion;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class InicioMascotasAdapter extends ArrayAdapter<inicioVistaMascota> {

    private Context context;
    private List<inicioVistaMascota> mascotaList;

    public InicioMascotasAdapter(Context context, List<inicioVistaMascota> mascotas) {
        super(context, 0, mascotas);
        this.context = context;
        this.mascotaList = mascotas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.iniciovista, parent, false);
        }


        inicioVistaMascota mascota = mascotaList.get(position);

        TextView nombre = convertView.findViewById(R.id.nombre_MascotaInicio);
        TextView raza = convertView.findViewById(R.id.raza_valor);
        TextView edad = convertView.findViewById(R.id.edad_valor);
        TextView genero = convertView.findViewById(R.id.genero_valor);
        ImageView imagen = convertView.findViewById(R.id.imagen_MascotaInicio);
        Button btnVermas = convertView.findViewById(R.id.btn_mas_detalles);


        nombre.setText(mascota.nombreMInicio);
        raza.setText("Raza: " + mascota.razaMInicio);
        edad.setText("Edad: " + mascota.edadMInicio);
        genero.setText("Género: " + mascota.generoMInicio);


        Glide.with(context)
                .load(mascota.rutaimagenMInicio)
                .into(imagen);

        btnVermas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detalles = new Intent(context, MainActivity6.class); // Usa 'context', no getContext()
                detalles.putExtra("idMascota", mascota.idMascota); // <-- Aquí envías la ID
                context.startActivity(detalles); // <- Usa context si estás en un adapter
            }
        });



        return convertView;
    }
}
