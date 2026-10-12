package com.example.mindstep;

/**
 * Algoritmo del Árbol de Decisiones para el Asistente Preventivo
 * Capa de Negocio (Business Layer)
 */
public class AsistenteLogica {

    public static class Recomendacion {
        public String titulo;
        public String subtitulo;
        public String explicacion;
        public String botonAccion;
        public int tipoEjercicio; // 1: Respiración 4-7-8, 2: Grounding, 3: Pausa activa
    }

    /**
     * Procesa la opción seleccionada por el usuario en el árbol de decisión
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
