package com.example.mindstep;

import java.util.Locale;

/**
 * Motor Conversacional de Salud Mental y Árbol de Decisiones (TCC y Triaje)
 * Maneja diálogo con contexto, múltiples herramientas de regulación emocional,
 * reestructuración cognitiva y desafíos de pensamientos.
 */
public class AsistenteLogica {

    public static class RespuestaBot {
        public String texto;
        public boolean incluyeEjercicio;
        public int tipoEjercicio; 
        // 1: Respiración 4-7-8
        // 2: Anclaje 5-4-3-2-1
        // 3: Cuestionamiento de Pensamientos (TCC)
        // 4: Técnica Pomodoro & Micro-Metas
        // 5: Pausa Somática / Estiramiento
    }

    public static class Recomendacion {
        public String titulo;
        public String subtitulo;
        public String explicacion;
        public String botonAccion;
        public int tipoEjercicio;
    }

    /**
     * Procesa el mensaje del usuario considerando la intención y ofreciendo
     * herramientas prácticas variadas (no solo respiración).
     */
    public static RespuestaBot procesarMensajeUsuario(String mensaje, String nombreUsuario, String riesgoActual, String ultimoContexto) {
        RespuestaBot res = new RespuestaBot();
        String t = mensaje.toLowerCase(Locale.ROOT).trim();

        // 1. Pensamientos sobre el Futuro / Ansiedad Anticipatoria / Rumiación
        if (t.contains("futuro") || t.contains("pensamiento") || t.contains("sobrepensar") || 
            t.contains("rumi") || t.contains("preocupa") || t.contains("inciert") || t.contains("miedo al")) {
            res.texto = "La mente suele viajar al futuro intentando controlar lo que aún no pasa, " + nombreUsuario + ". En Terapia Cognitivo-Conductual a esto le llamamos 'ansiedad anticipatoria'.\n\n" +
                    "Hagamos un ejercicio de Cuestionamiento de Pensamientos:\n" +
                    "1. ¿Qué es lo peor que crees que va a pasar?\n" +
                    "2. ¿Qué tan probable es objetivamente (de 0 a 100%) que ocurra tal cual lo imaginas?\n" +
                    "3. Si ocurriera, ¿qué harías para resolverlo?\n\n" +
                    "¿Quieres que hagamos la técnica de Anclaje Sensorial (5-4-3-2-1) para traer tu atención al presente?";
            res.incluyeEjercicio = true;
            res.tipoEjercicio = 2; // Anclaje sensorial
            return res;
        }

        // 2. Exigencias externas / Presión de calificaciones / Profesores / Familia
        if (t.contains("exigencia") || t.contains("presion") || t.contains("presión") || 
            t.contains("expectativa") || t.contains("familia") || t.contains("padres") || t.contains("calificacion")) {
            res.texto = "Cargar con expectativas ajenas o autoexigirte perfección es agotador, " + nombreUsuario + ".\n\n" +
                    "Recuerda: Tu valor como persona no lo define una sola entrega, un examen ni la aprobación de otros.\n\n" +
                    "Separemos lo que está en tu control de lo que no:\n" +
                    "• En tu control: cuánto esfuerzo pones hoy, cuándo descansas y pedir ayuda.\n" +
                    "• Fuera de tu control: la reacción de los demás o los resultados inmediatos.\n\n" +
                    "¿Sientes que te estás exigiendo de más en este momento?";
            res.incluyeEjercicio = false;
            return res;
        }

        // 3. Bloqueo / Procrastinación / No puedo empezar / Exámenes y Tareas
        if (t.contains("bloque") || t.contains("no puedo empezar") || t.contains("procrastin") || 
            t.contains("examen") || t.contains("tarea") || t.contains("proyecto") || t.contains("mucho que hacer")) {
            res.texto = "La procrastinación casi nunca es flojera; es miedo al agobio o a que no quede perfecto, " + nombreUsuario + ".\n\n" +
                    "Vamos a usar la 'Regla de los 5 Minutos':\n" +
                    "No pienses en terminar todo. Solo abre el documento, libreta o código y trabaja durante 5 minutos de reloj. Si quieres parar a los 5 minutos, puedes hacerlo.\n\n" +
                    "¿Cuál es el primer paso más diminuto que podrías dar hoy?";
            res.incluyeEjercicio = true;
            res.tipoEjercicio = 4; // Pomodoro & Micro-metas
            return res;
        }

        // 4. Ataques de pánico / Taquicardia / Angustia aguda
        if (t.contains("panico") || t.contains("pánico") || t.contains("pecho") || 
            t.contains("falta el aire") || t.contains("taquicardia") || t.contains("ahogo")) {
            res.texto = "Estoy aquí contigo, " + nombreUsuario + ". Tu cuerpo está en alerta, pero estás a salvo. Esto es temporal y bajará.\n\n" +
                    "Vamos a hacer la respiración 4-7-8 para activar tu nervio vago y estabilizar tu pulso de inmediato.";
            res.incluyeEjercicio = true;
            res.tipoEjercicio = 1; // Respiración 4-7-8
            return res;
        }

        // 5. Cansancio mental / Saturación / Burnout
        if (t.contains("burnout") || t.contains("agotad") || t.contains("cansad") || 
            t.contains("quemad") || t.contains("hart") || t.contains("ya no doy")) {
            res.texto = "Has llevado tu cuerpo al límite y te está pidiendo una pausa legítima, " + nombreUsuario + ".\n\n" +
                    "Descansar no es un premio que te ganas después de agotarte; es una necesidad biológica indispensable para que tu cerebro funcione.\n\n" +
                    "Te recomiendo una Pausa Somática de 3 minutos: alejarte de la pantalla, estirar cuello y hombros y tomar agua fresca.";
            res.incluyeEjercicio = true;
            res.tipoEjercicio = 5; // Pausa somática
            return res;
        }

        // 6. Insomnio / Problemas de descanso
        if (t.contains("dormir") || t.contains("insomnio") || t.contains("sueño") || t.contains("sueno") || t.contains("desvelo")) {
            res.texto = "Cuando la mente no para de noche, quedarse dando vueltas en la cama asocia el colchón con la angustia.\n\n" +
                    "Aplica el 'Vaciado Mental': Escribe en una hoja todo lo que te preocupa o lo que tienes pendiente para mañana. Al dejarlo en papel, tu cerebro puede soltar la guardia.\n\n" +
                    "¿Hay algo específico que te impida apagar tus pensamientos?";
            res.incluyeEjercicio = false;
            return res;
        }

        // 7. Tristeza / Desánimo / Soledad
        if (t.contains("triste") || t.contains("deprim") || t.contains("llor") || t.contains("solo") || t.contains("sola") || t.contains("desmotivad")) {
            res.texto = "Lamento que estés pasando por este momento, " + nombreUsuario + ". No tienes que fingir optimismo ni ser fuerte todo el tiempo.\n\n" +
                    "Permítete sentir la emoción sin juzgarla. Si pudieras pedir un deseo realista para el día de hoy, ¿qué te traería un poco de alivio o paz?";
            res.incluyeEjercicio = false;
            return res;
        }

        // 8. Respuestas breves / Sí / No
        if (t.equals("si") || t.equals("sí") || t.contains("por favor") || t.contains("quiero") || t.contains("dale")) {
            res.texto = "¡De acuerdo, " + nombreUsuario + "! Vamos a ponerlo en práctica ahora mismo. Recuerda hacer cada paso a tu propio ritmo.";
            res.incluyeEjercicio = true;
            res.tipoEjercicio = 2; // Default a anclaje sensorial
            return res;
        }

        if (t.equals("no") || t.contains("ahora no") || t.contains("luego")) {
            res.texto = "Completamente entendible, " + nombreUsuario + ". No hay presión. Cuéntame con tus propias palabras qué te está pesando en la cabeza.";
            res.incluyeEjercicio = false;
            return res;
        }

        // 9. Saludos
        if (t.startsWith("hola") || t.startsWith("buenas") || t.startsWith("que tal") || t.startsWith("que onda")) {
            res.texto = "¡Hola, " + nombreUsuario + "! Aquí estoy listo para escucharte. ¿Cómo te has sentido hoy y qué te gustaría conversar o trabajar?";
            res.incluyeEjercicio = false;
            return res;
        }

        // 10. Agradecimiento
        if (t.contains("gracias") || t.contains("me ayudó") || t.contains("me sirvio")) {
            res.texto = "¡Me alegra muchísimo haberte sido de ayuda, " + nombreUsuario + "! Date crédito por darte este tiempo para cuidar tu mente.";
            res.incluyeEjercicio = false;
            return res;
        }

        // 11. Respuesta reflexiva variada si el mensaje es abierto
        res.texto = "Entiendo lo que mencionas, " + nombreUsuario + ".\n\n" +
                "A veces los retos cotidianos nos sobrepasan cuando intentamos resolver todo a la vez. Si pudieras enfocarte en una sola cosa que resolverías en la próxima hora, ¿cuál sería?";
        res.incluyeEjercicio = false;
        return res;
    }

