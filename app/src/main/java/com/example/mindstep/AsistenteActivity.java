package com.example.mindstep;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

/**
 * Pantalla 7: Asistente de Apoyo Preventivo
 * Módulo interactivo guiado por un árbol de decisiones para ayudar al usuario
 * ante picos de estrés y recomendarle ejercicios prácticos inmediatos.
 */
public class AsistenteActivity extends AppCompatActivity {

    private ImageButton btnBackAsistente;
    private ScrollView scrollChat;
    private TextView tvMensajeInicialBot;

    private LinearLayout layoutOpcionesDecision;
    private MaterialButton btnOpcionTrabajo;
    private MaterialButton btnOpcionAnsiedad;
    private MaterialButton btnOpcionAgotamiento;

    private LinearLayout layoutRespuestaUsuario;
    private TextView tvRespuestaUsuarioTexto;

    private MaterialCardView cardEjercicioRecomendado;
    private TextView tvTituloEjercicioCard;
    private TextView tvDescripcionEjercicioCard;
    private MaterialButton btnIniciarEjercicio;

    private PreferencesManager preferencesManager;
    private AsistenteLogica.Recomendacion recomendacionActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistente);

        preferencesManager = new PreferencesManager(this);

        initViews();
        personalizarMensajeInicial();
        setupListeners();
    }

    private void initViews() {
        btnBackAsistente = findViewById(R.id.btnBackAsistente);
        scrollChat = findViewById(R.id.scrollChat);
        tvMensajeInicialBot = findViewById(R.id.tvMensajeInicialBot);

        layoutOpcionesDecision = findViewById(R.id.layoutOpcionesDecision);
        btnOpcionTrabajo = findViewById(R.id.btnOpcionTrabajo);
        btnOpcionAnsiedad = findViewById(R.id.btnOpcionAnsiedad);
        btnOpcionAgotamiento = findViewById(R.id.btnOpcionAgotamiento);

        layoutRespuestaUsuario = findViewById(R.id.layoutRespuestaUsuario);
        tvRespuestaUsuarioTexto = findViewById(R.id.tvRespuestaUsuarioTexto);

        cardEjercicioRecomendado = findViewById(R.id.cardEjercicioRecomendado);
        tvTituloEjercicioCard = findViewById(R.id.tvTituloEjercicioCard);
        tvDescripcionEjercicioCard = findViewById(R.id.tvDescripcionEjercicioCard);
        btnIniciarEjercicio = findViewById(R.id.btnIniciarEjercicio);
    }

    private void personalizarMensajeInicial() {
        String nombre = preferencesManager.getUserName();
        String riesgo = preferencesManager.getLastRisk();

        if ("Elevado".equalsIgnoreCase(riesgo)) {
            tvMensajeInicialBot.setText("Hola " + nombre + ". He detectado un nivel de estrés elevado y pocas horas de sueño. ¿Cuál es el factor principal que te afecta en este momento?");
        } else if ("Moderado".equalsIgnoreCase(riesgo)) {
            tvMensajeInicialBot.setText("Hola " + nombre + ". Veo que tu ritmo diario ha estado algo agitado. ¿Cuál es la causa principal de esa presión?");
        } else {
            tvMensajeInicialBot.setText("Hola " + nombre + ". Tus hábitos se mantienen estables. Si deseas regular tu energía o relajarte, ¿en qué área te gustaría trabajar?");
        }
    }

    private void setupListeners() {
        btnBackAsistente.setOnClickListener(v -> finish());

        btnOpcionTrabajo.setOnClickListener(v -> procesarEleccion("Carga de Trabajo / Escuela"));
        btnOpcionAnsiedad.setOnClickListener(v -> procesarEleccion("Ansiedad / Pensamientos Rumiantes"));
        btnOpcionAgotamiento.setOnClickListener(v -> procesarEleccion("Agotamiento Físico"));

        btnIniciarEjercicio.setOnClickListener(v -> {
            if (recomendacionActual != null) {
                mostrarDialogoEjercicio(recomendacionActual);
            }
        });
    }

    private void procesarEleccion(String eleccion) {
        // Ocultar botones de opciones y mostrar respuesta del usuario
        layoutOpcionesDecision.setVisibility(View.GONE);
        layoutRespuestaUsuario.setVisibility(View.VISIBLE);
        tvRespuestaUsuarioTexto.setText(eleccion);

        // Consultar árbol de decisiones
        recomendacionActual = AsistenteLogica.obtenerRecomendacion(eleccion);

        // Desplegar tarjeta de recomendación
        cardEjercicioRecomendado.setVisibility(View.VISIBLE);
        tvTituloEjercicioCard.setText(recomendacionActual.titulo);
        tvDescripcionEjercicioCard.setText(recomendacionActual.explicacion);
        btnIniciarEjercicio.setText(recomendacionActual.botonAccion);

        // Desplazar al fondo del chat
        scrollChat.post(() -> scrollChat.fullScroll(View.FOCUS_DOWN));
    }

    private void mostrarDialogoEjercicio(AsistenteLogica.Recomendacion rec) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_ejercicio_respiracion, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView tvTitulo = dialogView.findViewById(R.id.tvTituloEjercicio);
        TextView tvInstruccion = dialogView.findViewById(R.id.tvInstruccionRespiracion);
        TextView tvSegundos = dialogView.findViewById(R.id.tvSegundosRespiracion);
        MaterialButton btnTerminar = dialogView.findViewById(R.id.btnFinalizarEjercicio);

        tvTitulo.setText(rec.titulo);

        // Temporizador de respiración interactivo (Ciclos de 4s Inhala, 7s Sostén, 8s Exhala)
        CountDownTimer timer = new CountDownTimer(38000, 1000) {
            int fase = 0; // 0: Inhala (4s), 1: Sostén (7s), 2: Exhala (8s)
            int segundosRestantes = 4;

            @Override
            public void onTick(long millisUntilFinished) {
                tvSegundos.setText(segundosRestantes + "s");

                if (fase == 0) {
                    tvInstruccion.setText("Inhala suavemente");
                    tvInstruccion.setTextColor(Color.parseColor("#2563EB"));
                } else if (fase == 1) {
                    tvInstruccion.setText("Sostén el aire");
                    tvInstruccion.setTextColor(Color.parseColor("#F59E0B"));
                } else {
                    tvInstruccion.setText("Exhala lentamente");
                    tvInstruccion.setTextColor(Color.parseColor("#059669"));
                }

                segundosRestantes--;

                if (segundosRestantes < 0) {
                    fase = (fase + 1) % 3;
                    if (fase == 0) segundosRestantes = 4;
                    else if (fase == 1) segundosRestantes = 7;
                    else segundosRestantes = 8;
                }
            }

            @Override
            public void onFinish() {
                tvInstruccion.setText("¡Excelente trabajo!");
                tvSegundos.setText("✓");
            }
        }.start();

        btnTerminar.setOnClickListener(v -> {
            timer.cancel();
            dialog.dismiss();
            Toast.makeText(this, "¡Bien hecho! Respiración completada.", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }
}
