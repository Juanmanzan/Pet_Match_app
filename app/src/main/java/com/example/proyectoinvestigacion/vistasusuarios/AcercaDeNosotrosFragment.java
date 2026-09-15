package com.example.proyectoinvestigacion.vistasusuarios;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.proyectoinvestigacion.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AcercaDeNosotrosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AcercaDeNosotrosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AcercaDeNosotrosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AcercaDeNosotrosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AcercaDeNosotrosFragment newInstance(String param1, String param2) {
        AcercaDeNosotrosFragment fragment = new AcercaDeNosotrosFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_acerca_de_nosotros, container, false);
    }
}