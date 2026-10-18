package com.example.mindstep;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

/**
 * Pantalla 2: Evaluación Inicial / Diagnóstico Breve
 * Cuestionario de 3 pasos para determinar el estado emocional base del usuario.
 */
public class EvaluacionActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvPasoIndicador;
    private LinearProgressIndicator progressBar;
    private TextView tvCategoria;
    private TextView tvPregunta;

    private MaterialCardView cardOpcionA, cardOpcionB, cardOpcionC, cardOpcionD;
    private TextView tvBadgeA, tvBadgeB, tvBadgeC, tvBadgeD;
    private TextView tvTextoA, tvTextoB, tvTextoC, tvTextoD;
    private ImageView ivCheckA, ivCheckB, ivCheckC, ivCheckD;

    private MaterialButton btnSiguiente;

    private int pasoActual = 1;
    private int opcionSeleccionada = -1; // 0=A, 1=B, 2=C, 3=D
    private int puntajeAcumulado = 0;

    private PreferencesManager preferencesManager;

    private static class PreguntaConfig {
        String pasoTexto;
        int progreso;
        String categoria;
        String pregunta;
        String opA, opB, opC, opD;

        PreguntaConfig(String pasoTexto, int progreso, String categoria, String pregunta, String opA, String opB, String opC, String opD) {
            this.pasoTexto = pasoTexto;
            this.progreso = progreso;
            this.categoria = categoria;
            this.pregunta = pregunta;
            this.opA = opA;
            this.opB = opB;
            this.opC = opC;
            this.opD = opD;
        }
    }

    private final PreguntaConfig[] preguntas = new PreguntaConfig[]{
            new PreguntaConfig(
                    "Paso 1 de 3 - Diagnóstico Inicial",
                    33,
                    "ESTRÉS Y ENERGÍA",
                    "¿Con qué frecuencia has sentido sobrecarga de estrés o agotamiento en tu trabajo/escuela esta semana?",
                    "Rara vez o Nunca",
                    "A veces durante la semana",
                    "Frecuentemente casi todos los días",
                    "Constante y abrumador"
            ),
            new PreguntaConfig(
                    "Paso 2 de 3 - Diagnóstico Inicial",
                    66,
                    "SUEÑO Y DESCANSO",
                    "¿Cuántas horas promedio sueles dormir y qué tan reparador sientes tu descanso diario?",
                    "7 a 8 horas con descanso profundo",
                    "6 a 7 horas, descanso regular",
                    "Menos de 6 horas, despierto cansado",
                    "Insomnio frecuente o muy irregular"
            ),
            new PreguntaConfig(
                    "Paso 3 de 3 - Diagnóstico Inicial",
                    100,
                    "REGULACIÓN Y HÁBITOS",
                    "Cuando experimentas momentos de alta tensión, ¿cómo sueles responder habitualmente?",
                    "Hago pausas y aplico técnicas de respiración",
                    "Trato de distraerme o continuar la marcha",
                    "Me guardo la tensión y me siento sobrecargado",
                    "Siento bloqueo mental o irritabilidad severa"
            )
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluacion_inicial);

        preferencesManager = new PreferencesManager(this);

        initViews();
        cargarPregunta(pasoActual);

        btnBack.setOnClickListener(v -> {
            if (pasoActual > 1) {
                pasoActual--;
                cargarPregunta(pasoActual);
            } else {
                finish();
            }
        });

        // Listeners para tarjetas de opciones
        cardOpcionA.setOnClickListener(v -> seleccionarOpcion(0));
        cardOpcionB.setOnClickListener(v -> seleccionarOpcion(1));
        cardOpcionC.setOnClickListener(v -> seleccionarOpcion(2));
        cardOpcionD.setOnClickListener(v -> seleccionarOpcion(3));

        btnSiguiente.setOnClickListener(v -> {
            if (opcionSeleccionada == -1) {
                Toast.makeText(this, "Por favor selecciona una opción", Toast.LENGTH_SHORT).show();
                return;
            }

            // Puntuación inversa: 0 (mejor) a 3 (más vulnerable)
            puntajeAcumulado += opcionSeleccionada;

            if (pasoActual < 3) {
                pasoActual++;
                cargarPregunta(pasoActual);
            } else {
                finalizarDiagnostico();
            }
        });
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvPasoIndicador = findViewById(R.id.tvPasoIndicador);
        progressBar = findViewById(R.id.progressBar);
        tvCategoria = findViewById(R.id.tvCategoria);
        tvPregunta = findViewById(R.id.tvPregunta);

        cardOpcionA = findViewById(R.id.cardOpcionA);
        cardOpcionB = findViewById(R.id.cardOpcionB);
        cardOpcionC = findViewById(R.id.cardOpcionC);
        cardOpcionD = findViewById(R.id.cardOpcionD);

        tvBadgeA = findViewById(R.id.tvBadgeA);
        tvBadgeB = findViewById(R.id.tvBadgeB);
        tvBadgeC = findViewById(R.id.tvBadgeC);
        tvBadgeD = findViewById(R.id.tvBadgeD);

        tvTextoA = findViewById(R.id.tvTextoA);
        tvTextoB = findViewById(R.id.tvTextoB);
        tvTextoC = findViewById(R.id.tvTextoC);
        tvTextoD = findViewById(R.id.tvTextoD);

        ivCheckA = findViewById(R.id.ivCheckA);
        ivCheckB = findViewById(R.id.ivCheckB);
        ivCheckC = findViewById(R.id.ivCheckC);
        ivCheckD = findViewById(R.id.ivCheckD);

        btnSiguiente = findViewById(R.id.btnSiguiente);
    }

    private void cargarPregunta(int paso) {
        opcionSeleccionada = -1;
        resetearEstiloOpciones();

        PreguntaConfig config = preguntas[paso - 1];
        tvPasoIndicador.setText(config.pasoTexto);
        progressBar.setProgress(config.progreso);
        tvCategoria.setText(config.categoria);
        tvPregunta.setText(config.pregunta);

        tvTextoA.setText(config.opA);
        tvTextoB.setText(config.opB);
        tvTextoC.setText(config.opC);
        tvTextoD.setText(config.opD);

        if (paso == 3) {
            btnSiguiente.setText("Finalizar Diagnóstico");
        } else {
            btnSiguiente.setText("Siguiente Pregunta");
        }
    }

    private void seleccionarOpcion(int opcion) {
        opcionSeleccionada = opcion;
        resetearEstiloOpciones();

        MaterialCardView selectedCard;
        ImageView checkView;
        TextView badgeView;

        switch (opcion) {
            case 0:
                selectedCard = cardOpcionA;
                checkView = ivCheckA;
                badgeView = tvBadgeA;
                break;
            case 1:
                selectedCard = cardOpcionB;
                checkView = ivCheckB;
                badgeView = tvBadgeB;
                break;
            case 2:
                selectedCard = cardOpcionC;
                checkView = ivCheckC;
                badgeView = tvBadgeC;
                break;
            default:
                selectedCard = cardOpcionD;
                checkView = ivCheckD;
                badgeView = tvBadgeD;
                break;
        }

        selectedCard.setStrokeColor(Color.parseColor("#2563EB"));
        selectedCard.setStrokeWidth(4);
        selectedCard.setCardBackgroundColor(Color.parseColor("#EFF6FF"));
        checkView.setVisibility(View.VISIBLE);
        badgeView.setTextColor(Color.parseColor("#2563EB"));
    }

    private void resetearEstiloOpciones() {
        int colorBordeDefault = Color.parseColor("#E2E8F0");
        int colorFondoDefault = Color.parseColor("#FFFFFF");
        int colorTextoDefault = Color.parseColor("#0F172A");

        MaterialCardView[] cards = {cardOpcionA, cardOpcionB, cardOpcionC, cardOpcionD};
        ImageView[] checks = {ivCheckA, ivCheckB, ivCheckC, ivCheckD};
        TextView[] badges = {tvBadgeA, tvBadgeB, tvBadgeC, tvBadgeD};

        for (int i = 0; i < 4; i++) {
            cards[i].setStrokeColor(colorBordeDefault);
            cards[i].setStrokeWidth(2);
            cards[i].setCardBackgroundColor(colorFondoDefault);
            checks[i].setVisibility(View.GONE);
            badges[i].setTextColor(colorTextoDefault);
        }
    }

    private void finalizarDiagnostico() {
        // Calcular riesgo inicial
        String riesgoInicial = "Bajo";
        int scoreInicial = 85;

        if (puntajeAcumulado >= 6) {
            riesgoInicial = "Elevado";
            scoreInicial = 42;
        } else if (puntajeAcumulado >= 3) {
            riesgoInicial = "Moderado";
            scoreInicial = 65;
        }

        preferencesManager.setLastRisk(riesgoInicial);
        preferencesManager.setLastScore(scoreInicial);
        preferencesManager.setFirstTime(false); // Ya realizó la evaluación

        Toast.makeText(this, "¡Evaluación completada con éxito!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(EvaluacionActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
