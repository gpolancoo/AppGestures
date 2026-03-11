package com.example.appvideo;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

// Implementem la interfície OnGesturePerformedListener per detectar dibuixos
public class VideoFragment extends Fragment implements GestureOverlayView.OnGesturePerformedListener {
    private VideoView videoView; // El VideoView encapsula el MediaPlayer i el SurfaceView
    private ImageButton btnPlay, btnPause, btnReset;//Botons per utilitzar el video
    private int videoActual = 0;// Guarda el mil·lisegon actual
    private boolean Reproduint = false;// Indica si el vídeo estava en reproduinse
    private GestureLibrary llibreria; // Aquesta variable guarda la "biblioteca" on estan guardats els gestos

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inicia el layout de video
        View view = inflater.inflate(R.layout.video_fragment, container, false);

        // Carreguem el fitxer de gestos que hem creat amb l'app Gesture Builder /raw
        llibreria = GestureLibraries.fromRawResource(getContext(), R.raw.gestures);

        // Si el fitxer no es carrega bé, tanquem l'activitat per evitar errors
        if (!llibreria.load()) {
            getActivity().finish();
        }

        // Busquem la "capa" que hem ficat al  XML on l'usuari dibuixa el gest
        GestureOverlayView gesturesView = view.findViewById(R.id.gestures_overlay);
        // Indiquem que aquest Fragment escoltara quan l'usuari acabi de fer el dibuix
        if (gesturesView != null) {
            gesturesView.addOnGesturePerformedListener(this);
        }

        // Enllaçem els objectes amb XML
        videoView = view.findViewById(R.id.video);
        btnPlay = view.findViewById(R.id.Play);
        btnPause = view.findViewById(R.id.Pause);
        btnReset = view.findViewById(R.id.Reset);

        // Si tenim un estat guardat (per exemple, després de girar la pantalla), recuperem les dades
        if (savedInstanceState != null) {
            videoActual = savedInstanceState.getInt("posicion");// Recuperem el segon actual
            Reproduint = savedInstanceState.getBoolean("reproduint"); // Sabem si s'estava reproduint
        }
        // Creem la URI per accedir al vídeo local en res/raw
        Uri videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.super_mario_galaxy);
        videoView.setVideoURI(videoUri);

        //Cuan el video ja esta llest per reproduirse
        videoView.setOnPreparedListener(mp -> {
            //Si tenim un temps guardat tornem alli
            if (videoActual > 0) {
                videoView.seekTo(videoActual);
            }
            //Comprovem si el video s'esta reproduint
            if (Reproduint) {

                videoView.start();
                actualizarBotons(true);
            } else {
                actualizarBotons(false);
            }
            // Gestionem que passa quan el vídeo s'acaba
            mp.setOnCompletionListener(mediaPlayer -> {
                actualizarBotons(false);// Mostrem botó de Play
                videoActual = 0; // Reiniciem  el comptador on es guarda el temps deel video
                Reproduint = false;
            });
        });
        //indiquem que aquest boto es per iniciar el video
        btnPlay.setOnClickListener(v -> {
            videoView.start();
            Reproduint = true;
            actualizarBotons(true);
        });

        //indiquem que aquest boto es per pausar el video
        btnPause.setOnClickListener(v -> {
            videoView.pause();
            Reproduint = false;
            actualizarBotons(false);
        });

        //indiquem que aquest boto es per reiniciar el video i torna a carregar el vídeo des de l'inici
        btnReset.setOnClickListener(v -> {
            videoView.seekTo(0);
            videoView.start();
            Reproduint = true;
            actualizarBotons(true);
        });
        return view;
    }

    // Funcio que s'activa quan l'usuari aixeca el dit de la pantalla
    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        //Fem una llista de totes les possibles coincidencies de el nostre gest
        ArrayList<Prediction> gestures = llibreria.recognize(gesture);

        // Si el sistema ha trobat alguna semblant
        if (gestures.size() > 0) {
            //Agafem la que atrobat
            Prediction millorSemblant = gestures.get(0);

            //Score es la semblança de la gestura si es 1.0 es que es molt semblant si es menos no tant
            if (millorSemblant.score > 1.0) {
                // Mirem el nom que li vam posar al gest a  Gesture Builder
                String nombreGesto = millorSemblant.name;

                // Segons el nom del gest, fem una acció o una altra
                if (nombreGesto.equals("Menu")) {
                    Toast.makeText(getContext(), "Tornant al Menú...", Toast.LENGTH_SHORT).show();
                    // Cridem la funcio de la MainActivity per canviar el Fragment
                    ((MainActivity) getActivity()).replaceFragment(new MenuFragment());

                } else if (nombreGesto.equals("Salir")) {
                    Toast.makeText(getContext(), "Tancant aplicació", Toast.LENGTH_SHORT).show();
                    // Tanquem l'app completament
                    getActivity().finish();
                }
            } else {
                // Si el dibuix és molt diferent i no arriba a 1.0 de score, avisem l'usuari
                Toast.makeText(getContext(), "Gestura no reconeixida, intentau de nou", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Aquest mètode  s'executa si ens truquen o minimitzem l'app
    public void onPause() {
        super.onPause();
        if (videoView != null) {
            // Guardem l'estat actual abans que el fragment quedi en segon pla
            videoActual = videoView.getCurrentPosition();
            Reproduint = videoView.isPlaying();
            videoView.pause();
            actualizarBotons(false);
        }
    }
    // Es crida just abans de destruir el fragment per guardar l'estat (com al girar la pantalla)
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (videoView != null) {
            // Guardem les claus per recuperar-les al onCreateView
            outState.putInt("posicion", videoView.getCurrentPosition());
            outState.putBoolean("reproducint", videoView.isPlaying());
        }
    }

    // funcio que gestiona l'animació i la visibilitat dels botons
    private void actualizarBotons(boolean isPlaying) {
        if (getContext() == null) return;

        // Carreguem les animacions XML de /anim
        Animation animEntrar = AnimationUtils.loadAnimation(getContext(), R.anim.apareixer);
        Animation animSortir = AnimationUtils.loadAnimation(getContext(), R.anim.desapareixer);

        if (isPlaying) {
            // Si el vídeo esta en marxa el boto play marxa i pausa entra
            if (btnPlay.getVisibility() == View.VISIBLE) {
                btnPlay.startAnimation(animSortir);
                btnPlay.setVisibility(View.GONE);
            }
            if (btnPause.getVisibility() != View.VISIBLE) {
                btnPause.setVisibility(View.VISIBLE);
                btnPause.startAnimation(animEntrar);
            }
        } else {
            // Si el vídeo para pausa marxa i play entra
            if (btnPause.getVisibility() == View.VISIBLE) {
                btnPause.startAnimation(animSortir);
                btnPause.setVisibility(View.GONE);
            }
            if (btnPlay.getVisibility() != View.VISIBLE) {
                btnPlay.setVisibility(View.VISIBLE);
                btnPlay.startAnimation(animEntrar);
            }
        }
    }
}