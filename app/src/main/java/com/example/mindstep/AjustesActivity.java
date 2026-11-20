package com.example.mindstep;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

/**
 * Pantalla 8: Ajustes y Configuración
 * Administra el perfil local del usuario, switch de modo oscuro,
 * alertas preventivas, notificaciones y utilidades del sistema.
 */
public class AjustesActivity extends AppCompatActivity {

    private TextView tvAvatarAjustes;
    private TextView tvNombreAjustes;

    private SwitchMaterial switchRecordatorio;
    private SwitchMaterial switchAlertaEstres;
    private SwitchMaterial switchModoOscuro;

    private MaterialButton btnReiniciarDiagnostico;
    private BottomNavigationView bottomNavigation;

    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        preferencesManager = new PreferencesManager(this);

        initViews();
        cargarPreferencias();
        setupListeners();
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigation.setSelectedItemId(R.id.nav_ajustes);
    }

    private void initViews() {
        tvAvatarAjustes = findViewById(R.id.tvAvatarAjustes);
        tvNombreAjustes = findViewById(R.id.tvNombreAjustes);

        switchRecordatorio = findViewById(R.id.switchRecordatorio);
        switchAlertaEstres = findViewById(R.id.switchAlertaEstres);
        switchModoOscuro = findViewById(R.id.switchModoOscuro);

        btnReiniciarDiagnostico = findViewById(R.id.btnReiniciarDiagnostico);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void cargarPreferencias() {
        String nombre = preferencesManager.getUserName();
        if (nombre == null || nombre.trim().isEmpty()) {
            nombre = "Axel Guillermo";
        }
        tvNombreAjustes.setText(nombre);
        tvAvatarAjustes.setText(String.valueOf(nombre.charAt(0)).toUpperCase());

        switchRecordatorio.setChecked(preferencesManager.isRemindersEnabled());
        switchAlertaEstres.setChecked(preferencesManager.isStressAlertEnabled());
        switchModoOscuro.setChecked(preferencesManager.isDarkMode());
    }

    private void setupListeners() {
        switchRecordatorio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesManager.setRemindersEnabled(isChecked);
            Toast.makeText(this, isChecked ? "Recordatorios activados (09:00 PM)" : "Recordatorios desactivados", Toast.LENGTH_SHORT).show();
        });

        switchAlertaEstres.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesManager.setStressAlertEnabled(isChecked);
            Toast.makeText(this, isChecked ? "Alertas de sobrecarga activadas" : "Alertas desactivadas", Toast.LENGTH_SHORT).show();
        });

        switchModoOscuro.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesManager.setDarkMode(isChecked);
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Opciones de lista de información
        findViewById(R.id.itemIdioma).setOnClickListener(v ->
                Toast.makeText(this, "Idioma: Español (Latinoamérica)", Toast.LENGTH_SHORT).show()
        );

        findViewById(R.id.itemPerfil).setOnClickListener(v ->
                Toast.makeText(this, "Perfil: " + preferencesManager.getUserName(), Toast.LENGTH_SHORT).show()
        );

        findViewById(R.id.itemContacto).setOnClickListener(v ->
                Toast.makeText(this, "Soporte MindStep: soporte@mindstep.app", Toast.LENGTH_SHORT).show()
        );

        findViewById(R.id.itemAyuda).setOnClickListener(v ->
                Toast.makeText(this, "MindStep - Versión 1.0.0 (Offline First)", Toast.LENGTH_SHORT).show()
        );

        findViewById(R.id.itemValorar).setOnClickListener(v ->
                Toast.makeText(this, "¡Gracias por tus comentarios!", Toast.LENGTH_SHORT).show()
        );

        // Repetir Evaluación Inicial
        btnReiniciarDiagnostico.setOnClickListener(v -> {
            preferencesManager.setFirstTime(true);
            Toast.makeText(this, "Iniciando nueva evaluación diagnóstica...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AjustesActivity.this, EvaluacionActivity.class);
            startActivity(intent);
        });
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_inicio) {
                Intent intent = new Intent(AjustesActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_historial) {
                Intent intent = new Intent(AjustesActivity.this, HistorialActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_estadisticas) {
                Intent intent = new Intent(AjustesActivity.this, EstadisticasActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_ajustes) {
                return true;
            }
            return false;
        });
    }
}
