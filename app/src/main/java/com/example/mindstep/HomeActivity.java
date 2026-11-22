package com.example.mindstep;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Pantalla 3: Pantalla Principal / Home
 * Panel central de la aplicación que muestra el nivel de riesgo actual del usuario,
 * accesos rápidos para capturar hábitos diarios y la opción de interactuar con el asistente.
 */
public class HomeActivity extends AppCompatActivity {

    private TextView tvAvatarLetra;
    private TextView tvSaludoUsuario;
    private TextView tvFechaHoy;
    private ImageButton btnConfigRapida;

    private MaterialCardView cardNivelRiesgo;
    private TextView tvBadgeRiesgo;
    private ImageView ivIconoRiesgo;
    private TextView tvMensajeRiesgo;

    private MaterialButton btnCapturarHabitos;
    private MaterialButton btnHablarAsistente;
    private BottomNavigationView bottomNavigation;

    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        preferencesManager = new PreferencesManager(this);

        initViews();
        setupEvents();
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarDatosUsuario();
        bottomNavigation.setSelectedItemId(R.id.nav_inicio);
    }

    private void initViews() {
        tvAvatarLetra = findViewById(R.id.tvAvatarLetra);
        tvSaludoUsuario = findViewById(R.id.tvSaludoUsuario);
        tvFechaHoy = findViewById(R.id.tvFechaHoy);
        btnConfigRapida = findViewById(R.id.btnConfigRapida);

        cardNivelRiesgo = findViewById(R.id.cardNivelRiesgo);
        tvBadgeRiesgo = findViewById(R.id.tvBadgeRiesgo);
        ivIconoRiesgo = findViewById(R.id.ivIconoRiesgo);
        tvMensajeRiesgo = findViewById(R.id.tvMensajeRiesgo);

        btnCapturarHabitos = findViewById(R.id.btnCapturarHabitos);
        btnHablarAsistente = findViewById(R.id.btnHablarAsistente);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void actualizarDatosUsuario() {
        String nombre = preferencesManager.getUserName();
        if (nombre == null || nombre.trim().isEmpty()) {
            nombre = "Axel";
        }

        tvSaludoUsuario.setText("Hola, " + nombre);
        if (!nombre.isEmpty()) {
            tvAvatarLetra.setText(String.valueOf(nombre.charAt(0)).toUpperCase());
        }

        // Fecha actual formateada (ej. "Jueves, 08 de Octubre")
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd 'de' MMMM", new Locale("es", "MX"));
            String fechaActual = sdf.format(new Date());
            fechaActual = fechaActual.substring(0, 1).toUpperCase() + fechaActual.substring(1);
            tvFechaHoy.setText(fechaActual);
        } catch (Exception e) {
            tvFechaHoy.setText("Jueves, 08 de Octubre");
        }

        // Actualizar tarjeta según el último riesgo registrado
        String riesgo = preferencesManager.getLastRisk();
        int score = preferencesManager.getLastScore();

        if ("Elevado".equalsIgnoreCase(riesgo)) {
            cardNivelRiesgo.setCardBackgroundColor(Color.parseColor("#FEF2F2"));
            cardNivelRiesgo.setStrokeColor(Color.parseColor("#FECACA"));
            tvBadgeRiesgo.setText("● NIVEL DE RIESGO: ELEVADO (" + score + " pts)");
            tvBadgeRiesgo.setTextColor(Color.parseColor("#EF4444"));
            ivIconoRiesgo.setColorFilter(Color.parseColor("#EF4444"));
            tvMensajeRiesgo.setText("Sobrecarga alta detectada. Toca aquí para que el Asistente te guíe a regular tu tensión.");
        } else if ("Moderado".equalsIgnoreCase(riesgo)) {
            cardNivelRiesgo.setCardBackgroundColor(Color.parseColor("#FFFBEB"));
            cardNivelRiesgo.setStrokeColor(Color.parseColor("#FDE68A"));
            tvBadgeRiesgo.setText("● NIVEL DE RIESGO: MODERADO (" + score + " pts)");
            tvBadgeRiesgo.setTextColor(Color.parseColor("#F59E0B"));
            ivIconoRiesgo.setColorFilter(Color.parseColor("#F59E0B"));
            tvMensajeRiesgo.setText("Tu ritmo diario muestra tensión o descanso justo. Toca aquí para ver consejos.");
        } else {
            cardNivelRiesgo.setCardBackgroundColor(Color.parseColor("#F0FDF4"));
            cardNivelRiesgo.setStrokeColor(Color.parseColor("#BBF7D0"));
            tvBadgeRiesgo.setText("● NIVEL DE RIESGO: BAJO / ESTABLE (" + score + " pts)");
            tvBadgeRiesgo.setTextColor(Color.parseColor("#22C55E"));
            ivIconoRiesgo.setColorFilter(Color.parseColor("#22C55E"));
            tvMensajeRiesgo.setText("Tus hábitos de sueño y presión se mantienen en equilibrio hoy.");
        }
    }

    private void setupEvents() {
        // Tocar la tarjeta de riesgo abre el asistente de apoyo
        cardNivelRiesgo.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AsistenteActivity.class);
            startActivity(intent);
        });

        btnCapturarHabitos.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, RegistroHabitosActivity.class);
            startActivity(intent);
        });

        btnHablarAsistente.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AsistenteActivity.class);
            startActivity(intent);
        });

        btnConfigRapida.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AjustesActivity.class);
            startActivity(intent);
        });

        tvAvatarLetra.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AjustesActivity.class);
            startActivity(intent);
        });
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_inicio) {
                return true;
            } else if (itemId == R.id.nav_historial) {
                Intent intent = new Intent(HomeActivity.this, HistorialActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_estadisticas) {
                Intent intent = new Intent(HomeActivity.this, EstadisticasActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_ajustes) {
                Intent intent = new Intent(HomeActivity.this, AjustesActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}
