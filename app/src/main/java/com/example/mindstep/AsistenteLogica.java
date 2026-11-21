package com.example.mindstep;

import java.util.Locale;

/**
 * Motor Conversacional y Árbol de Decisiones para el Asistente Preventivo
 * Basado en Terapia Cognitivo-Conductual (TCC / CBT) y Triaje de Salud Mental
 */
public class AsistenteLogica {

    public static class RespuestaBot {
        public String texto;
        public boolean incluyeEjercicio;
        public int tipoEjercicio; // 1: Respiración 4-7-8, 2: Anclaje 5-4-3-2-1, 3: Pausa activa
    }

    public static class Recomendacion {
        public String titulo;
        public String subtitulo;
        public String explicacion;
        public String botonAccion;
        public int tipoEjercicio;
    }

    /**
     * Procesa un mensaje de texto libre escrito por el usuario
     * y genera una respuesta reflexiva y útil basada en TCC.
     */
    public static RespuestaBot procesarMensajeUsuario(String mensaje, String nombreUsuario, String riesgoActual) {
        RespuestaBot res = new RespuestaBot();
        String texto = mensaje.toLowerCase(Locale.ROOT).trim();

        // 1. Detección de Ansiedad o Ataques de Pánico
        if (texto.contains("ansiedad") || texto.contains("panico") || texto.contains("pánico") ||
                texto.contains("pecho") || texto.contains("falta el aire") || texto.contains("asustado") ||
                texto.contains("taquicardia") || texto.contains("nervioso") || texto.contains("desesperado")) {
            res.texto = "Comprendo lo abrumador que se siente ahora mismo, " + nombreUsuario + ". Es una respuesta física de tu cuerpo ante la sobrecarga, pero estás a salvo y esto pasará.\n\n" +
                    "Vamos a regular tu sistema nervioso juntos. Te recomiendo hacer 2 minutos de la técnica de Respiración 4-7-8 para bajar el ritmo cardíaco de inmediato.";
            res.incluyeEjercicio = true;
            res.tipoEjercicio = 1;
            return res;
        }

        // 2. Detección de Insomnio / Problemas de Sueño
        if (texto.contains("dormir") || texto.contains("insomnio") || texto.contains("desvelo") ||
                texto.contains("desvelad") || texto.contains("sueño") || texto.contains("sueno") ||
                texto.contains("despertar cansado") || texto.contains("no puedo dormir")) {
            res.texto = "El sueño irregular es uno de los detonantes más rápidos de estrés, " + nombreUsuario + ".\n\n" +
                    "Te sugiero estas 3 pautas prácticas:\n" +
                    "1. Si llevas más de 20 minutos en cama sin poder dormir, levántate y haz algo con luz tenue (leer en papel, estirar).\n" +
                    "2. Deja el teléfono fuera de tu alcance inmediato.\n" +
                    "3. Haz una respiración diafragmática para indicarle a tu mente que es hora de apagarse.";
            res.incluyeEjercicio = true;
            res.tipoEjercicio = 1;
            return res;
        }

        // 3. Sobrecarga Académica / Trabajo / Exámenes
        if (texto.contains("examen") || texto.contains("examenes") || texto.contains("tarea") ||
                texto.contains("trabajo") || texto.contains("escuela") || texto.contains("universidad") ||
                texto.contains("profe") || texto.contains("entregas") || texto.contains("tiempo") ||
                texto.contains("procrastin")) {
            res.texto = "La presión por rendir académicamente suele generar parálisis por saturación mental.\n\n" +
                    "Prueba la técnica del 'Micro-Paso': No intentes terminar todo el proyecto de golpe. Elige una sola tarea pequeña y dedícale solo 15 minutos con un temporizador.\n\n" +
                    "¿Qué es lo único más urgente que tienes pendiente hoy?";
            res.incluyeEjercicio = false;
            return res;
        }

        // 4. Burnout / Agotamiento extremo
        if (texto.contains("burnout") || texto.contains("agotado") || texto.contains("agotada") ||
                texto.contains("cansado") || texto.contains("cansada") || texto.contains("quemado") ||
                texto.contains("harto") || texto.contains("harta") || texto.contains("ya no puedo") ||
                texto.contains("sin energia") || texto.contains("sin fuerzas")) {
            res.texto = "Tu cuerpo y tu mente te están pidiendo una pausa legítima, " + nombreUsuario + ". La productividad no vale la pena si destruye tu salud mental.\n\n" +
                    "Hoy date permiso de bajar la velocidad. Una pausa activa de 3 minutos sin pantallas ni exigencias te ayudará a restablecer tus recursos cognitivos.";
            res.incluyeEjercicio = true;
            res.tipoEjercicio = 3;
            return res;
        }

        // 5. Tristeza / Desánimo / Soledad
        if (texto.contains("triste") || texto.contains("llorar") || texto.contains("solo") ||
                texto.contains("sola") || texto.contains("deprimid") || texto.contains("desmotivad") ||
                texto.contains("vacio") || texto.contains("vacío") || texto.contains("mal")) {
            res.texto = "Gracias por abrirte y compartirlo conmigo. Está bien tener días difíciles; no tienes que fingir que todo está perfecto.\n\n" +
                    "Muchas veces la mente nos hace creer que nos sentiremos así para siempre, pero las emociones son olas temporales. ¿Hay alguien de confianza con quien puedas platicar o desahogarte hoy?";
            res.incluyeEjercicio = false;
            return res;
        }

        // 6. Solicitud directa de respiración o ejercicios
        if (texto.contains("respir") || texto.contains("ejercicio") || texto.contains("calmar") ||
                texto.contains("ayuda") || texto.contains("tecnica") || texto.contains("técnica")) {
            res.texto = "¡Claro que sí! Tomar control de tu respiración es la forma más rápida y biológica de indicarle a tu sistema simpático que baje la guardia.";
            res.incluyeEjercicio = true;
            res.tipoEjercicio = 1;
            return res;
        }

        // 7. Saludos / Hola
        if (texto.startsWith("hola") || texto.startsWith("buenos") || texto.startsWith("buenas") || texto.startsWith("que onda")) {
            res.texto = "¡Hola, " + nombreUsuario + "! Me alegra que estés aquí. Cuéntame, ¿cómo ha estado tu día y qué tienes en mente ahora mismo?";
            res.incluyeEjercicio = false;
            return res;
        }

        // 8. Agradecimientos
        if (texto.contains("gracias") || texto.contains("agradezco") || texto.contains("me sirvio") || texto.contains("me ayudaste")) {
            res.texto = "¡Me alegra muchísimo haberte ayudado, " + nombreUsuario + "! Recuerda que estoy aquí para cuando necesites desahogarte o regular tu ritmo.";
            res.incluyeEjercicio = false;
            return res;
        }

        // 9. Respuesta empática general
        res.texto = "Te escucho atentamente, " + nombreUsuario + ". A veces poner en palabras lo que sentimos nos ayuda a organizar el caos mental.\n\n" +
                "¿Sientes que esto que te pasa se relaciona más con tus horas de descanso, con exigencias externas o con pensamientos sobre el futuro?";
        res.incluyeEjercicio = false;
        return res;
    }

