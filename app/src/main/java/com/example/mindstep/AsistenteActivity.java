package com.example.mindstep;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Pantalla 7: Asistente de Apoyo Preventivo (Chatbot Conversacional Real)
 * Módulo de triaje interactivo basado en TCC para acompañar al usuario,
 * escuchar desahogos y proveer técnicas inmediatas de regulación.
 */
public class AsistenteActivity extends AppCompatActivity implements ChatAdapter.OnEjercicioClickListener {

    private ImageButton btnBackAsistente;
    private RecyclerView rvChatMensajes;
    private EditText etMensajeUsuario;
    private ImageButton btnEnviarMensaje;

    private MaterialButton chipRespiracion;
    private MaterialButton chipAnsiedad;
    private MaterialButton chipExamenes;
    private MaterialButton chipSueno;
    private MaterialButton chipBurnout;

    private ChatAdapter chatAdapter;
    private List<ChatMessage> listaMensajes;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistente);

        preferencesManager = new PreferencesManager(this);

        initViews();
        setupRecyclerView();
        iniciarConversacion();
        setupListeners();
    }

    private void initViews() {
        btnBackAsistente = findViewById(R.id.btnBackAsistente);
        rvChatMensajes = findViewById(R.id.rvChatMensajes);
        etMensajeUsuario = findViewById(R.id.etMensajeUsuario);
        btnEnviarMensaje = findViewById(R.id.btnEnviarMensaje);

        chipRespiracion = findViewById(R.id.chipRespiracion);
        chipAnsiedad = findViewById(R.id.chipAnsiedad);
        chipExamenes = findViewById(R.id.chipExamenes);
        chipSueno = findViewById(R.id.chipSueno);
        chipBurnout = findViewById(R.id.chipBurnout);
    }

    private void setupRecyclerView() {
        listaMensajes = new ArrayList<>();
        chatAdapter = new ChatAdapter(listaMensajes, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvChatMensajes.setLayoutManager(layoutManager);
        rvChatMensajes.setAdapter(chatAdapter);
    }

    private void iniciarConversacion() {
        String nombre = preferencesManager.getUserName();
        String riesgo = preferencesManager.getLastRisk();
        String horaActual = obtenerHoraActual();

        String mensajeBienvenida;
        if ("Elevado".equalsIgnoreCase(riesgo)) {
            mensajeBienvenida = "Hola " + nombre + ". He detectado que tus últimos registros muestran un nivel de estrés elevado y pocas horas de sueño.\n\nEstoy aquí para escucharte y ayudarte a descomprimir la tensión. ¿Cómo te sientes en este instante?";
        } else if ("Moderado".equalsIgnoreCase(riesgo)) {
            mensajeBienvenida = "Hola " + nombre + ". Veo que tu ritmo diario ha estado algo agitado. ¿Cuál es la causa principal de esa presión hoy?";
        } else {
            mensajeBienvenida = "¡Hola " + nombre + "! Tus hábitos se mantienen en equilibrio hoy. Recuerda que puedes escribirme para desahogarte, planear tus descansos o hacer ejercicios guiados de respiración.";
        }

        listaMensajes.add(new ChatMessage(mensajeBienvenida, true, horaActual));
        chatAdapter.notifyItemInserted(listaMensajes.size() - 1);
    }

    private void setupListeners() {
        btnBackAsistente.setOnClickListener(v -> finish());

        // Enviar al pulsar el botón
        btnEnviarMensaje.setOnClickListener(v -> enviarMensajeUsuario());

        // Enviar al pulsar 'Enter / Send' en el teclado
        etMensajeUsuario.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                enviarMensajeUsuario();
                return true;
            }
            return false;
        });

        // Chips de respuesta rápida
        chipRespiracion.setOnClickListener(v -> responderConChip("Quiero hacer el ejercicio de Respiración 4-7-8"));
        chipAnsiedad.setOnClickListener(v -> responderConChip("Siento mucha ansiedad y el pecho apretado"));
        chipExamenes.setOnClickListener(v -> responderConChip("Tengo demasiada sobrecarga de exámenes y tareas"));
        chipSueno.setOnClickListener(v -> responderConChip("No puedo dormir y me siento desvelado"));
        chipBurnout.setOnClickListener(v -> responderConChip("Siento agotamiento total y burnout escolar"));
    }

    private void responderConChip(String texto) {
        etMensajeUsuario.setText(texto);
        enviarMensajeUsuario();
    }

    private void enviarMensajeUsuario() {
        String texto = etMensajeUsuario.getText().toString().trim();
        if (TextUtils.isEmpty(texto)) {
            return;
        }

        String hora = obtenerHoraActual();
        listaMensajes.add(new ChatMessage(texto, false, hora));
        chatAdapter.notifyItemInserted(listaMensajes.size() - 1);
        rvChatMensajes.smoothScrollToPosition(listaMensajes.size() - 1);

        etMensajeUsuario.setText("");

        // Simular tiempo de respuesta humano (600ms)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            String nombre = preferencesManager.getUserName();
            String riesgo = preferencesManager.getLastRisk();

            AsistenteLogica.RespuestaBot respuesta = AsistenteLogica.procesarMensajeUsuario(texto, nombre, riesgo);

            listaMensajes.add(new ChatMessage(
                    respuesta.texto,
                    true,
                    obtenerHoraActual(),
                    respuesta.incluyeEjercicio,
                    respuesta.tipoEjercicio
            ));
            chatAdapter.notifyItemInserted(listaMensajes.size() - 1);
            rvChatMensajes.smoothScrollToPosition(listaMensajes.size() - 1);
        }, 600);
    }

    @Override
    public void onEjercicioClick(int tipoEjercicio) {
        mostrarDialogoEjercicio(tipoEjercicio);
    }

    private void mostrarDialogoEjercicio(int tipo) {
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

        tvTitulo.setText(tipo == 1 ? "Respiración Guiada 4-7-8" : "Pausa de Regulación Sensorial");

        CountDownTimer timer = new CountDownTimer(38000, 1000) {
            int fase = 0; // 0: Inhala, 1: Sostén, 2: Exhala
            int segundosRestantes = 4;

            @Override
            public void onTick(long millisUntilFinished) {
                tvSegundos.setText(segundosRestantes + "s");

                if (fase == 0) {
                    tvInstruccion.setText("Inhala suavemente por la nariz");
                    tvInstruccion.setTextColor(Color.parseColor("#2563EB"));
                } else if (fase == 1) {
                    tvInstruccion.setText("Sostén el aire en tus pulmones");
                    tvInstruccion.setTextColor(Color.parseColor("#F59E0B"));
                } else {
                    tvInstruccion.setText("Exhala lentamente por la boca");
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
                tvInstruccion.setText("¡Excelente trabajo! Has completado el ciclo.");
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

    private String obtenerHoraActual() {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }
}
