package com.example.appvideo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Li diem a l'app quin fitxer XML ha de fer servir
        setContentView(R.layout.activity_main);

        // Si no hi ha cap fragment carregat, carreguem el MenuFragment per defecte
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    //Carreguem el Menu principal
                    .replace(R.id.fragment_container, new MenuFragment())
                    .commit();
        }
    }

    // Aquesta funció serveix per canviar el que veiem per una altra pantalla
    public void replaceFragment(androidx.fragment.app.Fragment fragment) {
        // Preparem el canvi de pantalla
        getSupportFragmentManager().beginTransaction()
                //Replaçem la pantalla que esta per la que el usuari a idicat
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                //Guardem canvi
                .commit();
    }
}