    /**
     * Procesa la opción rápida seleccionada por el usuario en el árbol de decisión
     */
    public static Recomendacion obtenerRecomendacion(String factorSeleccionado) {
        Recomendacion rec = new Recomendacion();

        if (factorSeleccionado.contains("Trabajo") || factorSeleccionado.contains("Escuela")) {
            rec.titulo = "Ejercicio Recomendado: Respiración 4-7-8";
            rec.subtitulo = "Técnica de Regulación Inmediata";
            rec.explicacion = "Una técnica de 2 minutos para reducir el ritmo cardíaco, disminuir el cortisol y calmar el sistema nervioso central.";
            rec.botonAccion = "Iniciar Ejercicio Guiado Ahora";
            rec.tipoEjercicio = 1;
        } else if (factorSeleccionado.contains("Ansiedad") || factorSeleccionado.contains("Rumiantes")) {
            rec.titulo = "Técnica Recomendada: Anclaje Sensorial (5-4-3-2-1)";
            rec.subtitulo = "Enfoque en el Presente";
            rec.explicacion = "Identifica 5 cosas que ves, 4 que tocas, 3 que oyes, 2 que hueles y 1 que saboreas para detener la espiral de pensamientos.";
            rec.botonAccion = "Iniciar Sesión de Anclaje";
            rec.tipoEjercicio = 2;
        } else {
            // Agotamiento Físico
            rec.titulo = "Recomendación: Pausa Somática y Desconexión";
            rec.subtitulo = "Alivio de Tensión Muscular";
            rec.explicacion = "Cierra tus ojos durante 3 minutos, estira hombros y cuello, y bebe un vaso de agua para hidratar tu organismo.";
            rec.botonAccion = "Iniciar Pausa de 3 Minutos";
            rec.tipoEjercicio = 3;
        }

        return rec;
    }
}
