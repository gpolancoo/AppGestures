package com.example.appvideo;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.VideoView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class VideoFragment extends Fragment {
    private VideoView videoView; // El VideoView encapsula el MediaPlayer i el SurfaceView
    private ImageButton btnPlay, btnPause, btnReset;
    // Variables per controlar l'estat del vídeo quan girem la pantalla o sortim de l'app
    private int videoActual = 0;// Guarda el mil·lisegon actual
    private boolean Reproduint = false;// Indica si el vídeo estava en reproduinse

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inicia el layout
        View view = inflater.inflate(R.layout.video_fragment, container, false);

        // Enllaçem els objectes amb XML
        videoView = view.findViewById(R.id.video);
        btnPlay = view.findViewById(R.id.Play);
        btnPause = view.findViewById(R.id.Pause);
        btnReset = view.findViewById(R.id.Reset);

        // Si tenim un estat guardat (per exemple, després de girar la pantalla), recuperem les dades
        if (savedInstanceState != null) {
            videoActual = savedInstanceState.getInt("posicio");// Recuperem el segon actual
            Reproduint = savedInstanceState.getBoolean("reproduint"); // Sabem si s'estava reproduint
        }
        // Creem la URI per accedir al vídeo local en res/raw
        Uri videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.super_mario_galaxy);
        videoView.setVideoURI(videoUri);

        videoView.setOnPreparedListener(mp -> {
            if (videoActual > 0) {
                videoView.seekTo(videoActual);
            }
            if (Reproduint) {
                videoView.start();
                actualizarBotons(true);
            } else {
                actualizarBotons(false);
            }
            // Gestionem què passa quan el vídeo s'acaba
            mp.setOnCompletionListener(mediaPlayer -> {
                actualizarBotons(false);// Mostrem botó de Play
                videoActual = 0; // Reiniciem  el comptador on es guarda el temps deel video
            });
        });
        //indiquem que aquest boto es per iniciar el video
        btnPlay.setOnClickListener(v -> {
            videoView.start();
            actualizarBotons(true);
        });

        //indiquem que aquest boto es per pausar el video
        btnPause.setOnClickListener(v -> {
            videoView.pause();
            actualizarBotons(false);
        });

        //indiquem que aquest boto es per reiniciar el video i torna a carregar el vídeo des de l'inici
        btnReset.setOnClickListener(v -> {
            videoView.stopPlayback(); // Aturem completament el MediaPlayer
            String path = "android.resource://" + requireContext().getPackageName() + "/" + R.raw.super_mario_galaxy;
            videoView.setVideoPath(path); // Tornem a assignar la ruta
            videoView.start(); // Comença des de zero
            actualizarBotons(true);
        });
        return view;
    }

    // Aquest mètode és vital: s'executa si ens truquen o minimitzem l'app
    public void onPause() {
        super.onPause();
        if (videoView != null) {
            // Guardem l'estat actual abans que el fragment quedi en segon pla
            Reproduint = videoView.isPlaying();
            videoActual = videoView.getCurrentPosition();

            if (videoView.isPlaying()) {
                videoView.pause(); // Aturem el vídeo
                actualizarBotons(false);
            }
        }
    }
    // Es crida just abans de destruir el fragment per guardar l'estat (com al girar la pantalla)
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (videoView != null) {
            // Guardem les claus per recuperar-les al onCreateView
            outState.putInt("posicion", videoView.getCurrentPosition());
            outState.putBoolean("reproduciendo", videoView.isPlaying());
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
