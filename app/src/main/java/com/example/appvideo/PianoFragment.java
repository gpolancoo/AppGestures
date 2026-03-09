package com.example.appvideo;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

public class PianoFragment extends Fragment {

    private SoundPool soundPool;
    // Creem variables per guardar els ID dels sonscarregats al SoundPool
    private int doSound, reSound, miSound, faSound, solSound, laSound, siSound, do2Sound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.piano_fragment, container, false);

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