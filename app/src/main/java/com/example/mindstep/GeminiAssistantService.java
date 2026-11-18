package com.example.mindstep;

import android.os.Handler;
import android.os.Looper;

/**
 * Servicio Conversacional de Apoyo Preventivo — MindStep v4.0
 * Responde de manera inmediata, empática y 100% privada sin depender de red.
 */
public class GeminiAssistantService {

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface OnRespuestaListener {
        void onRespuesta(String respuestaTexto, boolean sugiereEjercicio, int tipoEjercicio);
        void onError(String mensajeError);
    }

    public void enviarMensajeAI(String mensajeUsuario, String nombreUsuario, String riesgoActual, String apiKeyCustom, OnRespuestaListener listener) {
        // Procesar el mensaje directamente con el motor autónomo de árbol de decisiones TCC
        GenerativeEngine.ResultadoAI resultado = GenerativeEngine.resolverMensajeInteligente(mensajeUsuario, nombreUsuario, riesgoActual);

        mainHandler.postDelayed(() -> {
            listener.onRespuesta(resultado.texto, resultado.sugiereEjercicio, resultado.tipoEjercicio);
        }, 150); // Pequeño delay de 150ms para simular fluidez conversacional
    }
}
