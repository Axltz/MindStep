package com.example.mindstep;

import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Motor Autónomo de Triaje y Salud Mental TCC — MindStep v4.0
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
        // 2. PREGUNTAS SOBRE IA / MINDSTEP / FUNCIONAMIENTO / CREADOR
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("ia") || t.contains("inteligencia") || t.contains("bot") || t.contains("gemini")) {
            res.texto = "¡Sí, " + nombre + "! Funciono mediante un motor conversacional adaptativo de Psicología Cognitivo-Conductual (TCC), diseñado para acompañarte 100% de manera privada y offline en tu dispositivo. Puedes platicar sobre estrés, hábitos, dudas escolares o pedirme ejercicios guiados.";
            res.sugiereEjercicio = false;
            return res;
        }

        if (t.contains("quien eres") || t.contains("quién eres") || t.contains("que eres") || t.contains("qué eres") || t.contains("como te llamas")) {
            res.texto = "Soy MindStep, tu asistente de salud mental y prevención de burnout desarrollado por Axel. Mi objetivo es escucharte sin juicios, ayudarte a regular tu tensión y acompañarte a mantener hábitos saludables.";
            res.sugiereEjercicio = false;
            return res;
        }

        if (t.contains("como funcionas") || t.contains("cómo funcionas") || t.contains("que puedes hacer")) {
            res.texto = "Puedo ayudarte de 3 formas:\n\n" +
                    "1. 🗣️ Escuchar tus emociones y ayudarte a desahogarte libremente.\n" +
                    "2. 🧘‍♂️ Guiarte con 8 técnicas de regulación emocional (Respiración 4-7-8, Anclaje Sensorial, Pomodoro, Diario de Gratitud, etc.).\n" +
                    "3. 📊 Analizar patrones entre tu sueño, estrés y actividades diarias.";
            res.sugiereEjercicio = false;
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 3. ANSIEDAD AGUDA / ATAQUES DE PÁNICO / TAQUICARDIA
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("ansiedad") || t.contains("panico") || t.contains("pánico")
                || t.contains("taquicardia") || t.contains("falta el aire") || t.contains("ahogo")
                || t.contains("palpitaciones") || t.contains("angustia") || t.contains("desespera")
                || t.contains("pecho apretado")) {
            String[] intro = {
                    "Respira conmigo, " + nombre + ". Estás a salvo. Lo que sientes es una alarma corporal activada por el estrés, no un peligro real.",
                    "Estoy aquí contigo, " + nombre + ". Ese pico de angustia es incómodo pero pasará. Tu sistema nervioso puede calmarse con una técnica rápida."
            };
            res.texto = elegir(intro) + "\n\nCuando hiperventilamos baja el CO₂ y el cerebro cree estar en peligro. Exhalar el doble del tiempo activa el sistema parasimpático.\n\n👇 Pulsa abajo para la Respiración 4-7-8 guiada.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 1; // Respiración 4-7-8
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 4. FUTURO / INCERTIDUMBRE / RUMIACIÓN / SOBREPENSAR
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("futuro") || t.contains("incertidumbre") || t.contains("sobrepensar")
                || t.contains("sobrepienso") || t.contains("rumia") || t.contains("que va a pasar")
                || t.contains("miedo a fallar") || t.contains("anticipatoria") || t.contains("catastro")) {
            String[] intro = {
                    "Es muy natural que la mente viaje al futuro cuando siente incertidumbre, " + nombre + ". En TCC esto se llama 'ansiedad anticipatoria'.",
                    "El sobrepensamiento suele disfrazarse de preparación, pero solo agota tu energía, " + nombre + "."
            };
            res.texto = elegir(intro) + "\n\nEl 85 % de las cosas que tememos jamás ocurren tal cual las imaginas. La técnica de Anclaje 5-4-3-2-1 te ayuda a regresar al momento presente.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 2; // Anclaje Sensorial
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 5. CUESTIONAMIENTO DE PENSAMIENTOS / CULPA / AUTOEXIGENCIA
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("pensamiento") || t.contains("culpa") || t.contains("fracaso")
                || t.contains("inutil") || t.contains("inútil") || t.contains("autoexig")
                || t.contains("perfeccion") || t.contains("perfección") || t.contains("no soy suficiente")) {
            res.texto = "Separar las emociones de los hechos reales te devuelve el control, " + nombre + ".\n\nPasemos ese pensamiento por 3 filtros TCC:\n1. ¿Qué evidencia real tengo de esto?\n2. ¿Qué es lo peor que pasaría y cómo lo resolverías?\n3. ¿Qué le dirías a un amigo que sintiera esto mismo?";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 3; // Reencuadre Cognitivo
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 6. BLOQUEO ACADÉMICO / TAREAS / EXÁMENES / PROCRASTINACIÓN
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("examen") || t.contains("tarea") || t.contains("proyecto") || t.contains("estudiar")
                || t.contains("procrastin") || t.contains("bloque") || t.contains("no puedo avanzar")
                || t.contains("escuela") || t.contains("universidad") || t.contains("carrera")) {
            res.texto = "La procrastinación casi nunca es flojera, " + nombre + "; es un mecanismo de defensa ante la saturación.\n\nLa clave es reducir la exigencia: trabaja en micro-metas de 15 minutos enfocados sin revisar el teléfono.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 4; // Pomodoro
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 7. BURNOUT / AGOTAMIENTO FÍSICO Y MENTAL
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("burnout") || t.contains("agotad") || t.contains("cansad") || t.contains("hart")
                || t.contains("sin fuerza") || t.contains("sin energia") || t.contains("no doy mas")
                || t.contains("quemad")) {
            res.texto = "Ese cansancio acumulado no se quita solo durmiendo un rato; tu cuerpo te pide límites, " + nombre + ".\n\nUna carrera no se gana destruyendo tu sistema nervioso. Date permiso de hacer una Pausa Somática de 3 minutos para soltar cuello y hombros.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 5; // Pausa Somática
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 8. INSOMNIO / DIFICULTAD PARA DORMIR
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("dormir") || t.contains("insomnio") || t.contains("sueño") || t.contains("sueno")
                || t.contains("desvelo") || t.contains("cama")) {
            res.texto = "El descanso es indispensable para procesar la información del día, " + nombre + ".\n\nRecuerda hacer un vaciado mental en una hoja antes de acostarte y relajar cada grupo muscular con un Escáner Corporal.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 7; // Escáner Corporal
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 9. TRISTEZA / VACÍO / SOLEDAD / DÍA GRIS
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("triste") || t.contains("llor") || t.contains("solo") || t.contains("sola")
                || t.contains("desmotivad") || t.contains("vacio") || t.contains("vacío") || t.contains("deprim")) {
            res.texto = "Lamento que tengas este día gris, " + nombre + ". Es completamente humano y no tienes que fingir un optimismo forzado.\n\n¿Te ayudaría hacer una pausa breve con el Diario de Gratitud para rescatar algún detalle cálido de hoy?";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 6; // Diario de Gratitud
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 10. MOTIVACIÓN / METAS / CONFIANZA
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("motiva") || t.contains("confianza") || t.contains("miedo") || t.contains("meta")
                || t.contains("lograr") || t.contains("exito") || t.contains("éxito")) {
            res.texto = "Recuperar la confianza requiere recordar tus pequeños avances anteriores, " + nombre + ".\n\nLa técnica de Visualización Positiva activa las mismas regiones neuronales que vivir el logro en la realidad.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 8; // Visualización Positiva
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 11. SALUDOS Y AGRADECIMIENTOS
        // ══════════════════════════════════════════════════════════════════════
        if (t.equals("hola") || t.equals("buenas") || t.equals("buen dia") || t.equals("buenos dias") || t.equals("hey")) {
            res.texto = "¡Hola, " + nombre + "! Es un gusto saludarte. Estoy aquí listo para escucharte o acompañarte con alguna técnica. ¿Cómo te has sentido hoy?";
            res.sugiereEjercicio = false;
            return res;
        }

        if (t.contains("como estas") || t.contains("cómo estás") || t.contains("que tal")) {
            res.texto = "¡Muy bien y listo para escucharte, " + nombre + "! Platícame cómo va tu día o qué tienes en mente.";
            res.sugiereEjercicio = false;
            return res;
        }

        if (t.contains("gracias") || t.contains("me ayudo") || t.contains("me ayudó") || t.contains("grac")) {
            res.texto = "¡Me alegra muchísimo haberte sido de ayuda, " + nombre + "! El mérito es tuyo por darte este tiempo. ¡Aquí estaré si me necesitas!";
            res.sugiereEjercicio = false;
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 12. RESPUESTA DE CONVERSACIÓN ABIERTA INTELIGENTE
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
