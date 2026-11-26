package com.example.mindstep;

import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Motor Autónomo de Triaje Local — MindStep v3.1
 * 100 % gratuito, offline y sin costo.
 * 
 * Evalúa si el mensaje coincide con alguna categoría conocida de salud mental.
 * Si NO coincide, devuelve NULL para que el servicio consulte a la IA Generativa (Gemini).
 */
public class GenerativeEngine {

    private static final Random random = new Random();

    public static class ResultadoAI {
        public String texto;
        public boolean sugiereEjercicio;
        public int tipoEjercicio;
    }

    /**
     * Intenta resolver localmente el mensaje con reglas TCC.
     * @return ResultadoAI si encuentra coincidencia temática, o null si es un tema abierto/desconocido.
     */
    public static ResultadoAI resolverLocalSiExiste(
            String mensajeUsuario,
            String nombreUsuario,
            String riesgoActual,
            List<String> historialTemas) {

        if (mensajeUsuario == null || mensajeUsuario.trim().isEmpty()) {
            ResultadoAI r = new ResultadoAI();
            r.texto = "Estoy aquí, " + (nombreUsuario != null ? nombreUsuario : "") + ". ¿Qué traes en mente?";
            return r;
        }

        String t = mensajeUsuario.toLowerCase(Locale.ROOT).trim();
        String nombre = (nombreUsuario != null && !nombreUsuario.isEmpty()) ? nombreUsuario : "amigo/a";

        // ══════════════════════════════════════════════════════════════════════
        // PROTOCOLO ÉTICO DE EMERGENCIA (Siempre local e instantáneo)
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("suicid") || t.contains("matar") || t.contains("no quiero vivir")
                || t.contains("acabar con todo") || t.contains("quitarme la vida")
                || t.contains("hacerme daño") || t.contains("ya no quiero existir")) {
            ResultadoAI res = new ResultadoAI();
            res.texto = nombre + ", gracias por confiarme algo tan profundo. Lo que sientes importa " +
                    "y mereces apoyo humano especializado ahora mismo.\n\n" +
                    "🆘 Llama a la Línea de la Vida (México): 800 911 2000\n" +
                    "Disponible 24 horas, gratis y confidencial.\n\n" +
                    "También puedes acercarte hoy con un familiar, amigo de confianza o un profesional de salud. " +
                    "MindStep seguirá aquí cuando lo necesites.";
            res.sugiereEjercicio = false;
            return res;
        }

        // 1. ANSIEDAD AGUDA
        if (t.contains("ansiedad") || t.contains("panico") || t.contains("pánico")
                || t.contains("taquicardia") || t.contains("falta el aire") || t.contains("ahogo")
                || t.contains("palpitaciones") || t.contains("angustia") || t.contains("desespera")
                || t.contains("pecho apretado")) {
            ResultadoAI res = new ResultadoAI();
            String[] intro = {
                    "Respira conmigo, " + nombre + ". Estás a salvo. Lo que sientes es una alarma corporal activada por el estrés, no un peligro real.",
                    "Estoy aquí contigo, " + nombre + ". Ese pico de angustia es muy incómodo pero pasará. Tu sistema nervioso puede calmarse con una técnica rápida."
            };
            res.texto = elegir(intro) + "\n\nCuando hiperventilamos baja el CO₂ y el cerebro cree estar en peligro. Exhalar el doble del tiempo activa el sistema parasimpático.\n\n👇 Pulsa abajo para la Respiración 4-7-8 guiada.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 1;
            return res;
        }

        // 2. FUTURO / INCERTIDUMBRE / RUMIACIÓN
        if (t.contains("futuro") || t.contains("incertidumbre") || t.contains("sobrepensar")
                || t.contains("sobrepienso") || t.contains("rumia") || t.contains("que va a pasar")
                || t.contains("miedo a fallar") || t.contains("anticipatoria") || t.contains("catastro")) {
            ResultadoAI res = new ResultadoAI();
            String[] intro = {
                    "Es muy natural que la mente intente viajar al futuro cuando siente incertidumbre, " + nombre + ". En TCC esto se llama ansiedad anticipatoria.",
                    "El sobrepensamiento suele disfrazarse de preparación, pero solo agota tu energía, " + nombre + "."
            };
            res.texto = elegir(intro) + "\n\nEl 85 % de las cosas que tememos jamás ocurren tal cual las imaginamos. La técnica de Anclaje 5-4-3-2-1 te ayuda a regresar al aquí y ahora.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 2;
            return res;
        }

