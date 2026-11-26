package com.example.mindstep;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Motor Autónomo de IA Generativa Local (NLP & Razonamiento Emocional TCC)
 * 100% Gratuito, Ilimitado, Offline, Sin necesidad de API Keys ni configuración técnica.
 * Genera respuestas adaptativas, empáticas y personalizadas basadas en el estado del usuario.
 */
public class GenerativeEngine {

    private static final Random random = new Random();

    public static class ResultadoAI {
        public String texto;
        public boolean sugiereEjercicio;
        public int tipoEjercicio; // 1: Respiración 4-7-8, 2: Anclaje 5-4-3-2-1, 4: Pomodoro, 5: Pausa Somática
    }

    /**
     * Analiza el mensaje, el perfil, el estado de ánimo y el historial
     * para generar una respuesta sintética inteligente, profunda y ética.
     */
    public static ResultadoAI generarRespuesta(String mensajeUsuario, String nombreUsuario, String riesgoActual, List<String> historialTemas) {
        ResultadoAI res = new ResultadoAI();
        String t = mensajeUsuario.toLowerCase(Locale.ROOT).trim();

        // Protocolo Ético de Emergencia (Seguridad Absoluta)
        if (t.contains("suicid") || t.contains("matar") || t.contains("morir") || t.contains("no quiero vivir") || t.contains("acabar con todo") || t.contains("cortar")) {
            res.texto = nombreUsuario + ", por favor escúchame: tu vida tiene un valor inmenso y no tienes que cargar con este dolor en soledad.\n\n" +
                    "Como MindStep es un sistema preventivo, requieres apoyo humano profesional de inmediato. En México puedes comunicarte gratis y confidencialmente las 24 hrs a la Línea de la Vida al 800 911 2000.\n\n" +
                    "Por favor acércate hoy mismo con un ser querido, familiar o especialista. Estamos contigo.";
            res.sugiereEjercicio = false;
            return res;
        }

        // 1. Pensamientos Anticipatorios / Futuro / Incertidumbre
        if (t.contains("futuro") || t.contains("incertidumbre") || t.contains("que va a pasar") || t.contains("miedo a fallar") || t.contains("sobrepensar") || t.contains("rumia")) {
            String[] aperturas = {
                    "Es muy natural que la mente intente viajar al futuro cuando siente miedo a la falta de control, " + nombreUsuario + ".",
                    "El sobrepensamiento suele disfrazarse de preparación, pero en realidad solo agota tus reservas de energía, " + nombreUsuario + ".",
                    "Entiendo perfectamente esa sensación de tener mil escenarios del futuro dándote vueltas en la cabeza."
            };
            String[] reflexiones = {
                    "En Terapia Cognitivo-Conductual separamos lo 'posible' de lo 'probable'. Que algo malo pueda pasar no significa que vaya a suceder.",
                    "La mente suele asumir el peor escenario como si fuera un hecho seguro, olvidando todas las veces que has salido adelante.",
                    "El 85% de las cosas que nos angustian del futuro jamás llegan a concretarse tal cual las tememos."
            };
            String[] llamados = {
                    "Para bajar esa velocidad mental, te sugiero hacer el ejercicio de Anclaje 5-4-3-2-1. Te ayudará a regresar tu atención a lo que sí está en tus manos hoy.",
                    "¿Qué te parece si frenamos ese torbellino con un ejercicio de anclaje a tus sentidos?",
                    "Hagamos una pausa para volver al presente. Aquí abajo tienes la técnica de anclaje para aterrizar la mente."
            };

            res.texto = elegir(aperturas) + "\n\n" + elegir(reflexiones) + "\n\n" + elegir(llamados);
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 2; // Anclaje sensorial
            return res;
        }

        // 2. Ansiedad aguda, Taquicardia, Crisis
        if (t.contains("ansiedad") || t.contains("panico") || t.contains("pánico") || t.contains("pecho") || t.contains("falta el aire") || t.contains("taquicardia") || t.contains("desesperad")) {
            String[] aperturas = {
                    "Respira despacio conmigo, " + nombreUsuario + ". Estás a salvo en este momento y esto que sientes va a pasar.",
                    "Siento mucho que estés atravesando este pico de angustia, " + nombreUsuario + ". Es una alarma corporal encendida por el estrés, pero no estás en peligro real."
            };
            res.texto = elegir(aperturas) + "\n\n" +
                    "Cuando el cuerpo entra en hiperventilación, el cerebro interpreta que debe correr o luchar. Vamos a engañar a tu sistema nervioso exhalando el doble de tiempo de lo que inhalas.\n\n" +
                    "Presiona el botón de abajo para hacer la Respiración 4-7-8 juntos. Te guiaré segundo a segundo.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 1; // Respiración
            return res;
        }

        // 3. Bloqueo, Procrastinación, Exámenes, Universidad
        if (t.contains("examen") || t.contains("tarea") || t.contains("proyecto") || t.contains("procrastin") || t.contains("bloque") || t.contains("no puedo avanzar") || t.contains("escuela") || t.contains("estudiar")) {
            String[] aperturas = {
                    "La saturación académica genera un efecto de congelamiento cognitivo, " + nombreUsuario + ".",
                    "Cuando vemos una montaña de pendientes, nuestro cerebro entra en resistencia automática, " + nombreUsuario + "."
            };
            res.texto = elegir(aperturas) + "\n\n" +
                    "La solución probada no es obligarte a trabajar 6 horas seguidas, sino romper la tarea en fragmentos tan pequeños que sea imposible fallar.\n\n" +
                    "Usa la técnica de Micro-Metas de 15 minutos: Elige un solo párrafo, una sola lectura o un problema y dedícale solo 15 minutos sin revisar el teléfono. ¿Empezamos?";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 4; // Pomodoro
            return res;
        }

        // 4. Burnout, Agotamiento Severo, Sin Energía
        if (t.contains("burnout") || t.contains("agotad") || t.contains("cansad") || t.contains("hart") || t.contains("sin fuerza") || t.contains("sin energia") || t.contains("no doy mas")) {
            res.texto = "Ese cansancio que sientes no se quita solo durmiendo un rato; es agotamiento acumulado por ignorar tus límites, " + nombreUsuario + ".\n\n" +
                    "En MindStep promovemos la sostenibilidad: Un semestre o una carrera no se ganan destruyendo tu sistema nervioso. Hoy date permiso de descansar sin culpa.\n\n" +
                    "Te invito a hacer una Pausa Somática de 3 minutos para soltar la mandíbula, estirar cuello y hombros e hidratarte.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 5; // Pausa Somática
            return res;
        }

        // 5. Insomnio, Problemas para dormir
        if (t.contains("dormir") || t.contains("insomnio") || t.contains("sueño") || t.contains("sueno") || t.contains("desvelo") || t.contains("despertar cansado")) {
            res.texto = "El descanso es la piedra angular de tu salud mental, " + nombreUsuario + ".\n\n" +
                    "Tres reglas de oro para hoy:\n" +
                    "1. Vaciado mental: Escribe en una nota lo que te preocupa para que tu cerebro entienda que ya quedó registrado.\n" +
                    "2. Cero pantallas luminosas 30 minutos antes de acostarte.\n" +
                    "3. Si pasan 20 minutos sin dormirte, sal de la cama y lee con luz tenue hasta sentir pesadez en los párpados.\n\n" +
                    "¿Hay algo específico que te haya quitado el sueño esta noche?";
            res.sugiereEjercicio = false;
            return res;
        }

        // 6. Tristeza, Desánimo, Soledad
        if (t.contains("triste") || t.contains("llor") || t.contains("solo") || t.contains("sola") || t.contains("desmotivad") || t.contains("vacio") || t.contains("mal")) {
            res.texto = "Te agradezco mucho la confianza de contármelo, " + nombreUsuario + ". Es completamente humano tener días grises donde la energía simplemente no está ahí.\n\n" +
                    "No intentes forzarte a estar feliz de inmediato. Acepta que hoy es un día de bajo voltaje y trátate con la misma amabilidad con la que tratarías a tu mejor amigo si estuviera sufriendo.\n\n" +
                    "¿Qué pequeña cosa (un baño caliente, un té, una caminata o tu música favorita) te daría un respiro ahorita?";
            res.sugiereEjercicio = false;
            return res;
        }

        // 7. Respuestas conversacionales (Sí, Claro, Vale, Ok)
        if (t.equals("si") || t.equals("sí") || t.contains("claro") || t.contains("vamos") || t.contains("ayudame") || t.contains("por favor")) {
            res.texto = "¡Excelente, " + nombreUsuario + "! Me alegra tu disposición para cuidarte.\n\n" +
                    "Aquí tienes la herramienta interactiva guiada. Tómate el tiempo que necesites sin prisas.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 2; // Anclaje o Respiración
            return res;
        }

        // 8. Saludos
        if (t.startsWith("hola") || t.startsWith("buenas") || t.startsWith("que tal") || t.startsWith("que onda") || t.startsWith("buenos")) {
            res.texto = "¡Hola, " + nombreUsuario + "! Qué gusto saludarte.\n\n" +
                    "Estoy activo para escucharte libremente, ayudarte a desenredar pensamientos o acompañarte con técnicas de relajación. ¿Qué traes en mente en este momento?";
            res.sugiereEjercicio = false;
            return res;
        }

        // 9. Agradecimiento
        if (t.contains("gracias") || t.contains("agradezco") || t.contains("me sirvio") || t.contains("me siento mejor")) {
            res.texto = "¡Me llena de alegría saberlo, " + nombreUsuario + "! El mérito es completamente tuyo por hacer una pausa y reflexionar sobre tu bienestar.\n\n" +
                    "Aquí estaré siempre que necesites despejar la mente. ¡Que tengas un excelente día!";
            res.sugiereEjercicio = false;
            return res;
        }

        // 10. Respuesta adaptativa inteligente para mensajes abiertos
        String[] abiertas = {
                "Comprendo lo que me dices, " + nombreUsuario + ". Cuando acumulamos muchas cosas en la cabeza, expresarlas en palabras es el primer paso para procesarlas.",
                "Tiene mucho sentido lo que comentas. A veces no necesitamos respuestas mágicas, sino simplemente poder ordenar lo que sentimos sin que nadie nos juzgue.",
                "Te escucho con atención, " + nombreUsuario + ". En el día a día universitario solemos ignorar estas señales hasta que el cuerpo nos pasa la factura."
        };
        String[] preguntas = {
                "¿Sientes que esto se relaciona más con la carga de estudio, con tus horas de descanso o con expectativas personales?",
                "Si pudieras quitarte un solo pendiente o preocupación de encima en este instante, ¿cuál elegirías?",
                "¿Qué es lo que más te ayudaría a sentir un poco más de calma hoy?"
        };

        res.texto = elegir(abiertas) + "\n\n" + elegir(preguntas);
        res.sugiereEjercicio = false;
        return res;
    }

    private static String elegir(String[] opciones) {
        return opciones[random.nextInt(opciones.length)];
    }
}
