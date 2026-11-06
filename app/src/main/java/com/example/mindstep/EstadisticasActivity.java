package com.example.mindstep;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButtonToggleGroup;

/**
 * Pantalla 6: Estadísticas y Patrones
 * Visualización de correlaciones entre horas de descanso y picos de estrés,
 * junto con la identificación algorítmica de los principales factores detonantes.
 */
public class EstadisticasActivity extends AppCompatActivity {

    private GraficaPatronesView graficaPatrones;
    private MaterialButtonToggleGroup togglePeriodo;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        graficaPatrones = findViewById(R.id.graficaPatrones);
        togglePeriodo = findViewById(R.id.togglePeriodo);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        setupToggleGroup();
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigation.setSelectedItemId(R.id.nav_estadisticas);
    }

    private void setupToggleGroup() {
        togglePeriodo.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btnPeriodo7Dias) {
                    graficaPatrones.setPeriodo(7);
                } else if (checkedId == R.id.btnPeriodo30Dias) {
                    graficaPatrones.setPeriodo(30);
                } else if (checkedId == R.id.btnPeriodo3Meses) {
                    graficaPatrones.setPeriodo(90);
                }
            }
        });
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_inicio) {
                Intent intent = new Intent(EstadisticasActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_historial) {
                Intent intent = new Intent(EstadisticasActivity.this, HistorialActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_estadisticas) {
                return true;
            } else if (itemId == R.id.nav_ajustes) {
                Intent intent = new Intent(EstadisticasActivity.this, AjustesActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}
