package com.example.mindstep;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Cliente de IA Generativa de Google Gemini para MindStep
 * Con System Prompt ético estricto especializado en salud mental preventiva,
 * empatía, TCC (Terapia Cognitivo-Conductual) y triaje seguro.
 */
public class GeminiAssistantService {

    // API Key de respaldo o configurable
    private static final String DEFAULT_API_KEY = ""; 
    private static final String MODEL_ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    private static final String SYSTEM_INSTRUCTION_ETICA =
            "Eres MindStep, un asistente empático y preventivo de salud mental para estudiantes universitarios. " +
            "Principios éticos obligatorios:\n" +
            "1. Claridad preventiva: No eres médico ni psicoterapeuta clínico y no emites diagnósticos psiquiátricos.\n" +
            "2. Enfoque: Usa Terapia Cognitivo-Conductual (TCC), validación emocional y resolución práctica de problemas.\n" +
            "3. Estilo: Responde de forma cálida, concisa (máximo 2 a 3 párrafos), cercana y en español de Latinoamérica.\n" +
            "4. Si el usuario sufre de ansiedad o pánico, guíalo con calma y sugiere pausas o respiración consciente.\n" +
            "5. Protocolo de seguridad: Si el usuario menciona autolesión o ideación suicida, responde con compasión absoluta y proporciona inmediatamente la Línea de la Vida (800 911 2000 en México) o servicios de emergencia locales.";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface OnRespuestaListener {
        void onRespuesta(String respuestaTexto, boolean sugiereEjercicio, int tipoEjercicio);
        void onError(String mensajeError);
    }

    public void enviarMensajeAI(String mensajeUsuario, String nombreUsuario, String riesgoActual, String apiKeyCustom, OnRespuestaListener listener) {
        String apiKey = (apiKeyCustom != null && !apiKeyCustom.trim().isEmpty()) ? apiKeyCustom : DEFAULT_API_KEY;

        if (apiKey.isEmpty()) {
            // Fallback al motor local ético si aún no han configurado la key
            mainHandler.post(() -> {
                AsistenteLogica.RespuestaBot local = AsistenteLogica.procesarMensajeUsuario(mensajeUsuario, nombreUsuario, riesgoActual, "");
                listener.onRespuesta(local.texto, local.incluyeEjercicio, local.tipoEjercicio);
            });
            return;
        }

        executor.execute(() -> {
            try {
                URL url = new URL(MODEL_ENDPOINT + apiKey);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setDoOutput(true);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(15000);

                // Construir Payload JSON para Gemini 1.5 Flash
                JSONObject root = new JSONObject();

                // Instrucción de Sistema Ética
                JSONObject systemInstruction = new JSONObject();
                JSONObject systemParts = new JSONObject();
                systemParts.put("text", SYSTEM_INSTRUCTION_ETICA + " El usuario se llama " + nombreUsuario + " y su nivel de riesgo actual es " + riesgoActual + ".");
                JSONArray systemPartsArray = new JSONArray();
                systemPartsArray.put(systemParts);
                systemInstruction.put("parts", systemPartsArray);
                root.put("system_instruction", systemInstruction);

                // Contenido del usuario
                JSONArray contentsArray = new JSONArray();
                JSONObject userContent = new JSONObject();
                userContent.put("role", "user");
                JSONArray userParts = new JSONArray();
                JSONObject userPartText = new JSONObject();
                userPartText.put("text", mensajeUsuario);
                userParts.put(userPartText);
                userContent.put("parts", userParts);
                contentsArray.put(userContent);
                root.put("contents", contentsArray);

                // Enviar Request
                byte[] input = root.toString().getBytes(StandardCharsets.UTF_8);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(input, 0, input.length);
                }

                int code = conn.getResponseCode();
                if (code == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }

                    JSONObject resJson = new JSONObject(response.toString());
                    JSONArray candidates = resJson.getJSONArray("candidates");
                    String botTexto = candidates.getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text");

                    // Evaluar si recomienda ejercicios
                    boolean incluyeEjercicio = false;
                    int tipo = 0;
                    String botLower = botTexto.toLowerCase();
                    if (botLower.contains("4-7-8") || botLower.contains("respir")) {
                        incluyeEjercicio = true;
                        tipo = 1;
                    } else if (botLower.contains("5-4-3-2-1") || botLower.contains("anclaje")) {
                        incluyeEjercicio = true;
                        tipo = 2;
                    } else if (botLower.contains("pomodoro") || botLower.contains("15 minutos")) {
                        incluyeEjercicio = true;
                        tipo = 4;
                    } else if (botLower.contains("pausa") || botLower.contains("estiramiento")) {
                        incluyeEjercicio = true;
                        tipo = 5;
                    }

                    boolean finalIncluyeEjercicio = incluyeEjercicio;
                    int finalTipo = tipo;
                    mainHandler.post(() -> listener.onRespuesta(botTexto, finalIncluyeEjercicio, finalTipo));
                } else {
                    // Fallback a motor local si hay problema de red o cuota
                    mainHandler.post(() -> {
                        AsistenteLogica.RespuestaBot local = AsistenteLogica.procesarMensajeUsuario(mensajeUsuario, nombreUsuario, riesgoActual, "");
                        listener.onRespuesta(local.texto, local.incluyeEjercicio, local.tipoEjercicio);
                    });
                }
            } catch (Exception e) {
                // Fallback seguro a motor local si está offline
                mainHandler.post(() -> {
                    AsistenteLogica.RespuestaBot local = AsistenteLogica.procesarMensajeUsuario(mensajeUsuario, nombreUsuario, riesgoActual, "");
                    listener.onRespuesta(local.texto, local.incluyeEjercicio, local.tipoEjercicio);
                });
            }
        });
    }
}
