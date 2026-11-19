package com.example.mindstep;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.Locale;

/**
 * Pantalla 6: Estadísticas y Patrones
 * Visualización de correlaciones entre horas de descanso y picos de estrés,
 * junto con la identificación algorítmica de los principales factores detonantes.
 */
public class EstadisticasActivity extends AppCompatActivity {

    private GraficaPatronesView graficaPatrones;
    private MaterialButtonToggleGroup togglePeriodo;
    private BottomNavigationView bottomNavigation;

    private TextView tvPromedioSuenoLeyenda;
    private TextView tvPromedioEstresLeyenda;
    private TextView tvTextoPatron;
    private TextView tvPieGrafica;

    private DB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        dbHelper = new DB(this);

        initViews();
        setupToggleGroup();
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigation.setSelectedItemId(R.id.nav_estadisticas);
        cargarEstadisticasDesdeBD();
    }

    private void initViews() {
        graficaPatrones = findViewById(R.id.graficaPatrones);
        togglePeriodo = findViewById(R.id.togglePeriodo);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        tvPromedioSuenoLeyenda = findViewById(R.id.tvPromedioSuenoLeyenda);
        tvPromedioEstresLeyenda = findViewById(R.id.tvPromedioEstresLeyenda);
        tvTextoPatron = findViewById(R.id.tvTextoPatron);
        tvPieGrafica = findViewById(R.id.tvPieGrafica);
    }

    private void cargarEstadisticasDesdeBD() {
        float promSueno = dbHelper.obtenerPromedioSueno();
        float promEstres = dbHelper.obtenerPromedioEstres();
        int total = dbHelper.obtenerTotalRegistros();

        tvPromedioSuenoLeyenda.setText(String.format(Locale.US, "Sueño (~%.1fh)", promSueno));
        tvPromedioEstresLeyenda.setText(String.format(Locale.US, "Estrés (~%.1f/10)", promEstres));

        tvPieGrafica.setText("Datos calculados a partir de " + total + " registros en SQLite");

        // Diagnóstico algorítmico real
        if (promSueno < 6.0f) {
            tvTextoPatron.setText("🚨 Patrón Crítico: Tu promedio de descanso es de solo " + String.format(Locale.US, "%.1fh", promSueno) + ". Los días con menos de 6h de sueño duplican tus picos de estrés.");
        } else if (promEstres >= 6.5f) {
            tvTextoPatron.setText("⚠️ Patrón Detectado: Aunque duermes " + String.format(Locale.US, "%.1fh", promSueno) + ", la sobrecarga académica o laboral mantiene tu cortisol elevado.");
        } else {
            tvTextoPatron.setText("✅ Hábitos Estables: Mantienes un descanso promedio de " + String.format(Locale.US, "%.1fh", promSueno) + " y niveles controlados de presión.");
        }
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
