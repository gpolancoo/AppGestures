package com.example.appvideo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Al iniciar, carreguem el menú principal.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Si no hi ha cap fragment carregat, carreguem el MenuFragment per defecte
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MenuFragment())
                    .commit();
        }
    }

    public void replaceFragment(androidx.fragment.app.Fragment fragment) {
        // Si volem canviar de fragment des del MenuFragment, cridem aquesta funció per fer el canvi
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null) // Permet tornar al menú amb el botó de "back"
                .commit();
    }
}