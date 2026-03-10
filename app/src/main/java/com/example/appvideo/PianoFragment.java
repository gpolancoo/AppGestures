package com.example.appvideo;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class PianoFragment extends Fragment implements GestureOverlayView.OnGesturePerformedListener {

    private SoundPool soundPool;
    // Creem variables per guardar els ID dels sonscarregats al SoundPool
    private GestureLibrary llibreria;// Aquesta variable guarda la "biblioteca" on estan guardats els gestos
    private int doSound, reSound, miSound, faSound, solSound, laSound, siSound, do2Sound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.piano_fragment, container, false);

        // Carreguem el fitxer de gestos que hem creat amb l'app Gesture Builder /raw)
        llibreria = GestureLibraries.fromRawResource(getContext(), R.raw.gestures);

        // Si el fitxer no es carrega bé, tanquem l'activitat per evitar errors
        if (!llibreria.load()) {
            getActivity().finish();
        }

        // Busquem la "capa invisible" del XML on l'usuari dibuixa el gest
        GestureOverlayView gesturesView = view.findViewById(R.id.gestures_overlay);
        // Indiquem que aquest Fragment escoltara quan l'usuari acabi de fer el dibuix
        gesturesView.addOnGesturePerformedListener(this);

        // Crear SoundPool amb atributs d'àudio per reproduir música
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        // Inicialitzem el SoundPool amb un màxim de 8 sons simultanis
        soundPool = new SoundPool.Builder()
                .setMaxStreams(8)
                .setAudioAttributes(audioAttributes)
                .build();

        // Carreguem els sons des de res/raw
        doSound = soundPool.load(getContext(), R.raw.do1, 1);
        reSound = soundPool.load(getContext(), R.raw.re, 1);
        miSound = soundPool.load(getContext(), R.raw.mi, 1);
        faSound = soundPool.load(getContext(), R.raw.fa, 1);
        solSound = soundPool.load(getContext(), R.raw.sol, 1);
        laSound = soundPool.load(getContext(), R.raw.la, 1);
        siSound = soundPool.load(getContext(), R.raw.si, 1);
        do2Sound = soundPool.load(getContext(), R.raw.do2, 1);

        // Connectem els botons del piano amb els sons que tenim establerts
        view.findViewById(R.id.botonDo).setOnClickListener(v -> soundPool.play(doSound, 1, 1, 0, 0, 1));
        view.findViewById(R.id.botonRe).setOnClickListener(v -> soundPool.play(reSound, 1, 1, 0, 0, 1));
        view.findViewById(R.id.botonMi).setOnClickListener(v -> soundPool.play(miSound, 1, 1, 0, 0, 1));
        view.findViewById(R.id.botonFa).setOnClickListener(v -> soundPool.play(faSound, 1, 1, 0, 0, 1));
        view.findViewById(R.id.botonSol).setOnClickListener(v -> soundPool.play(solSound, 1, 1, 0, 0, 1));
        view.findViewById(R.id.botonLa).setOnClickListener(v -> soundPool.play(laSound, 1, 1, 0, 0, 1));
        view.findViewById(R.id.botonSi).setOnClickListener(v -> soundPool.play(siSound, 1, 1, 0, 0, 1));
        view.findViewById(R.id.botonDo2).setOnClickListener(v -> soundPool.play(do2Sound, 1, 1, 0, 0, 1));

        return view;
    }
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> gestures = llibreria.recognize(gesture);

        if (gestures.size() > 0) {
            Prediction millorSemblant = gestures.get(0);

            if (millorSemblant.score > 1.0) {
                String nombreGesto = millorSemblant.name;

               if (nombreGesto.equals("Menu")) {
                    // Cambia "Menu" por el nombre exacto que pusiste en Gesture Builder
                    Toast.makeText(getContext(), "Tornant al Menú...", Toast.LENGTH_SHORT).show();
                    ((MainActivity) getActivity()).replaceFragment(new MenuFragment());

                } else if (nombreGesto.equals("Salir")) {
                    Toast.makeText(getContext(), "Tancant aplicació", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            } else {
                Toast.makeText(getContext(), "Gest no reconegut", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        // Quan el fragment es destrueix, es comprova que el SoundPool no sigui null i es alliberen els recursos associats
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}