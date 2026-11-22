package com.example.mindstep;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.slider.Slider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Pantalla 4: Registro Diario de Hábitos
 * Formulario interactivo con sliders para capturar métricas diarias
 * (sueño, estrés, ánimo, hidratación, actividad, pantalla, notas y etiquetas)
 * y generar la evaluación diaria de bienestar con almacenamiento en SQLite.
 */
public class RegistroHabitosActivity extends AppCompatActivity {

    private ImageButton btnClose;

    private TextView tvValorSueno;
    private Slider sliderSueno;
    private TextView tvFeedbackSueno;

    private TextView tvValorEstres;
    private Slider sliderEstres;
    private TextView tvFeedbackEstres;

    private TextView tvAnimoExcelente, tvAnimoBien, tvAnimoRegular, tvAnimoMal, tvAnimoBurnout;
    private int animoSeleccionado = 1; // 0=Excelente, 1=Bien, 2=Regular, 3=Mal, 4=Burnout

    private Chip chipExamenes, chipTareas, chipTrabajo, chipInsomnio, chipEjercicio, chipSocial;

    private TextView tvValorActividad;
    private Slider sliderActividad;

    private TextView tvValorAgua;
    private Slider sliderAgua;

    private TextView tvValorPantalla;
    private Slider sliderPantalla;

    private EditText etNotasRegistro;
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
        tvFeedbackSueno = findViewById(R.id.tvFeedbackSueno);

        tvValorEstres = findViewById(R.id.tvValorEstres);
        sliderEstres = findViewById(R.id.sliderEstres);
        tvFeedbackEstres = findViewById(R.id.tvFeedbackEstres);

        tvAnimoExcelente = findViewById(R.id.tvAnimoExcelente);
        tvAnimoBien = findViewById(R.id.tvAnimoBien);
        tvAnimoRegular = findViewById(R.id.tvAnimoRegular);
        tvAnimoMal = findViewById(R.id.tvAnimoMal);
        tvAnimoBurnout = findViewById(R.id.tvAnimoBurnout);

        chipExamenes = findViewById(R.id.chipExamenes);
        chipTareas = findViewById(R.id.chipTareas);
        chipTrabajo = findViewById(R.id.chipTrabajo);
        chipInsomnio = findViewById(R.id.chipInsomnio);
        chipEjercicio = findViewById(R.id.chipEjercicio);
        chipSocial = findViewById(R.id.chipSocial);

        tvValorActividad = findViewById(R.id.tvValorActividad);
        sliderActividad = findViewById(R.id.sliderActividad);

        tvValorAgua = findViewById(R.id.tvValorAgua);
        sliderAgua = findViewById(R.id.sliderAgua);

        tvValorPantalla = findViewById(R.id.tvValorPantalla);
        sliderPantalla = findViewById(R.id.sliderPantalla);

        etNotasRegistro = findViewById(R.id.etNotasRegistro);
        btnGuardarRegistro = findViewById(R.id.btnGuardarRegistro);
    }

    private void setupListeners() {
        btnClose.setOnClickListener(v -> finish());

        // Slider Sueño con feedback en vivo
        sliderSueno.addOnChangeListener((slider, value, fromUser) -> {
            tvValorSueno.setText(String.format(Locale.US, "%.1f hrs", value));
            if (value >= 7.0f && value <= 9.0f) {
                tvFeedbackSueno.setText("✅ Rango óptimo y reparador para tu jornada.");
                tvFeedbackSueno.setTextColor(Color.parseColor("#22C55E"));
            } else if (value >= 6.0f) {
                tvFeedbackSueno.setText("⚠️ Descanso justo. Intenta dormir 1 hora más hoy.");
                tvFeedbackSueno.setTextColor(Color.parseColor("#F59E0B"));
            } else {
                tvFeedbackSueno.setText("🚨 Déficit de sueño severo: Aumenta tu vulnerabilidad al estrés.");
                tvFeedbackSueno.setTextColor(Color.parseColor("#EF4444"));
            }
        });

        // Slider Estrés con feedback en vivo
        sliderEstres.addOnChangeListener((slider, value, fromUser) -> {
            int val = (int) value;
            if (val <= 3) {
                tvValorEstres.setText("● " + val + " / 10 (Leve)");
                tvValorEstres.setTextColor(Color.parseColor("#22C55E"));
                tvFeedbackEstres.setText("✅ Nivel de tensión bajo y controlado.");
                tvFeedbackEstres.setTextColor(Color.parseColor("#22C55E"));
            } else if (val <= 6) {
                tvValorEstres.setText("● " + val + " / 10 (Moderado)");
                tvValorEstres.setTextColor(Color.parseColor("#F59E0B"));
                tvFeedbackEstres.setText("⚡ Tensión manejable. Mantén pausas activas durante el día.");
                tvFeedbackEstres.setTextColor(Color.parseColor("#F59E0B"));
            } else {
                tvValorEstres.setText("● " + val + " / 10 (Elevado)");
                tvValorEstres.setTextColor(Color.parseColor("#EF4444"));
                tvFeedbackEstres.setText("🚨 Nivel crítico: Te sugerimos hablar con el Asistente Preventivo.");
                tvFeedbackEstres.setTextColor(Color.parseColor("#EF4444"));
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

        // Concatenar etiquetas de los chips seleccionados
        StringBuilder tagsBuilder = new StringBuilder();
        if (chipExamenes.isChecked()) tagsBuilder.append("#Exámenes ");
        if (chipTareas.isChecked()) tagsBuilder.append("#Tareas ");
        if (chipTrabajo.isChecked()) tagsBuilder.append("#Trabajo ");
        if (chipInsomnio.isChecked()) tagsBuilder.append("#Insomnio ");
        if (chipEjercicio.isChecked()) tagsBuilder.append("#Ejercicio ");
        if (chipSocial.isChecked()) tagsBuilder.append("#Social ");

        String tagsFinales = tagsBuilder.toString().trim();
        if (tagsFinales.isEmpty()) {
            tagsFinales = res.disparadorPrincipal;
        }

        tvModalDisparador.setText("🏷️ Detonantes: " + tagsFinales);

        final String tagsParaGuardar = tagsFinales;

        btnModalCerrar.setOnClickListener(v -> {
            guardarRegistroEnBD(res, sueno, estres, tagsParaGuardar);

            preferencesManager.setLastScore(res.score);
            preferencesManager.setLastRisk(res.nivelRiesgo);

            dialog.dismiss();
            Toast.makeText(this, "Registro guardado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        });

        dialog.show();
    }

    private void guardarRegistroEnBD(ValidadorMetricas.ResultadoCalculo res, float sueno, int estres, String tags) {
        try {
            SimpleDateFormat sdfDia = new SimpleDateFormat("dd MMMM", new Locale("es", "MX"));
            String fecha = sdfDia.format(new Date());

            String tituloFecha = "Hoy - " + fecha;
            String suenoStr = "Sueño " + String.format(Locale.US, "%.1fh", sueno);
            String estresStr = "Estrés " + estres + "/10";

            String[] animoNombres = {"😃 Excelente", "🙂 Bien", "😐 Regular", "🙁 Mal", "😫 Agotado"};
            String animoStr = "Ánimo " + animoNombres[animoSeleccionado];
            String notas = etNotasRegistro.getText().toString().trim();

            dbHelper.insertarRegistro(
                    fecha,
                    tituloFecha,
                    "Riesgo " + res.nivelRiesgo,
                    res.colorHex,
                    suenoStr,
                    estresStr,
                    animoStr,
                    tags,
                    notas
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
