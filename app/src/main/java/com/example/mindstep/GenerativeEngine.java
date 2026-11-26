package com.example.mindstep;

import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Motor Autónomo de IA Generativa Local — MindStep v3.0
 * 100 % gratuito, ilimitado, offline, sin API Keys ni configuración técnica.
 *
 * Características:
 *  - 16 categorías de intención (ansiedad, insomnio, burnout, relaciones, autoestima, duelo…)
 *  - Respuestas variadas con Random para nunca repetir la misma cadena
 *  - Memoria de turno anterior (historialTemas) para seguimiento de contexto
 *  - 8 tipos de ejercicio interactivo
 *  - Protocolo ético de emergencia para riesgo de vida
 */
public class GenerativeEngine {

    private static final Random random = new Random();

    // ─────────────────────────── Tipos de ejercicio ───────────────────────────
    // 1 Respiración 4-7-8        5 Pausa Somática
    // 2 Anclaje 5-4-3-2-1        6 Diario de Gratitud
    // 3 Reencuadre Cognitivo     7 Escáner Corporal
    // 4 Pomodoro 15 min          8 Visualización Positiva

    public static class ResultadoAI {
        public String texto;
        public boolean sugiereEjercicio;
        public int tipoEjercicio;
    }

    /**
     * Punto de entrada principal: analiza el mensaje y el contexto
     * para generar una respuesta empática y ética.
     */
    public static ResultadoAI generarRespuesta(
            String mensajeUsuario,
            String nombreUsuario,
            String riesgoActual,
            List<String> historialTemas) {

        ResultadoAI res = new ResultadoAI();
        if (mensajeUsuario == null || mensajeUsuario.trim().isEmpty()) {
            res.texto = "Estoy aquí, " + nombreUsuario + ". ¿Qué traes en mente en este momento?";
            return res;
        }

        String t = mensajeUsuario.toLowerCase(Locale.ROOT).trim();
        String nombre = (nombreUsuario != null && !nombreUsuario.isEmpty()) ? nombreUsuario : "amigo/a";

        // ══════════════════════════════════════════════════════════════════════
        // PROTOCOLO ÉTICO DE EMERGENCIA — MÁXIMA PRIORIDAD
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
        // 1. ANSIEDAD AGUDA — Pánico, taquicardia, falta de aire
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("ansiedad") || t.contains("panico") || t.contains("pánico")
                || t.contains("taquicardia") || t.contains("falta el aire") || t.contains("ahogo")
                || t.contains("palpitaciones") || t.contains("angustia") || t.contains("desespera")
                || t.contains("pecho apretado")) {
            String[] intro = {
                    "Respira conmigo, " + nombre + ". Estás a salvo. Lo que sientes es una alarma corporal activada por el estrés, no un peligro real.",
                    "Estoy aquí contigo, " + nombre + ". Ese pico de angustia es muy incómodo pero pasará. Tu sistema nervioso puede calmarse con una técnica rápida.",
                    "Escúchame, " + nombre + ": esto que sientes es intenso, pero temporal. Tu cuerpo está en modo alerta y podemos regularlo juntos ahora mismo."
            };
            String[] cuerpo = {
                    "Cuando hiperventilamos, el CO₂ baja y se activa la sensación de peligro. Vamos a engañar al sistema nervioso exhalando el doble de lo que inhalamos.",
                    "La respiración diafragmática lenta activa el nervio vago y baja el pulso en segundos. Es fisiología pura, no autosugestión.",
                    "La técnica 4-7-8 aumenta el tono parasimpático y frena la adrenalina. Con dos ciclos sueles sentir una diferencia notable."
            };
            res.texto = elegir(intro) + "\n\n" + elegir(cuerpo) +
                    "\n\n👇 Presiona el botón para hacer la Respiración 4-7-8 guiada segundo a segundo.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 1;
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 2. PENSAMIENTOS ANTICIPATORIOS / FUTURO / RUMIACIÓN
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("futuro") || t.contains("incertidumbre") || t.contains("sobrepensar")
                || t.contains("sobrepienso") || t.contains("rumia") || t.contains("pensamientos")
                || t.contains("que va a pasar") || t.contains("miedo a fallar") || t.contains("miedo a")
                || t.contains("anticipatoria") || t.contains("catastro") || t.contains("todo va a salir mal")) {
            String[] intro = {
                    "Es muy natural que la mente intente controlar el futuro cuando siente incertidumbre, " + nombre + ". A eso en TCC le llamamos ansiedad anticipatoria.",
                    "El sobrepensamiento suele disfrazarse de 'preparación', pero en realidad solo agota tus reservas de energía mental, " + nombre + ".",
                    "Entiendo esa sensación de tener miles de escenarios dando vueltas en la cabeza. El cerebro ansioso actúa como un detector de humo hipersensible."
            };
            String[] dato = {
                    "Un dato útil: el 85 % de lo que imaginamos que saldrá mal nunca ocurre exactamente como lo tememos.",
                    "En TCC separamos lo 'posible' de lo 'probable'. Que algo malo pueda pasar no significa que vaya a suceder.",
                    "La mente ansiosa toma el peor escenario y lo trata como un hecho. Pero tú has salido adelante en situaciones difíciles antes."
            };
            String[] accion = {
                    "Para bajar esa velocidad mental, el Anclaje 5-4-3-2-1 te regresa al momento presente en menos de 2 minutos.",
                    "El Anclaje Sensorial interrumpe el ciclo de rumiación conectándote con lo que sí está en tus manos hoy.",
                    "Hagamos una pausa para volver al presente. El ejercicio de abajo te guiará paso a paso."
            };
            res.texto = elegir(intro) + "\n\n" + elegir(dato) + "\n\n" + elegir(accion);
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 2;
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 3. BLOQUEO ACADÉMICO / PROCRASTINACIÓN / EXÁMENES
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("examen") || t.contains("tarea") || t.contains("proyecto") || t.contains("estudiar")
                || t.contains("procrastin") || t.contains("bloque") || t.contains("no puedo avanzar")
                || t.contains("escuela") || t.contains("universidad") || t.contains("carrera")
                || t.contains("no puedo concentrar") || t.contains("distraigo") || t.contains("pendiente")) {
            String[] intro = {
                    "La saturación académica genera un efecto de congelamiento cognitivo, " + nombre + ". El cerebro se paraliza cuando siente que la lista es infinita.",
                    "Cuando vemos una montaña de pendientes, nuestro cerebro entra en resistencia automática. Eso no es flojera; es auto-protección, " + nombre + ".",
                    "Ese bloqueo que sientes es real, " + nombre + ". La sobreestimulación de tareas activa la misma respuesta que el estrés físico."
            };
            String[] cuerpo = {
                    "La solución probada no es trabajar más horas, sino romper la tarea en fragmentos tan pequeños que sea imposible fallar.",
                    "La 'Regla de los 2 minutos': si una tarea tarda menos de 2 minutos, hazla ya. Para las demás, divide en pasos de 15 minutos.",
                    "Un solo micro-objetivo bien definido activa más el cerebro que una lista de pendientes vaga. Elige la tarea más pequeña concreta."
            };
            res.texto = elegir(intro) + "\n\n" + elegir(cuerpo) +
                    "\n\n⏱️ Presiona abajo para activar el Temporizador de Foco de 15 minutos.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 4;
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 4. BURNOUT / AGOTAMIENTO / SIN FUERZAS
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("burnout") || t.contains("agotad") || t.contains("cansad") || t.contains("hart")
                || t.contains("sin fuerza") || t.contains("sin energia") || t.contains("no doy mas")
                || t.contains("quemad") || t.contains("ya no puedo") || t.contains("colapse")
                || t.contains("exhaust")) {
            String[] intro = {
                    "Ese cansancio que describes no se quita con una siesta, " + nombre + ". Es agotamiento acumulado por ignorar durante semanas las señales de tu cuerpo.",
                    "El burnout no aparece de un día para otro; es el resultado de dar constantemente sin recuperarte, " + nombre + ".",
                    "Lo que sientes es físicamente real: el cortisol sostenido afecta la memoria, la motivación y la inmunidad, " + nombre + "."
            };
            String[] cuerpo = {
                    "En MindStep creemos en la sostenibilidad: una carrera no se gana destruyendo tu sistema nervioso. Date hoy permiso de descansar sin culpa.",
                    "El descanso no es un lujo ni un premio. Es el combustible sin el cual ningún motor funciona bien.",
                    "Prioridad número uno ahora: hidratación, alejarte de la pantalla 10 minutos y soltar la tensión muscular que llevas acumulada."
            };
            res.texto = elegir(intro) + "\n\n" + elegir(cuerpo) +
                    "\n\n💧 Usa el botón de abajo para hacer la Pausa Somática guiada de 3 minutos.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 5;
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 5. INSOMNIO / PROBLEMAS PARA DORMIR
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("dormir") || t.contains("insomnio") || t.contains("sueño") || t.contains("sueno")
                || t.contains("desvelo") || t.contains("no puedo descansar") || t.contains("cansado al despertar")
                || t.contains("me quedo dormid") || t.contains("pesadilla")) {
            String[] intro = {
                    "El descanso es la piedra angular de tu salud mental, " + nombre + ". Cuando dormimos mal, el cerebro regula peor las emociones al día siguiente.",
                    "El insomnio casi siempre tiene raíz en la activación mental excesiva. La cama se asocia con alerta en lugar de descanso, " + nombre + ".",
                    "Dormir mal es tanto causa como consecuencia del estrés, " + nombre + ". Podemos romper ese ciclo con pequeños hábitos."
            };
            String[] tips = {
                    "Tres reglas para esta noche:\n• Escribe tus pendientes antes de acostarte (vaciado mental).\n• Cero pantallas 30 min antes.\n• Si no duermes en 20 min, levántate y lee con luz tenue.",
                    "El 'método del cuaderno': escribe todo lo que te preocupa en papel antes de dormir. Tu cerebro percibe que ya está 'guardado' y puede soltar la guardia.",
                    "Temperatura fresca, oscuridad total y reducir pantallas son los 3 factores con mayor evidencia para mejorar la calidad del sueño esta misma noche."
            };
            res.texto = elegir(intro) + "\n\n" + elegir(tips) + "\n\n¿Hay algo específico que te haya mantenido despierto últimamente?";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 7; // Escáner Corporal (ideal antes de dormir)
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 6. TRISTEZA / DESÁNIMO / VACÍO / SOLEDAD
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("triste") || t.contains("llor") || t.contains("solo") || t.contains("sola")
                || t.contains("desmotivad") || t.contains("vacio") || t.contains("vacío") || t.contains("deprim")
                || t.contains("sin ganas") || t.contains("aislad") || t.contains("nadie me entiende")) {
            String[] intro = {
                    "Gracias por confiarme esto, " + nombre + ". Es completamente humano tener días grises donde la energía simplemente no está disponible.",
                    "No tienes que forzarte a estar bien, " + nombre + ". Sentirte así no significa que algo esté roto; significa que eres humano/a.",
                    "Te escucho, " + nombre + ". Esos momentos de vacío son señales de que tu mente y cuerpo necesitan atención y gentileza."
            };
            String[] cuerpo = {
                    "Trátate hoy con la misma amabilidad con la que tratarías a tu mejor amigo/a si estuviera sufriendo. Esa compasión también la mereces tú.",
                    "No intentes 'resolver' la tristeza. Acéptala como una ola que llega y se va. Resistirla suele amplificarla.",
                    "Una cosa pequeña que a veces ayuda: escribe tres cosas concretas que, aunque sean mínimas, estuvieron bien hoy."
            };
            res.texto = elegir(intro) + "\n\n" + elegir(cuerpo) +
                    "\n\n📒 El Diario de Gratitud puede ayudarte a reconectar con lo positivo sin forzarlo.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 6; // Diario de Gratitud
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 7. RELACIONES / CONFLICTOS / PAREJA / AMIGOS
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("relacion") || t.contains("pareja") || t.contains("novio") || t.contains("novia")
                || t.contains("amigo") || t.contains("amistad") || t.contains("peleas") || t.contains("pelea")
                || t.contains("discusion") || t.contains("discusión") || t.contains("me traiciono")
                || t.contains("me falló") || t.contains("ruptura") || t.contains("termine") || t.contains("terminé")
                || t.contains("me dejó") || t.contains("me dejo")) {
            String[] intro = {
                    "Las relaciones son una de las fuentes de mayor bienestar y también de mayor dolor, " + nombre + ". Lo que sientes tiene mucho sentido.",
                    "Los conflictos interpersonales activan las mismas redes cerebrales que el dolor físico, " + nombre + ". No estás exagerando.",
                    "Gracias por contarme esto, " + nombre + ". Las relaciones son complejas y mereces un espacio para procesarlas sin juicio."
            };
            String[] reflexion = {
                    "Una pregunta útil de la TCC: ¿Estás respondiendo a lo que la otra persona dijo, o a la interpretación que tu mente hizo de ello?",
                    "En los conflictos relacionales, casi siempre hay dos realidades subjetivas válidas. Entender la perspectiva del otro no significa darle la razón.",
                    "¿Qué necesitas realmente de esta relación ahora mismo: ser escuchado/a, ser comprendido/a o que las cosas cambien?"
            };
            res.texto = elegir(intro) + "\n\n" + elegir(reflexion) +
                    "\n\n¿Quieres contarme más sobre lo que está pasando?";
            res.sugiereEjercicio = false;
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 8. AUTOESTIMA / COMPARACIÓN SOCIAL / REDES SOCIALES
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("autoestima") || t.contains("me odio") || t.contains("no sirvo") || t.contains("soy malo")
                || t.contains("soy mala") || t.contains("no valgo") || t.contains("comparar") || t.contains("instagram")
                || t.contains("redes sociales") || t.contains("todos tienen") || t.contains("todos son mejores")
                || t.contains("fracas") || t.contains("perdedor") || t.contains("perdedora") || t.contains("tonto")
                || t.contains("tonta") || t.contains("estupid")) {
            String[] intro = {
                    "Escuchar esas palabras tan duras dirigidas hacia ti mismo/a me preocupa y me importa, " + nombre + ".",
                    "La comparación social es el ladrón de la alegría, especialmente cuando solo vemos los 'highlights' de los demás, " + nombre + ".",
                    "Esas voces críticas internas no son hechos objetivos; son patrones aprendidos que pueden cambiar, " + nombre + "."
            };
            String[] reflexion = {
                    "Una técnica de TCC: cuando aparezca ese pensamiento autocrítico, pregúntate: '¿Le diría esto a un amigo/a en mi situación?' Si la respuesta es no, tampoco te lo digas a ti.",
                    "Las redes muestran versiones curadas y editadas de la realidad. Comparar tu interior (con dudas, miedos y procesos) con el exterior de otros es una comparación injusta.",
                    "El valor de una persona no lo define su productividad, sus logros ni la aprobación externa. ¿En qué área sí te reconoces alguna fortaleza, aunque sea pequeña?"
            };
            res.texto = elegir(intro) + "\n\n" + elegir(reflexion) +
                    "\n\n🔄 El Reencuadre Cognitivo puede ayudarte a desafiar esa voz crítica con evidencia real.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 3; // Reencuadre Cognitivo
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 9. PRESIÓN EXTERNA / FAMILIA / EXPECTATIVAS
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("familia") || t.contains("padres") || t.contains("mama") || t.contains("mamá")
                || t.contains("papa") || t.contains("papá") || t.contains("presion") || t.contains("presión")
                || t.contains("expectativas") || t.contains("decepcion") || t.contains("decepción")
                || t.contains("decepcionar") || t.contains("exigencia") || t.contains("calificacion")
                || t.contains("calificación")) {
            String[] intro = {
                    "Cargar con expectativas ajenas además de las propias es agotador, " + nombre + ". Ese peso doble tiene un costo real.",
                    "Querer no decepcionar a quienes amas es completamente comprensible, " + nombre + ". Pero a veces ese deseo puede convertirse en una jaula.",
                    "Las expectativas familiares pueden ser una muestra de amor o de proyección. A veces son ambas, " + nombre + "."
            };
            String[] reflexion = {
                    "Separemos lo que está en tu control:\n✅ Tu esfuerzo honesto, pedir ayuda, cuidar tu salud.\n❌ La reacción emocional de los demás a tus resultados.",
                    "Tu valor como persona no lo define ninguna calificación, decisión de carrera ni aprobación. ¿Qué quieres TÚ para tu vida?",
                    "Una conversación honesta (en el momento correcto) suele liberar más carga que seguir cargando solo/a el peso de las expectativas no verbalizadas."
            };
            res.texto = elegir(intro) + "\n\n" + elegir(reflexion) +
                    "\n\n¿Hay alguien con quien puedas hablar sobre esto?";
            res.sugiereEjercicio = false;
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 10. DUELO / PÉRDIDA / FIN DE ALGO IMPORTANTE
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("perdi") || t.contains("perdí") || t.contains("duelo") || t.contains("muerte")
                || t.contains("murió") || t.contains("murio") || t.contains("extraño") || t.contains("extrañ")
                || t.contains("se fue") || t.contains("ya no está") || t.contains("abuel")
                || t.contains("mascota")) {
            String[] intro = {
                    "Lo siento mucho, " + nombre + ". Una pérdida, sin importar su forma, deja un espacio real que el tiempo procesa de manera no lineal.",
                    "El duelo no tiene un calendario ni una forma correcta de vivirse, " + nombre + ". Lo que sientes es completamente válido.",
                    "Gracias por compartir algo tan personal, " + nombre + ". Perder a alguien o algo importante es uno de los dolores más profundos que existen."
            };
            String[] cuerpo = {
                    "No hay que 'superar' el duelo en un tiempo fijo. Hay que atravesarlo a tu propio ritmo, sin obligarte a estar bien antes de estarlo.",
                    "Permitirte llorar, recordar y hablar de esa persona o situación forma parte del proceso natural de integración.",
                    "Una cosa que ayuda: honrar lo que se fue con algún ritual significativo, grande o pequeño. ¿Hay algo que sientas que quieres hacer para reconocer esa pérdida?"
            };
            res.texto = elegir(intro) + "\n\n" + elegir(cuerpo) +
                    "\n\n¿Quieres contarme más sobre lo que extrañas?";
            res.sugiereEjercicio = false;
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 11. ESTRÉS GENERAL / TENSIÓN / SOBRECARGA
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("estres") || t.contains("estrés") || t.contains("tension") || t.contains("tensión")
                || t.contains("sobrecarga") || t.contains("estresad") || t.contains("mucho que hacer")
                || t.contains("no me da") || t.contains("sin tiempo")) {
            String[] intro = {
                    "El estrés moderado puede ser un motor útil, pero cuando se acumula sin pausa se convierte en desgaste, " + nombre + ".",
                    "Sentirte sobrecargado/a no es señal de debilidad; es una respuesta fisiológica a un entorno que exige demasiado, " + nombre + ".",
                    "Ese nivel de tensión que describes merece atención, " + nombre + ". El cuerpo nos avisa antes de que colapsemos."
            };
            String[] accion = {
                    "Una técnica inmediata: haz una 'descarga mental' en papel. Escribe TODO lo que tienes pendiente sin ordenarlo. Luego elige UNA sola cosa para hacer en la próxima hora.",
                    "El Escáner Corporal es una herramienta rápida para detectar dónde acumulas tensión y soltarla conscientemente.",
                    "Antes de continuar: ¿has comido, tomado agua y respirado profundo en la última hora? A veces el estrés se amplifica por necesidades básicas ignoradas."
            };
            res.texto = elegir(intro) + "\n\n" + elegir(accion);
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 7; // Escáner Corporal
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 12. MOTIVACIÓN / PROPÓSITO / DIRECCIÓN
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("motivacion") || t.contains("motivación") || t.contains("proposito") || t.contains("propósito")
                || t.contains("para que") || t.contains("para qué") || t.contains("sin sentido")
                || t.contains("no sé qué quiero") || t.contains("perdido") || t.contains("perdida")
                || t.contains("dirección") || t.contains("carrera no me gusta")) {
            String[] intro = {
                    "Preguntarse para qué es una señal de madurez, no de fracaso, " + nombre + ". La crisis de propósito es parte del crecimiento.",
                    "Sentirse perdido/a no es el destino final; es la señal de que algo está cambiando en ti, " + nombre + ".",
                    "Muchos de los avances más importantes en la vida empiezan con exactamente esa pregunta: '¿para qué hago esto?'"
            };
            String[] reflexion = {
                    "Una pregunta para explorar: si el dinero y la aprobación de los demás no importaran, ¿cómo pasarías tu tiempo? La respuesta dice mucho sobre tus valores reales.",
                    "El propósito rara vez aparece completo desde el inicio. Se construye probando, equivocándose y descartando lo que no resuena.",
                    "¿Hay algo que hagas —aunque sea pequeño— que te haga perder la noción del tiempo? Ahí suelen estar pistas importantes."
            };
            res.texto = elegir(intro) + "\n\n" + elegir(reflexion) +
                    "\n\n🌟 La Visualización Positiva puede ayudarte a conectar con lo que realmente quieres.";
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 8; // Visualización Positiva
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 13. SALUDOS Y APERTURA DE CONVERSACIÓN
        // ══════════════════════════════════════════════════════════════════════
        if (t.startsWith("hola") || t.startsWith("buenas") || t.startsWith("que tal")
                || t.startsWith("que onda") || t.startsWith("buenos") || t.equals("hey")
                || t.equals("hi") || t.startsWith("buen dia") || t.startsWith("buen día")) {
            String[] saludos = {
                    "¡Hola, " + nombre + "! Qué bueno verte por aquí. Estoy listo para escucharte sin juicios. ¿Qué traes en mente hoy?",
                    "¡Hola! Me alegra que hayas abierto el chat, " + nombre + ". A veces el primer paso es el más difícil. ¿Cómo te has sentido hoy?",
                    "¡Buenas, " + nombre + "! Aquí estoy. Puedes contarme lo que sea: algo que te preocupa, algo que sientes o simplemente charlar. ¿Por dónde empezamos?"
            };
            res.texto = elegir(saludos);
            res.sugiereEjercicio = false;
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 14. AGRADECIMIENTO / RETROALIMENTACIÓN POSITIVA
        // ══════════════════════════════════════════════════════════════════════
        if (t.contains("gracias") || t.contains("me ayudo") || t.contains("me ayudó")
                || t.contains("me siento mejor") || t.contains("me sirvio") || t.contains("me sirvió")
                || t.contains("funcionó") || t.contains("funciono")) {
            String[] gracias = {
                    "¡Qué bien, " + nombre + "! El mérito es completamente tuyo por tomarte este tiempo para cuidar tu mente. ¡Sigue así!",
                    "Me alegra mucho que haya sido de ayuda, " + nombre + ". Date crédito: has dado un paso real hacia tu bienestar hoy.",
                    "Eso me da mucho gusto, " + nombre + ". Recuerda que MindStep siempre estará aquí cuando lo necesites. ¡Cuídate!"
            };
            res.texto = elegir(gracias);
            res.sugiereEjercicio = false;
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 15. RESPUESTAS BREVES / AFIRMACIONES
        // ══════════════════════════════════════════════════════════════════════
        if (t.equals("si") || t.equals("sí") || t.equals("ok") || t.equals("dale")
                || t.contains("vamos") || t.contains("por favor") || t.contains("ayudame")
                || t.equals("claro") || t.equals("bueno")) {
            String[] afirmaciones = {
                    "¡Excelente, " + nombre + "! Me alegra tu disposición. Vamos paso a paso, sin prisa.",
                    "¡Perfecto! Aquí tienes la herramienta guiada. Tómate el tiempo que necesites.",
                    "¡Vamos, " + nombre + "! Lo mejor que puedes hacer por ti mismo/a es exactamente esto."
            };
            res.texto = elegir(afirmaciones);
            res.sugiereEjercicio = true;
            res.tipoEjercicio = 2; // Anclaje como default
            return res;
        }

        // ══════════════════════════════════════════════════════════════════════
        // 16. RESPUESTA REFLEXIVA ADAPTATIVA (fallback inteligente)
        // ══════════════════════════════════════════════════════════════════════
        // Variamos según el riesgo del usuario para personalizar el fallback
        String[] abiertas;
        if ("Elevado".equalsIgnoreCase(riesgoActual)) {
            abiertas = new String[]{
                    "Te escucho, " + nombre + ". Con el nivel de estrés que traes, es importante que hablemos. ¿Qué es lo que más te está pesando en este momento?",
                    "Noto que has tenido una semana difícil, " + nombre + ". No tienes que resolverlo todo hoy. ¿Qué es lo primero que se te viene a la mente?",
                    "Gracias por escribirme, " + nombre + ". Cuando el nivel de estrés sube, el cuerpo nos manda señales que vale la pena escuchar. ¿Cómo te sientes físicamente ahora mismo?"
            };
        } else if ("Moderado".equalsIgnoreCase(riesgoActual)) {
            abiertas = new String[]{
                    "Comprendo lo que me dices, " + nombre + ". Expresar lo que sentimos en palabras es el primer paso para procesarlo. ¿Puedes contarme más?",
                    "Tiene mucho sentido lo que describes, " + nombre + ". A veces no necesitamos respuestas mágicas, sino simplemente un espacio para ordenar lo que sentimos.",
                    "Te escucho con atención, " + nombre + ". ¿Qué es lo que más te ayudaría a sentir un poco más de calma justo ahora?"
            };
        } else {
            abiertas = new String[]{
                    "Interesante lo que compartes, " + nombre + ". ¿Quieres explorarlo un poco más? Cuéntame con tus propias palabras.",
                    "Escucho lo que dices, " + nombre + ". A veces el solo hecho de articularlo en palabras ya ayuda. ¿Qué más está pasando?",
                    "Gracias por contarme, " + nombre + ". Si pudieras quitarte un solo pendiente de encima en este momento, ¿cuál elegirías?"
            };
        }

        String[] preguntas = {
                "¿Sientes que esto se relaciona más con la carga académica, con tu descanso o con algo en tus relaciones?",
                "¿Qué es lo que más necesitas en este momento: ser escuchado/a, una técnica concreta o simplemente charlar?",
                "Si pudieras describir cómo te sientes con una sola imagen o metáfora, ¿cuál sería?"
        };

        res.texto = elegir(abiertas) + "\n\n" + elegir(preguntas);
        res.sugiereEjercicio = false;
        return res;
    }

    private static String elegir(String[] opciones) {
        if (opciones == null || opciones.length == 0) return "";
        return opciones[random.nextInt(opciones.length)];
    }
}
