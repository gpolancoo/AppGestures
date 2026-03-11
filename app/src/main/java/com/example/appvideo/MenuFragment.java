package com.example.appvideo;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class MenuFragment extends Fragment implements GestureOverlayView.OnGesturePerformedListener {

    private GestureLibrary llibreria;// Aquesta variable guarda la "biblioteca" on estan guardats els gestos

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Carrega el layout del menú (on hi ha el GestureOverlayView)
        View view = inflater.inflate(R.layout.menu_fragment, container, false);

        // Carreguem el fitxer de gestos que hem creat amb l'app Gesture Builder /raw
        llibreria = GestureLibraries.fromRawResource(getContext(), R.raw.gestures);

        // Si el fitxer no es carrega bé, tanquem l'activitat per evitar errors
        if (!llibreria.load()) {
            getActivity().finish();
        }

        // Busquem la "capa" que hem ficat al  XML on l'usuari dibuixa el gest
        GestureOverlayView gesturesView = view.findViewById(R.id.gestures_overlay);
        // Indiquem que aquest Fragment escoltara quan l'usuari acabi de fer el dibuix
        gesturesView.addOnGesturePerformedListener(this);

        return view;
    }

    // Funcio que s'activa quan l'usuari aixeca el dit de la pantalla
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        //Fem una llista de totes les possibles coincidencies de el nostre gest
        ArrayList<Prediction> gestures = llibreria.recognize(gesture);

        // Si el sistema ha trobat alguna semblant
        if (gestures.size() > 0) {
           //Agafem la que atrobat
            Prediction millorSemblant = gestures.get(0);

           //Score es la semblança de la gestura si es 1.0 es que es molt semblant si es menos no tant
            if (millorSemblant.score >= 1.0) {

                // Mirem el nom que li vam posar al gest a  Gesture Builder
                String nombreGesto = millorSemblant.name;

                if (nombreGesto.equals("reproductorVideo")) {
                    Toast.makeText(getContext(), "Obrint Reproductor...", Toast.LENGTH_SHORT).show();
                    // Cridem la funcio de la MainActivity per canviar el Fragment
                    ((MainActivity) getActivity()).replaceFragment(new VideoFragment());

                } else if (nombreGesto.equals("Piano")) {
                    Toast.makeText(getContext(), "Obrint Piano...", Toast.LENGTH_SHORT).show();
                    ((MainActivity) getActivity()).replaceFragment(new PianoFragment());

                } else if (nombreGesto.equals("Salir")) {
                    Toast.makeText(getContext(), "Tancant aplicación", Toast.LENGTH_SHORT).show();
                    // Tanquem l'app completament
                    getActivity().finish();
                }
            } else {
                // Si el dibuix és molt diferent i no arriba a 1.0 de score, avisem l'usuari
                Toast.makeText(getContext(), "Gestura no reconeixida, intentau de nou", Toast.LENGTH_SHORT).show();
            }
        }
    }
}