    public static Recomendacion obtenerRecomendacion(String factorSeleccionado) {
        Recomendacion rec = new Recomendacion();

        if (factorSeleccionado.contains("Trabajo") || factorSeleccionado.contains("Escuela")) {
            rec.titulo = "Técnica Pomodoro y Micro-Metas";
            rec.subtitulo = "Desbloqueo y Foco";
            rec.explicacion = "Trabaja 15 minutos en un solo micro-paso y toma 5 de pausa activa para evitar saturación cognitiva.";
            rec.botonAccion = "Iniciar Temporizador de Foco";
            rec.tipoEjercicio = 4;
        } else if (factorSeleccionado.contains("Ansiedad") || factorSeleccionado.contains("Rumiantes") || factorSeleccionado.contains("Futuro")) {
            rec.titulo = "Técnica de Anclaje Sensorial (5-4-3-2-1)";
            rec.subtitulo = "Conectar con el Presente";
            rec.explicacion = "Identifica 5 cosas que ves, 4 que tocas, 3 que oyes, 2 que hueles y 1 que saboreas para romper el bucle mental.";
            rec.botonAccion = "Iniciar Sesión de Anclaje";
            rec.tipoEjercicio = 2;
        } else {
            rec.titulo = "Pausa Somática y Relajación Muscular";
            rec.subtitulo = "Alivio Físico Inmediato";
            rec.explicacion = "Estira cuello y hombros, respira profundo y toma un vaso con agua para oxigenar tu organismo.";
            rec.botonAccion = "Iniciar Pausa Guiada";
            rec.tipoEjercicio = 5;
        }

        return rec;
    }
}
