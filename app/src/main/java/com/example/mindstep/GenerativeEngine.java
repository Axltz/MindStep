package com.example.mindstep;

import java.util.Locale;
import java.util.Random;

/**
 * Motor Autónomo de Triaje y Salud Mental TCC — MindStep v5.0
 * 100 % privado, offline, ultrarrápido y sin necesidad de red.
 * 
 * Árbol de decisiones adaptativo de Psicología Cognitivo-Conductual.
 */
public class GenerativeEngine {

    private static final Random random = new Random();

    public static class ResultadoAI {
        public String texto;
        public boolean sugiereEjercicio;
        public int tipoEjercicio;
        // 1: Respiración 4-7-8
        // 2: Anclaje 5-4-3-2-1
        // 3: Reencuadre Cognitivo
        // 4: Pomodoro & Micro-metas
        // 5: Pausa Somática
        // 6: Diario de Gratitud
        // 7: Escáner Corporal
        // 8: Visualización Positiva
    }

    public static ResultadoAI resolverMensajeInteligente(String mensajeUsuario, String nombreUsuario, String riesgoActual) {
        ResultadoAI res = new ResultadoAI();

        if (mensajeUsuario == null || mensajeUsuario.trim().isEmpty()) {
            res.texto = "Estoy aquí contigo, " + (nombreUsuario != null ? nombreUsuario : "amigo/a") + ". Platícame qué traes en mente o cómo te sientes hoy.";
            res.sugiereEjercicio = false;
            return res;
        }

        String t = mensajeUsuario.toLowerCase(Locale.ROOT).trim();
        String nombre = (nombreUsuario != null && !nombreUsuario.isEmpty()) ? nombreUsuario : "amigo/a";

        // ══════════════════════════════════════════════════════════════════════
        // 1. PROTOCOLO ÉTICO DE EMERGENCIA (Siempre prioritario)
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("suicid") || t.contains("matar") || t.contains("no quiero vivir")
                || t.contains("acabar con todo") || t.contains("quitarme la vida")
                || t.contains("hacerme daño") || t.contains("ya no quiero existir")) {
            res.texto = nombre + ", gracias por confiarme algo tan profundo. Lo que sientes importa " +
                    "y mereces apoyo humano especializado ahora mismo.\n\n" +
                    "🆘 Llama a la Línea de la Vida (México): 800 911 2000\n" +
                    "Disponible 24 horas, gratis y confidencial.\n\n" +
                    "También puedes acercarte hoy con un familiar, amigo de confianza o un profesional de salud. " +
                    "MindStep seguirá aquí cuando lo necesites.";
            res.sugiereEjercicio = false;
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 2. DISPARADORES DIRECTOS DE EJERCICIOS Y CHIPS
        // ══════════════════════════════════════════════════════════════════════
        // A) Respiración 4-7-8
        if (t.contains("respir") || t.contains("4-7-8") || t.contains("inhal")) {
            res.texto = "¡Excelente decisión, " + nombre + "! La técnica de Respiración Guiada 4-7-8 desacelera el ritmo cardiaco, oxigena tu cuerpo y equilibra tu sistema nervioso en menos de un minuto.\n\n👇 Presiona el botón verde aquí abajo para iniciar la sesión guiada.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 1; // Respiración 4-7-8
            return res;
        }

        // B) Anclaje Sensorial (5-4-3-2-1)
        if (t.contains("anclaj") || t.contains("5-4-3-2-1") || t.contains("sentidos") || t.contains("presente")) {
            res.texto = "Perfecto, " + nombre + ". La técnica de Anclaje Sensorial 5-4-3-2-1 interrumpe el bucle de rumiación y conecta tu atención de inmediato con tu entorno físico.\n\n👇 Pulsa abajo para iniciar la guía de Anclaje.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 2; // Anclaje Sensorial
            return res;
        }

        // C) Reencuadre Cognitivo TCC
        if (t.contains("reencuadre") || t.contains("pensamiento") || t.contains("culpa") || t.contains("autoexig")) {
            res.texto = "Excelente ejercicio, " + nombre + ". El Reencuadre Cognitivo nos permite separar las emociones intensas de los hechos reales para devolverte la paz.\n\n👇 Presiona el botón abajo para realizar los 3 filtros TCC.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 3; // Reencuadre Cognitivo
            return res;
        }

        // D) Pomodoro & Micro-metas
        if (t.contains("pomodoro") || t.contains("micro-meta") || t.contains("15 min") || t.contains("bloque") || t.contains("procrastin") || t.contains("tarea") || t.contains("examen") || t.contains("estudiar")) {
            res.texto = "La parálisis cognitiva se rompe reduciendo la exigencia, " + nombre + ". Vamos a aplicar la técnica de Micro-Metas de 15 minutos en un solo paso pequeño.\n\n👇 Haz clic abajo para activar tu enfoque.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 4; // Pomodoro
            return res;
        }

        // E) Pausa Somática
        if (t.contains("somátic") || t.contains("somatic") || t.contains("pausa") || t.contains("burnout") || t.contains("agotad") || t.contains("cansad") || t.contains("estira")) {
            res.texto = "Tu cuerpo ha acumulado tensión y necesita una pausa somática, " + nombre + ". Soltar hombros, mandíbula e hidratarse devuelve la energía vital.\n\n👇 Presiona abajo para realizar la Pausa Somática.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 5; // Pausa Somática
            return res;
        }

        // F) Diario de Gratitud
        if (t.contains("gratitud") || t.contains("diario") || t.contains("triste") || t.contains("solo") || t.contains("sola") || t.contains("deprim")) {
            res.texto = "Lamento que pases por un momento pesado, " + nombre + ". El Diario de Gratitud reconfigura la atención hacia los pequeños detalles positivos del día.\n\n👇 Haz clic abajo para abrir tu Diario de Gratitud.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 6; // Diario de Gratitud
            return res;
        }

        // G) Escáner Corporal
        if (t.contains("escáner") || t.contains("escaner") || t.contains("dormir") || t.contains("insomnio") || t.contains("sueño") || t.contains("sueno") || t.contains("desvelo")) {
            res.texto = "El descanso es esencial para tu salud, " + nombre + ". El Escáner Corporal relaja cada grupo muscular de pies a cabeza para inducir un sueño reparador.\n\n👇 Presiona abajo para iniciar el Escáner Corporal.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 7; // Escáner Corporal
            return res;
        }

        // H) Visualización Positiva
        if (t.contains("visualiz") || t.contains("motiva") || t.contains("confianza") || t.contains("miedo") || t.contains("meta")) {
            res.texto = "Visualizar el éxito activa las mismas conexiones neuronales que lograrlo en la realidad, " + nombre + ".\n\n👇 Toca abajo para realizar la Visualización Guiada.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 8; // Visualización Positiva
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 3. ANSIEDAD AGUDA Y SOBRECARGA
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("ansiedad") || t.contains("panico") || t.contains("pánico")
                || t.contains("taquicardia") || t.contains("falta el aire") || t.contains("ahogo")
                || t.contains("palpitaciones") || t.contains("angustia") || t.contains("desespera")
                || t.contains("pecho apretado")) {
            res.texto = "Respira conmigo, " + nombre + ". Estás a salvo. Ese pico de angustia es incómodo pero pasará rápido.\n\n👇 Pulsa abajo para iniciar la Respiración Guiada 4-7-8.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 1;
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 4. SOBREPENSAR Y FUTURO
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("futuro") || t.contains("incertidumbre") || t.contains("sobrepensar")
                || t.contains("sobrepienso") || t.contains("rumia") || t.contains("que va a pasar")
                || t.contains("miedo a fallar")) {
            res.texto = "Es natural que la mente intente viajar al futuro cuando siente incertidumbre, " + nombre + ". La técnica de Anclaje 5-4-3-2-1 te regresa al aquí y ahora.\n\n👇 Pulsa abajo para iniciar la guía.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 2;
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 5. CONSULTAS SOBRE LA APP Y LA IA
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("ia") || t.contains("inteligencia") || t.contains("bot") || t.contains("gemini")) {
            res.texto = "¡Sí, " + nombre + "! Funciono mediante un motor conversacional adaptativo de Psicología Cognitivo-Conductual (TCC), diseñado para acompañarte 100% de manera privada y offline en tu dispositivo. ¿Qué te gustaría practicar hoy?";
            res.sugiereEjercicio = false;
            return res;
        }

        if (t.contains("quien eres") || t.contains("quién eres") || t.contains("que eres") || t.contains("qué eres") || t.contains("como te llamas")) {
            res.texto = "Soy MindStep, tu asistente de salud mental y prevención de burnout desarrollado por Axel. Estoy aquí para escucharte sin juicios y guiarte con ejercicios prácticos.";
            res.sugiereEjercicio = false;
            return res;
        }

        if (t.contains("como funcionas") || t.contains("cómo funcionas") || t.contains("que puedes hacer")) {
            res.texto = "Puedo ayudarte de 3 formas:\n\n1. 🗣️ Escuchar tus emociones y desahogarte libremente.\n2. 🧘‍♂️ Guiarte en 8 técnicas de regulación emocional (Respiración 4-7-8, Anclaje Sensorial, Pomodoro, etc.).\n3. 📊 Analizar tus patrones de descanso y estrés.";
            res.sugiereEjercicio = false;
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 6. SALUDOS Y AGRADECIMIENTOS
        // ══════════════════════════════════════════════════════════════════════
        if (t.equals("hola") || t.equals("buenas") || t.equals("buen dia") || t.equals("buenos dias") || t.equals("hey")) {
            res.texto = "¡Hola, " + nombre + "! Es un gusto saludarte. Estoy aquí listo para escucharte o acompañarte con alguna técnica. ¿Cómo te sientes hoy?";
            res.sugiereEjercicio = false;
            return res;
        }

        if (t.contains("como estas") || t.contains("cómo estás") || t.contains("que tal")) {
            res.texto = "¡Muy bien y listo para escucharte, " + nombre + "! Platícame cómo va tu día o qué tienes en mente.";
            res.sugiereEjercicio = false;
            return res;
        }

        if (t.contains("gracias") || t.contains("me ayudo") || t.contains("me ayudó") || t.contains("grac")) {
            res.texto = "¡Me alegra muchísimo haberte sido de ayuda, " + nombre + "! El mérito es tuyo por darte este tiempo para cuidar de ti. ¡Aquí estaré cuando me necesites!";
            res.sugiereEjercicio = false;
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 7. RESPUESTA DE CONVERSACIÓN ABIERTA INTELIGENTE
        // ══════════════════════════════════════════════════════════════════════
        String[] abiertas = {
                "Te escucho con atención, " + nombre + ". Platícame un poco más sobre ello o dime si prefieres que hagamos alguna técnica de regulación emocional.",
                "Entiendo lo que mencionas, " + nombre + ". A veces el solo hecho de poner en palabras lo que nos pesa en la cabeza nos devuelve la tranquilidad.",
                "Tiene sentido lo que compartes, " + nombre + ". Si pudieras enfocarte en una sola cosa para sentirte mejor en este momento, ¿cuál sería?"
        };
        res.texto = elegir(abiertas);
        res.sugiereEjercicio = false;
        return res;
    }

    private static String elegir(String[] opciones) {
        if (opciones == null || opciones.length == 0) return "";
        return opciones[random.nextInt(opciones.length)];
    }
}
