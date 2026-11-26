package com.example.mindstep;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
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
 * Pantalla 7: Asistente de Apoyo Preventivo (Chatbot Adaptativo con IA Generativa)
 * Maneja diálogo con memoria de contexto, protocolo ético y 8 ejercicios TCC.
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
    private GeminiAssistantService geminiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistente);

        preferencesManager = new PreferencesManager(this);
        geminiService = new GeminiAssistantService();

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
            mensajeBienvenida = "Hola " + nombre + ". Tus registros indican que has estado bajo un nivel de tensión alto últimamente.\n\nEstoy aquí para escucharte sin prisas ni juicios. Puedes desahogarte libremente o pedirme una técnica para calmarte. ¿Cómo te sientes en este instante?";
        } else if ("Moderado".equalsIgnoreCase(riesgo)) {
            mensajeBienvenida = "Hola " + nombre + ". Veo que tu ritmo ha estado algo agitado. ¿Cuál sientes que es la causa principal de esa presión hoy?";
        } else {
            mensajeBienvenida = "¡Hola " + nombre + "! Es un gusto saludarte. Recuerda que puedes platicar conmigo sobre cualquier cosa: dudas, pendientes, emociones o hacer algún ejercicio guiado. ¿Qué traes en mente?";
        }

        listaMensajes.add(new ChatMessage(mensajeBienvenida, true, horaActual));
        chatAdapter.notifyItemInserted(listaMensajes.size() - 1);
    }

    private void setupListeners() {
        btnBackAsistente.setOnClickListener(v -> finish());
        btnEnviarMensaje.setOnClickListener(v -> enviarMensajeUsuario());

        etMensajeUsuario.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                enviarMensajeUsuario();
                return true;
            }
            return false;
        });

        chipRespiracion.setOnClickListener(v -> responderConChip("Quiero hacer el ejercicio de Respiración 4-7-8"));
        chipAnsiedad.setOnClickListener(v -> responderConChip("Tengo pensamientos sobre el futuro que me dan ansiedad"));
        chipExamenes.setOnClickListener(v -> responderConChip("Tengo sobrecarga de exámenes y tareas pendientes"));
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

        String nombre = preferencesManager.getUserName();
        String riesgo = preferencesManager.getLastRisk();
        String apiKey = preferencesManager.getGeminiApiKey();

        geminiService.enviarMensajeAI(texto, nombre, riesgo, apiKey, new GeminiAssistantService.OnRespuestaListener() {
            @Override
            public void onRespuesta(String respuestaTexto, boolean sugiereEjercicio, int tipoEjercicio) {
                listaMensajes.add(new ChatMessage(
                        respuestaTexto,
                        true,
                        obtenerHoraActual(),
                        sugiereEjercicio,
                        tipoEjercicio
                ));
                chatAdapter.notifyItemInserted(listaMensajes.size() - 1);
                rvChatMensajes.smoothScrollToPosition(listaMensajes.size() - 1);
            }

            @Override
            public void onError(String mensajeError) {
                GenerativeEngine.ResultadoAI local = GenerativeEngine.resolverLocalSiExiste(texto, nombre, riesgo, new ArrayList<>());
                if (local == null) {
                    local = GenerativeEngine.generarFallbackDesconectado(nombre, riesgo);
                }
                listaMensajes.add(new ChatMessage(
                        local.texto,
                        true,
                        obtenerHoraActual(),
                        local.sugiereEjercicio,
                        local.tipoEjercicio
                ));
                chatAdapter.notifyItemInserted(listaMensajes.size() - 1);
                rvChatMensajes.smoothScrollToPosition(listaMensajes.size() - 1);
            }
        });
    }

    @Override
    public void onEjercicioClick(int tipoEjercicio) {
        switch (tipoEjercicio) {
            case 1:
                mostrarDialogoRespiracion();
                break;
            case 2:
                mostrarDialogoAnclajeSensorial();
                break;
            case 3:
                mostrarDialogoReencuadreCognitivo();
                break;
            case 4:
                mostrarDialogoPomodoro();
                break;
            case 5:
                mostrarDialogoPausaSomatica();
                break;
            case 6:
                mostrarDialogoDiarioGratitud();
                break;
            case 7:
                mostrarDialogoEscanerCorporal();
                break;
            case 8:
                mostrarDialogoVisualizacionPositiva();
                break;
            default:
                mostrarDialogoRespiracion();
                break;
        }
    }

    // 1. Respiración Guiada 4-7-8
    private void mostrarDialogoRespiracion() {
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

        tvTitulo.setText("Respiración Guiada 4-7-8");

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
            Toast.makeText(this, "¡Respiración completada!", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    // 2. Anclaje Sensorial (5-4-3-2-1)
    private void mostrarDialogoAnclajeSensorial() {
        new AlertDialog.Builder(this)
                .setTitle("🧘‍♂️ Anclaje Sensorial (5-4-3-2-1)")
                .setMessage("Tómate un minuto en tu entorno real para identificar conscientemente:\n\n" +
                        "👀 5 cosas que puedas VER\n" +
                        "✋ 4 cosas que puedas TOCAR (tu ropa, la mesa)\n" +
                        "👂 3 sonidos que puedas OÍR en el fondo\n" +
                        "👃 2 aromas que puedas OLER\n" +
                        "👅 1 sabor en tu boca\n\n" +
                        "Esta técnica interrumpe el bucle de rumiación y regresa tu cerebro al presente.")
                .setPositiveButton("Me siento más anclado", (d, w) -> {
                    Toast.makeText(this, "¡Buen trabajo reconectando con el presente!", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    // 3. Reencuadre Cognitivo (TCC)
    private void mostrarDialogoReencuadreCognitivo() {
        new AlertDialog.Builder(this)
                .setTitle("🔄 Reencuadre Cognitivo TCC")
                .setMessage("Ante un pensamiento que te angustia, pásalo por estos 3 filtros:\n\n" +
                        "1. ¿Qué evidencia real tengo de que esto va a suceder?\n" +
                        "2. ¿Qué es lo peor que podría pasar y cómo lo resolvería si ocurriera?\n" +
                        "3. Si un amigo muy querido estuviera en mi lugar, ¿qué le diría?\n\n" +
                        "Separar las emociones de los hechos te devuelve el control.")
                .setPositiveButton("Hecho, lo tengo más claro", (d, w) -> {
                    Toast.makeText(this, "¡Pensamiento reestructurado con éxito!", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    // 4. Pomodoro y Micro-Metas (15 minutos)
    private void mostrarDialogoPomodoro() {
        new AlertDialog.Builder(this)
                .setTitle("⏱️ Técnica de Micro-Metas (15 Min)")
                .setMessage("El secreto para romper el bloqueo mental es reducir la exigencia:\n\n" +
                        "1. Elige una sola tarea pequeña (ej. escribir media página o leer 5 hojas).\n" +
                        "2. Pon un temporizador de 15 minutos.\n" +
                        "3. Silencia notificaciones y no pienses en nada más.\n\n" +
                        "Al terminar los 15 minutos, tendrás permiso de tomar 5 minutos de descanso libre.")
                .setPositiveButton("Iniciar ahora", (d, w) -> {
                    Toast.makeText(this, "¡Enfoque activado! Mucho éxito.", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    // 5. Pausa Somática y Alivio Muscular
    private void mostrarDialogoPausaSomatica() {
        new AlertDialog.Builder(this)
                .setTitle("💧 Pausa Somática (3 Minutos)")
                .setMessage("Tu cuerpo acumula tensión inconscientemente:\n\n" +
                        "• Suelta la mandíbula (separa los dientes).\n" +
                        "• Baja los hombros lejos de las orejas.\n" +
                        "• Gira la cabeza suavemente en círculos lentos.\n" +
                        "• Bebe un vaso con agua fresca.\n" +
                        "• Mira por la ventana a un punto lejano por 20 segundos.")
                .setPositiveButton("Pausa completada", (d, w) -> {
                    Toast.makeText(this, "Cuerpo relajado e hidratado.", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    // 6. Diario de Gratitud
    private void mostrarDialogoDiarioGratitud() {
        new AlertDialog.Builder(this)
                .setTitle("📒 Diario de Gratitud Expreso")
                .setMessage("Identifica mentalmente o anota 3 cosas por las que estés agradecido/a hoy:\n\n" +
                        "1. Algo simple que disfrutaste (un café, una canción, un momento tranquilo).\n" +
                        "2. Una persona que haya hecho tu día más llevadero.\n" +
                        "3. Un esfuerzo propio que reconoces haber hecho hoy.\n\n" +
                        "La gratitud comprobada reduce el cortisol y reconfigura la atención hacia lo que sí funciona.")
                .setPositiveButton("Anotadas en mi mente", (d, w) -> {
                    Toast.makeText(this, "¡Reconexión positiva completada!", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    // 7. Escáner Corporal para Relajación
    private void mostrarDialogoEscanerCorporal() {
        new AlertDialog.Builder(this)
                .setTitle("🌙 Escáner Corporal Guiado")
                .setMessage("Cierra los ojos y lleva tu atención lentamente a cada zona de tu cuerpo:\n\n" +
                        "1. Siente la planta de tus pies y suéltalos completamente.\n" +
                        "2. Nota tus pantorrillas y muslos, déjalos caer pesados.\n" +
                        "3. Suelta el abdomen, permite que se expanda con tu respiración.\n" +
                        "4. Despega la lengua del paladar y relaja el entrecejo.\n\n" +
                        "Permanece así 1 minuto respirando a tu ritmo natural.")
                .setPositiveButton("Cuerpo descansado", (d, w) -> {
                    Toast.makeText(this, "Preparado para descansar mejor.", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    // 8. Visualización Positiva
    private void mostrarDialogoVisualizacionPositiva() {
        new AlertDialog.Builder(this)
                .setTitle("🌟 Visualización Guiada")
                .setMessage("Imagina por un momento un día donde lograste superar el reto que tienes enfrente:\n\n" +
                        "• ¿Cómo te sientes físicamente al terminarlo?\n" +
                        "• ¿Qué te dices a ti mismo/a en ese momento de alivio?\n" +
                        "• ¿Quién celebra contigo este avance?\n\n" +
                        "Visualizar el éxito activa las mismas regiones neuronales que vivirlo y disminuye la ansiedad de ejecución.")
                .setPositiveButton("Visualizado con éxito", (d, w) -> {
                    Toast.makeText(this, "¡Confianza renovada!", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private String obtenerHoraActual() {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }
}
