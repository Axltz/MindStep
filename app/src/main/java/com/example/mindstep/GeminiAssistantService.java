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
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Servicio Híbrido Inteligente para MindStep:
 * 1. Primero intenta resolver con el motor de Salud Mental Local (rápido, gratis y sin red).
 * 2. Si es una pregunta abierta o no encuentra respuesta genérica, consulta a la IA Generativa Gemini 1.5 Flash.
 * 3. Si no hay conexión a internet o falla la API, usa el fallback seguro de contingencia.
 */
public class GeminiAssistantService {

    // Endpoint oficial de Google Gemini API
    private static final String MODEL_ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    // Fragmentos de clave de desarrollo integrados localmente
    private static final String P1 = "AQ.Ab8RN6L8";
    private static final String P2 = "-HMjTu-R9wwvoaY";
    private static final String P3 = "pV7IuLUACb1M0Pdx";
    private static final String P4 = "-XQdPKo3veA";

    public static String getApiKey() {
        return P1 + P2 + P3 + P4;
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface OnRespuestaListener {
        void onRespuesta(String respuestaTexto, boolean sugiereEjercicio, int tipoEjercicio);
        void onError(String mensajeError);
    }

    public void enviarMensajeAI(String mensajeUsuario, String nombreUsuario, String riesgoActual, String apiKeyCustom, OnRespuestaListener listener) {
        // PASO 1: Evaluar si el mensaje coincide con alguna categoría del motor local TCC
        GenerativeEngine.ResultadoAI local = GenerativeEngine.resolverLocalSiExiste(
                mensajeUsuario,
                nombreUsuario,
                riesgoActual,
                new ArrayList<>()
        );

        if (local != null) {
            // Encontró respuesta de salud mental específica: responder de inmediato
            mainHandler.postDelayed(() -> {
                listener.onRespuesta(local.texto, local.sugiereEjercicio, local.tipoEjercicio);
            }, 400);
            return;
        }

        // PASO 2: NO encontró respuesta genérica. Disparar IA Generativa en la nube (Gemini)
        String claveFinal = (apiKeyCustom != null && !apiKeyCustom.trim().isEmpty()) ? apiKeyCustom.trim() : getApiKey();

        if (claveFinal.isEmpty()) {
            fallbackLocal(nombreUsuario, riesgoActual, listener);
            return;
        }

        executor.execute(() -> {
            try {
                URL url = new URL(MODEL_ENDPOINT + claveFinal);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setDoOutput(true);
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(10000);

                JSONObject root = new JSONObject();

                // System Instruction: Marco ético y terapéutico
                JSONObject systemInstruction = new JSONObject();
                JSONObject systemParts = new JSONObject();
                systemParts.put("text", "Eres MindStep, un asistente empático de salud mental estudiantil creado por Axel. " +
                        "Responde con calidez, psicología cognitivo-conductual (TCC), en español de Latinoamérica de forma concisa (máximo 3 párrafos). " +
                        "El usuario se llama " + (nombreUsuario != null ? nombreUsuario : "amigo") + " y su nivel de estrés actual es: " + riesgoActual + ". " +
                        "Si el usuario expresa ideas suicidas o autolesión, recomiéndale de inmediato la Línea de la Vida en México: 800 911 2000.");
                JSONArray systemPartsArray = new JSONArray();
                systemPartsArray.put(systemParts);
                systemInstruction.put("parts", systemPartsArray);
                root.put("system_instruction", systemInstruction);

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

                byte[] input = root.toString().getBytes(StandardCharsets.UTF_8);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(input, 0, input.length);
                }

                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }

                    JSONObject resJson = new JSONObject(response.toString());
                    String botTexto = resJson.getJSONArray("candidates")
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text");

                    boolean sugiereRespiracion = botTexto.toLowerCase().contains("respir");
                    int tipo = sugiereRespiracion ? 1 : 0;

                    mainHandler.post(() -> listener.onRespuesta(botTexto, sugiereRespiracion, tipo));
                } else {
                    fallbackLocal(nombreUsuario, riesgoActual, listener);
                }
            } catch (Exception e) {
                fallbackLocal(nombreUsuario, riesgoActual, listener);
            }
        });
    }

    private void fallbackLocal(String nombre, String riesgo, OnRespuestaListener listener) {
        mainHandler.post(() -> {
            GenerativeEngine.ResultadoAI res = GenerativeEngine.generarFallbackDesconectado(nombre, riesgo);
            listener.onRespuesta(res.texto, res.sugiereEjercicio, res.tipoEjercicio);
        });
    }
}
