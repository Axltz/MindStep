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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Servicio Inteligente de Asistente IA para MindStep
 * Funciona de forma 100% autónoma, gratuita y sin configuración técnica
 * utilizando el motor GenerativeEngine local, con opción transparente de Gemini si hay API Key.
 */
public class GeminiAssistantService {

    private static final String MODEL_ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface OnRespuestaListener {
        void onRespuesta(String respuestaTexto, boolean sugiereEjercicio, int tipoEjercicio);
        void onError(String mensajeError);
    }

    public void enviarMensajeAI(String mensajeUsuario, String nombreUsuario, String riesgoActual, String apiKeyCustom, OnRespuestaListener listener) {
        // Si no hay API key configurada por el usuario (la mayoría de personas normales),
        // usa automáticamente el motor de IA generativa autónomo local sin costo y sin límites
        if (apiKeyCustom == null || apiKeyCustom.trim().isEmpty()) {
            mainHandler.postDelayed(() -> {
                GenerativeEngine.ResultadoAI res = GenerativeEngine.generarRespuesta(
                        mensajeUsuario,
                        nombreUsuario,
                        riesgoActual,
                        new ArrayList<>()
                );
                listener.onRespuesta(res.texto, res.sugiereEjercicio, res.tipoEjercicio);
            }, 600); // Pequeña latencia natural de pensamiento
            return;
        }

        // Si el usuario configuró una API Key, consulta a los servidores de Gemini
        executor.execute(() -> {
            try {
                URL url = new URL(MODEL_ENDPOINT + apiKeyCustom);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setDoOutput(true);
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(10000);

                JSONObject root = new JSONObject();

                JSONObject systemInstruction = new JSONObject();
                JSONObject systemParts = new JSONObject();
                systemParts.put("text", "Eres MindStep, asistente empático de salud mental estudiantil. Responde con calidez y TCC en español de Latinoamérica.");
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

                    boolean incluyeEjercicio = botTexto.toLowerCase().contains("respir") || botTexto.toLowerCase().contains("4-7-8");
                    mainHandler.post(() -> listener.onRespuesta(botTexto, incluyeEjercicio, 1));
                } else {
                    fallbackLocal(mensajeUsuario, nombreUsuario, riesgoActual, listener);
                }
            } catch (Exception e) {
                fallbackLocal(mensajeUsuario, nombreUsuario, riesgoActual, listener);
            }
        });
    }

    private void fallbackLocal(String mensaje, String nombre, String riesgo, OnRespuestaListener listener) {
        mainHandler.post(() -> {
            GenerativeEngine.ResultadoAI res = GenerativeEngine.generarRespuesta(
                    mensaje,
                    nombre,
                    riesgo,
                    new ArrayList<>()
            );
            listener.onRespuesta(res.texto, res.sugiereEjercicio, res.tipoEjercicio);
        });
    }
}
