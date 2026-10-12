package com.example.mindstep;

/**
 * Calculadora de Riesgo y Score del Día
 * Capa de Negocio (Business Layer)
 */
public class ValidadorMetricas {

    public static class ResultadoCalculo {
        public int score;
        public String nivelRiesgo;
        public String colorHex;
        public String mensajeEstado;
        public String desgloseSueno;
        public String desgloseEstres;
        public String disparadorPrincipal;
    }

    /**
     * Calcula el bienestar general y el nivel de riesgo a partir de las métricas diarias
     */
    public static ResultadoCalculo evaluarMetricas(
            float horasSueno,
            int nivelEstres,
            int indiceAnimo, // 0: Excelente, 1: Bien, 2: Regular, 3: Mal, 4: Burnout
            int minutosActividad,
            float litrosAgua,
            float horasPantalla
    ) {
        ResultadoCalculo res = new ResultadoCalculo();
        int puntos = 50; // Base neutral

        // Factor Sueño
        if (horasSueno >= 7.0f && horasSueno <= 9.0f) {
            puntos += 20;
            res.desgloseSueno = "Sueño +20pts (Óptimo)";
        } else if (horasSueno >= 6.0f) {
            puntos += 10;
            res.desgloseSueno = "Sueño +10pts (Aceptable)";
        } else {
            puntos -= 20;
            res.desgloseSueno = "Sueño -20pts (Déficit severo)";
        }

        // Factor Estrés (1 a 10)
        if (nivelEstres <= 3) {
            puntos += 15;
            res.desgloseEstres = "Estrés +15pts (Tranquilo)";
        } else if (nivelEstres <= 6) {
            puntos -= 5;
            res.desgloseEstres = "Estrés -5pts (Moderado)";
        } else {
            puntos -= 25;
            res.desgloseEstres = "Estrés -25pts (Sobrecarga alta)";
        }

        // Factor Ánimo
        switch (indiceAnimo) {
            case 0: puntos += 15; break; // Excelente
            case 1: puntos += 10; break; // Bien
            case 2: puntos += 0; break;  // Regular
            case 3: puntos -= 10; break; // Mal
            case 4: puntos -= 20; break; // Burnout
        }

        // Actividad física & hidratación
        if (minutosActividad >= 30) puntos += 5;
        if (litrosAgua >= 2.0f) puntos += 5;
        if (horasPantalla > 6.0f) puntos -= 10;

        // Limitar rango a 0-100
        res.score = Math.max(5, Math.min(100, puntos));

        // Determinar riesgo y etiquetas
        if (res.score >= 70) {
            res.nivelRiesgo = "Bajo / Estable";
            res.colorHex = "#22C55E";
            res.mensajeEstado = "Tus hábitos de sueño y presión se mantienen en equilibrio hoy.";
            res.disparadorPrincipal = "#Hábitos_Saludables";
        } else if (res.score >= 45) {
            res.nivelRiesgo = "Moderado";
            res.colorHex = "#F59E0B";
            res.mensajeEstado = "Hay indicios de sobrecarga de estrés o descanso insuficiente.";
            res.disparadorPrincipal = (horasSueno < 6) ? "#Poco_Descanso" : "#Carga_Laboral";
        } else {
            res.nivelRiesgo = "Elevado";
            res.colorHex = "#EF4444";
            res.mensajeEstado = "Nivel de riesgo alto. Se recomienda pausar y realizar ejercicios de relajación.";
            res.disparadorPrincipal = (nivelEstres >= 7) ? "#Estrés_Académico" : "#Sobrecarga_Mental";
        }

        return res;
    }
}
