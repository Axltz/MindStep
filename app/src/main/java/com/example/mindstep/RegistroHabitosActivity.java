package com.example.mindstep;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Pantalla 4: Registro Diario de Hábitos
 * Formulario interactivo con sliders para capturar métricas diarias
 * (sueño, estrés, ánimo, hidratación, actividad, pantalla) y generar
 * la puntuación de bienestar del día con almacenamiento en SQLite.
 */
public class RegistroHabitosActivity extends AppCompatActivity {

    private ImageButton btnClose;

    private TextView tvValorSueno;
    private Slider sliderSueno;

    private TextView tvValorEstres;
    private Slider sliderEstres;

    private TextView tvAnimoExcelente, tvAnimoBien, tvAnimoRegular, tvAnimoMal, tvAnimoBurnout;
    private int animoSeleccionado = 1; // 0=Excelente, 1=Bien, 2=Regular, 3=Mal, 4=Burnout

    private TextView tvValorActividad;
    private Slider sliderActividad;

    private TextView tvValorAgua;
    private Slider sliderAgua;

    private TextView tvValorPantalla;
    private Slider sliderPantalla;

    private MaterialButton btnGuardarRegistro;

    private DB dbHelper;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_habitos);

        dbHelper = new DB(this);
        preferencesManager = new PreferencesManager(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnClose = findViewById(R.id.btnClose);

        tvValorSueno = findViewById(R.id.tvValorSueno);
        sliderSueno = findViewById(R.id.sliderSueno);

        tvValorEstres = findViewById(R.id.tvValorEstres);
        sliderEstres = findViewById(R.id.sliderEstres);

        tvAnimoExcelente = findViewById(R.id.tvAnimoExcelente);
        tvAnimoBien = findViewById(R.id.tvAnimoBien);
        tvAnimoRegular = findViewById(R.id.tvAnimoRegular);
        tvAnimoMal = findViewById(R.id.tvAnimoMal);
        tvAnimoBurnout = findViewById(R.id.tvAnimoBurnout);

        tvValorActividad = findViewById(R.id.tvValorActividad);
        sliderActividad = findViewById(R.id.sliderActividad);

        tvValorAgua = findViewById(R.id.tvValorAgua);
        sliderAgua = findViewById(R.id.sliderAgua);

        tvValorPantalla = findViewById(R.id.tvValorPantalla);
        sliderPantalla = findViewById(R.id.sliderPantalla);

        btnGuardarRegistro = findViewById(R.id.btnGuardarRegistro);
    }

    private void setupListeners() {
        btnClose.setOnClickListener(v -> finish());

        // Slider Sueño
        sliderSueno.addOnChangeListener((slider, value, fromUser) -> {
            tvValorSueno.setText(String.format(Locale.US, "%.1f hrs", value));
        });

        // Slider Estrés
        sliderEstres.addOnChangeListener((slider, value, fromUser) -> {
            int val = (int) value;
            if (val <= 3) {
                tvValorEstres.setText("● " + val + " / 10 (Leve)");
                tvValorEstres.setTextColor(Color.parseColor("#22C55E"));
            } else if (val <= 6) {
                tvValorEstres.setText("● " + val + " / 10 (Moderado)");
                tvValorEstres.setTextColor(Color.parseColor("#F59E0B"));
            } else {
                tvValorEstres.setText("● " + val + " / 10 (Elevado)");
                tvValorEstres.setTextColor(Color.parseColor("#EF4444"));
            }
        });

        // Emojis Ánimo
        tvAnimoExcelente.setOnClickListener(v -> seleccionarAnimo(0));
        tvAnimoBien.setOnClickListener(v -> seleccionarAnimo(1));
        tvAnimoRegular.setOnClickListener(v -> seleccionarAnimo(2));
        tvAnimoMal.setOnClickListener(v -> seleccionarAnimo(3));
        tvAnimoBurnout.setOnClickListener(v -> seleccionarAnimo(4));

        // Slider Actividad
        sliderActividad.addOnChangeListener((slider, value, fromUser) -> {
            tvValorActividad.setText((int) value + " min");
        });

        // Slider Hidratación
        sliderAgua.addOnChangeListener((slider, value, fromUser) -> {
            tvValorAgua.setText(String.format(Locale.US, "%.1f L", value));
        });

        // Slider Pantalla
        sliderPantalla.addOnChangeListener((slider, value, fromUser) -> {
            tvValorPantalla.setText(String.format(Locale.US, "%.1f hrs", value));
        });

        // Botón Guardar
        btnGuardarRegistro.setOnClickListener(v -> procesarYMostrarScore());
    }

    private void seleccionarAnimo(int indice) {
        animoSeleccionado = indice;
        TextView[] views = {tvAnimoExcelente, tvAnimoBien, tvAnimoRegular, tvAnimoMal, tvAnimoBurnout};

        for (int i = 0; i < views.length; i++) {
            if (i == indice) {
                views[i].setBackgroundResource(R.drawable.bg_option_selected);
                views[i].setTextColor(Color.parseColor("#2563EB"));
            } else {
                views[i].setBackground(null);
                views[i].setTextColor(Color.parseColor("#334155"));
            }
        }
    }

    private void procesarYMostrarScore() {
        float sueno = sliderSueno.getValue();
        int estres = (int) sliderEstres.getValue();
        int actividad = (int) sliderActividad.getValue();
        float agua = sliderAgua.getValue();
        float pantalla = sliderPantalla.getValue();

        ValidadorMetricas.ResultadoCalculo resultado = ValidadorMetricas.evaluarMetricas(
                sueno,
                estres,
                animoSeleccionado,
                actividad,
                agua,
                pantalla
        );

        mostrarDialogoScore(resultado, sueno, estres);
    }

    private void mostrarDialogoScore(ValidadorMetricas.ResultadoCalculo res, float sueno, int estres) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_score_dia, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView tvModalScorePuntos = dialogView.findViewById(R.id.tvModalScorePuntos);
        TextView tvModalNivelBienestar = dialogView.findViewById(R.id.tvModalNivelBienestar);
        TextView tvModalDesgloseSueno = dialogView.findViewById(R.id.tvModalDesgloseSueno);
        TextView tvModalDesgloseEstres = dialogView.findViewById(R.id.tvModalDesgloseEstres);
        TextView tvModalDisparador = dialogView.findViewById(R.id.tvModalDisparador);
        MaterialButton btnModalCerrar = dialogView.findViewById(R.id.btnModalCerrar);

        tvModalScorePuntos.setText(String.valueOf(res.score));
        tvModalScorePuntos.setTextColor(Color.parseColor(res.colorHex));

        tvModalNivelBienestar.setText("Nivel de Riesgo " + res.nivelRiesgo);
        tvModalNivelBienestar.setTextColor(Color.parseColor(res.colorHex));

        tvModalDesgloseSueno.setText("💤 " + res.desgloseSueno);
        tvModalDesgloseEstres.setText("⚡ " + res.desgloseEstres);
        tvModalDisparador.setText("🏷️ Disparador " + res.disparadorPrincipal);

        btnModalCerrar.setOnClickListener(v -> {
            // Guardar en la base de datos SQLite local
            guardarRegistroEnBD(res, sueno, estres);

            // Actualizar preferencias para el home
            preferencesManager.setLastScore(res.score);
            preferencesManager.setLastRisk(res.nivelRiesgo);

            dialog.dismiss();
            Toast.makeText(this, "Registro guardado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        });

        dialog.show();
    }

    private void guardarRegistroEnBD(ValidadorMetricas.ResultadoCalculo res, float sueno, int estres) {
        try {
            SimpleDateFormat sdfDia = new SimpleDateFormat("dd MMMM", new Locale("es", "MX"));
            String fecha = sdfDia.format(new Date());

            String tituloFecha = "Hoy - " + fecha;
            String suenoStr = "Sueño " + String.format(Locale.US, "%.1fh", sueno);
            String estresStr = "Estrés " + estres + "/10";

            String[] animoNombres = {"😃 Excelente", "🙂 Bien", "😐 Regular", "🙁 Mal", "😫 Agotado"};
            String animoStr = "Ánimo " + animoNombres[animoSeleccionado];

            String tags = res.disparadorPrincipal + " #Registro_Diario";

            dbHelper.insertarRegistro(
                    fecha,
                    tituloFecha,
                    "Riesgo " + res.nivelRiesgo,
                    res.colorHex,
                    suenoStr,
                    estresStr,
                    animoStr,
                    tags
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
