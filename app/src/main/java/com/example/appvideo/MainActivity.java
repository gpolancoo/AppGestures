package com.example.appvideo;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.VideoView;
import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView;
    private ImageButton btnPlay, btnPause, btnReset;
    private MediaPlayer mPlayer;
    private int videoActual = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.myVideoView);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnReset = findViewById(R.id.btnReset);

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.super_mario_galaxy);
        videoView.setVideoURI(videoUri);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPlayer = mp;
                if (videoActual > 0) {
                    videoView.seekTo(videoActual);
                    videoView.start();
                    actualizarBotones(true);
                }
                mPlayer.setOnCompletionListener(mediaPlayer -> {
                    videoView.start();
                    actualizarBotones(true);
                });
            }
        });

        if (savedInstanceState != null) {
            videoActual = savedInstanceState.getInt("posicion");
        }

        btnPlay.setOnClickListener(v -> {
            videoView.start();
            actualizarBotones(true);
        });

        btnPause.setOnClickListener(v -> {
            videoView.pause();
            actualizarBotones(false);
        });

        btnReset.setOnClickListener(v -> {
            videoView.stopPlayback();
            videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.super_mario_galaxy);
            videoView.start();
            actualizarBotones(true);
        });
    }

    private void actualizarBotones(boolean isPlaying) {
        Animation animEntrar = AnimationUtils.loadAnimation(this, R.anim.apareixer);
        Animation animSortir = AnimationUtils.loadAnimation(this, R.anim.desapareixer);

        if (isPlaying) {
            if (btnPlay.getVisibility() == View.VISIBLE) {
                btnPlay.startAnimation(animSortir);
                btnPlay.setVisibility(View.GONE);
            }
            if (btnPause.getVisibility() != View.VISIBLE) {
                btnPause.setVisibility(View.VISIBLE);
                btnPause.startAnimation(animEntrar);
            }
        } else {
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

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
            actualizarBotones(false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (videoView != null) {
            outState.putInt("posicion", videoView.getCurrentPosition());
        }
    }
}