        // 3. BLOQUEO / TAREAS / EXÁMENES / PROCRASTINACIÓN
        if (t.contains("examen") || t.contains("tarea") || t.contains("proyecto") || t.contains("estudiar")
                || t.contains("procrastin") || t.contains("bloque") || t.contains("no puedo avanzar")
                || t.contains("escuela") || t.contains("universidad") || t.contains("carrera")) {
            ResultadoAI res = new ResultadoAI();
            res.texto = "La saturación académica genera parálisis cognitiva, " + nombre + ".\n\nLa solución no es forzarte horas seguidas, sino trabajar en micro-metas de 15 minutos sin revisar el teléfono.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 4; // Pomodoro
            return res;
        }

        // 4. BURNOUT / AGOTAMIENTO
        if (t.contains("burnout") || t.contains("agotad") || t.contains("cansad") || t.contains("hart")
                || t.contains("sin fuerza") || t.contains("sin energia") || t.contains("no doy mas")
                || t.contains("quemad")) {
            ResultadoAI res = new ResultadoAI();
            res.texto = "Ese cansancio acumulado no se quita solo durmiendo un rato; tu cuerpo pide límites, " + nombre + ".\n\nUna carrera no se gana destruyendo tu sistema nervioso. Date permiso de hacer una pausa somática para soltar cuello y hombros.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 5; // Pausa Somática
            return res;
        }

        // 5. INSOMNIO / DORMIR
        if (t.contains("dormir") || t.contains("insomnio") || t.contains("sueño") || t.contains("sueno")
                || t.contains("desvelo")) {
            ResultadoAI res = new ResultadoAI();
            res.texto = "El descanso es vital, " + nombre + ".\n\nRecuerda: vaciado mental en una hoja antes de acostarte, cero pantallas 30 min antes y relajar los músculos con un escáner corporal.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 7; // Escáner Corporal
            return res;
        }

        // 6. TRISTEZA / VACÍO / SOLEDAD
        if (t.contains("triste") || t.contains("llor") || t.contains("solo") || t.contains("sola")
                || t.contains("desmotivad") || t.contains("vacio") || t.contains("vacío") || t.contains("deprim")) {
            ResultadoAI res = new ResultadoAI();
            res.texto = "Lamento que tengas este día gris, " + nombre + ". Es completamente humano y no tienes que fingir optimismo forzado.\n\n¿Te ayudaría hacer una pausa con el Diario de Gratitud para rescatar algún detalle cálido de hoy?";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 6; // Diario de Gratitud
            return res;
        }

        // 7. AGRADECIMIENTOS O SALUDOS BÁSICOS
        if (t.equals("hola") || t.equals("buenas") || t.equals("buen dia") || t.equals("buenos dias") || t.equals("hey")) {
            ResultadoAI res = new ResultadoAI();
            res.texto = "¡Hola, " + nombre + "! Estoy aquí listo para escucharte o acompañarte con alguna técnica. ¿Cómo te has sentido hoy?";
            res.sugiereEjercicio = false;
            return res;
        }

        if (t.contains("gracias") || t.contains("me ayudo") || t.contains("me ayudó")) {
            ResultadoAI res = new ResultadoAI();
            res.texto = "¡Me alegra muchísimo saberlo, " + nombre + "! El mérito es tuyo por darte este espacio. ¡Aquí estaré si me necesitas!";
            res.sugiereEjercicio = false;
            return res;
        }

        // NO coincide con ninguna categoría fija preestablecida:
        // devolvemos NULL para que entre la IA Generativa en la nube
        return null;
    }

    /**
     * Fallback cuando no hay internet y tampoco coincidió con reglas.
     */
    public static ResultadoAI generarFallbackDesconectado(String nombreUsuario, String riesgoActual) {
        ResultadoAI res = new ResultadoAI();
        String nombre = (nombreUsuario != null && !nombreUsuario.isEmpty()) ? nombreUsuario : "amigo/a";
        String[] abiertas = {
                "Te escucho con atención, " + nombre + ". A veces el solo hecho de poner en palabras lo que sentimos ya nos da un respiro.",
                "Entiendo lo que dices, " + nombre + ". Cuando la mente se satura, reflexionar sobre lo que podemos controlar ayuda mucho.",
                "Tiene sentido lo que compartes, " + nombre + ". Cuéntame más si lo deseas o dime si prefieres que hagamos un ejercicio de respiración o anclaje."
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
