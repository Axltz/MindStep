package com.example.mindstep;

/**
 * Modelo para los mensajes del Chatbot Asistente Preventivo
 */
public class ChatMessage {
    private String texto;
    private boolean esBot;
    private String hora;
    private boolean sugiereEjercicio;
    private int tipoEjercicio; // 1: Respiración 4-7-8, 2: Anclaje 5-4-3-2-1, 3: Pausa activa

    public ChatMessage(String texto, boolean esBot, String hora) {
        this.texto = texto;
        this.esBot = esBot;
        this.hora = hora;
        this.sugiereEjercicio = false;
        this.tipoEjercicio = 0;
    }

    public ChatMessage(String texto, boolean esBot, String hora, boolean sugiereEjercicio, int tipoEjercicio) {
        this.texto = texto;
        this.esBot = esBot;
        this.hora = hora;
        this.sugiereEjercicio = sugiereEjercicio;
        this.tipoEjercicio = tipoEjercicio;
    }

    public String getTexto() {
        return texto;
    }

    public boolean isEsBot() {
        return esBot;
    }

    public String getHora() {
        return hora;
    }

    public boolean isSugiereEjercicio() {
        return sugiereEjercicio;
    }

    public int getTipoEjercicio() {
        return tipoEjercicio;
    }
}
