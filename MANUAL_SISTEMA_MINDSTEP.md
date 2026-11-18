# 📱 MANUAL COMPLETO DEL SISTEMA MINDSTEP

**Versión:** 4.0  
**Desarrollador:** Axel Guillermo  
**Plataforma:** Android Nativo (Java) con Material Design 3  
**Persistencia:** SQLite Local (`DB.java`) + SharedPreferences (`PreferencesManager.java`)  
**Enfoque:** Prevención de Burnout, Monitoreo de Hábitos y Salud Mental Estudiantil (100% Offline y Privado).

---

## 📌 1. DESCRIPCIÓN GENERAL Y ARQUITECTURA DEL SISTEMA

**MindStep** es una aplicación móvil nativa diseñada para acompañar a estudiantes y profesionales en la gestión de su bienestar mental, ofreciendo herramientas preventivas de psicología cognitivo-conductual (TCC), evaluación algorítmica del nivel de riesgo, monitoreo diario de hábitos y visualización de patrones de estrés vs. descanso.

### 🏗️ Pilares Arquitectónicos:
1. **100% Offline y Privado:** Todos los registros, diagnósticos y diálogos se procesan y almacenan localmente en el dispositivo. Ningún dato sale a la nube.
2. **Material Design 3 Adaptativo:** Vistas XML nativas con soporte completo de WindowInsets / Edge-To-Edge para ajustarse dinámicamente a cualquier resolución, recortado de cámara (*notch*) y barra de gestos.
3. **Motor Conversacional TCC Autónomo:** Árbol de decisiones psicológico en `GenerativeEngine.java` que ofrece respuestas inmediatas, empáticas y 8 ejercicios de regulación emocional interactivos.
4. **Base de Datos SQLite Robusta:** Tabla `registros` gestionada mediante `DB.java` con `SQLiteOpenHelper`.

---

## 🖥️ 2. MANUAL Y DESCRIPCIÓN DE PANTALLAS (MÓDULOS DEL SISTEMA)

### 1. Pantalla de Bienvenida (`BienvenidaActivity.java` | `activity_bienvenida.xml`)
* **Propósito:** Onboarding inicial del usuario.
* **Funcionamiento:** Solicita el nombre del usuario y verifica con `PreferencesManager` si es la primera vez que ingresa. Si ya completó la evaluación inicial, redirige automáticamente a la pantalla de Inicio (*Home*).

### 2. Evaluación Diagnóstica Inicial (`EvaluacionActivity.java` | `activity_evaluacion_inicial.xml`)
* **Propósito:** Diagnóstico expreso de 3 pasos para determinar el nivel de riesgo emocional base.
* **Funcionamiento:**
  * Paso 1: Estrés y energía.
  * Paso 2: Sueño y descanso.
  * Paso 3: Regulación y hábitos.
* **Resultado:** Calcula la puntuación inicial (Riesgo Bajo, Moderado o Elevado) y la almacena en `SharedPreferences`.

### 3. Dashboard Principal / Home (`HomeActivity.java` | `activity_home.xml`)
* **Propósito:** Centro de control del usuario.
* **Componentes:**
  * Tarjeta dinámica del Nivel de Riesgo actual (colores condicionales: verde `#22C55E` para Bajo, amarillo `#F59E0B` para Moderado, rojo `#EF4444` para Elevado).
  * Fecha actual en español.
  * Botones de acceso rápido: "Capturar Hábitos Diarios" y "Hablar con Asistente".
  * Barra de Navegación Inferior (*BottomNavigationView*).

### 4. Registro Diario de Hábitos (`RegistroHabitosActivity.java` | `activity_registro_habitos.xml`)
* **Propósito:** Captura de métricas diarias con sliders interactivos.
* **Métricas Capturadas:**
  * Sueño (Horas).
  * Estrés (Escala 1 a 10).
  * Estado de Ánimo (Emojis: Excelente, Bien, Regular, Mal, Burnout).
  * Factores Detonantes (Chips: #Exámenes, #Tareas, #Trabajo, #Insomnio, #Ejercicio, #Social).
  * Actividad Física (Minutos), Hidratación (Litros) y Tiempo de Pantalla (Horas).
  * Notas personales.
* **Cálculo:** `ValidadorMetricas.java` evalúa los datos, genera el Score del día y guarda el registro en la base de datos SQLite (`DB.java`).

### 5. Historial de Bienestar (`HistorialActivity.java` | `activity_historial.xml`)
* **Propósito:** Registro cronológico de todos los días capturados.
* **Componentes:**
  * Header con Resumen Mensual (porcentaje de consistencia).
  * Filtro de búsqueda por mes.
  * *RecyclerView* con tarjetas individuales (`item_registro_diario.xml`) mostrando badge lateral de riesgo, métricas compactas y etiquetas (*tags*).

### 6. Estadísticas y Patrones (`EstadisticasActivity.java` | `activity_estadisticas.xml`)
* **Propósito:** Visualización de correlación entre horas de sueño y picos de estrés.
* **Componentes:**
  * Canvas personalizado `GraficaPatronesView.java` que dibuja las líneas comparativas de sueño vs. estrés.
  * Filtro por periodos (7 días, 30 días, 90 días).
  * Diagnóstico algorítmico automatizado basado en promedios reales de SQLite.

### 7. Asistente de Apoyo Preventivo (`AsistenteActivity.java` | `activity_asistente.xml`)
* **Propósito:** Chatbot adaptativo de salud mental con 8 ejercicios guiados TCC.
* **Ejercicios Disponibles:**
  1. 🌬️ Respiración Guiada 4-7-8 (con temporizador en vivo).
  2. 🧘‍♂️ Anclaje Sensorial (5-4-3-2-1).
  3. 🔄 Reencuadre Cognitivo (TCC).
  4. ⏱️ Técnica Pomodoro y Micro-Metas (15 min).
  5. 💧 Pausa Somática y Alivio Muscular.
  6. 📒 Diario de Gratitud.
  7. 🌙 Escáner Corporal para Descanso.
  8. 🌟 Visualización Positiva.

### 8. Ajustes y Configuración (`AjustesActivity.java` | `activity_ajustes.xml`)
* **Propósito:** Administración de perfil y preferencias del usuario.
* **Funcionalidades:**
  * Edición de Nombre de Usuario.
  * Configuración de Recordatorio Diario (TimePicker).
  * Switch de Alerta Preventiva de Estrés.
  * Switch de Modo Oscuro (*AppCompatDelegate*).
  * Selección de Idioma.
  * Opción de Repetir Evaluación Diagnóstica Inicial.
  * Vaciar todo el historial en SQLite.

---

## 🗄️ 3. ESQUEMA DE BASE DE DATOS SQLITE (`DB.java`)

### Tabla: `registros`
| Columna | Tipo SQL | Descripción |
| :--- | :--- | :--- |
| `id` | INTEGER PRIMARY KEY AUTOINCREMENT | ID del registro |
| `fecha` | TEXT | Fecha corta (ej. "08 Octubre") |
| `titulo_fecha` | TEXT | Título descriptivo (ej. "Hoy - 08 Octubre") |
| `riesgo` | TEXT | Nivel de riesgo (Riesgo Bajo, Moderado, Elevado) |
| `riesgo_color` | TEXT | Código HEX del color (#22C55E, #F59E0B, #EF4444) |
| `sueno` | TEXT | Texto compacto de sueño (ej. "Sueño 7.5h") |
| `estres` | TEXT | Texto compacto de estrés (ej. "Estrés 4/10") |
| `animo` | TEXT | Texto de estado de ánimo (ej. "Ánimo 🙂 Bien") |
| `tags` | TEXT | Etiquetas asociadas (ej. "#Trabajo #Ejercicio") |
| `notas` | TEXT | Notas adicionales del usuario |

---

## 🚀 4. INSTRUCCIONES PARA PUBLICAR EN GITHUB

Para subir todo el código actualizado y la documentación al repositorio de Git `https://github.com/Axltz/MindStep`:

### Comandos Git en Terminal / PowerShell:

```bash
# 1. Verificar estado de los archivos modificados
git status

# 2. Agregar todos los archivos del proyecto y el manual
git add .

# 3. Crear commit con mensaje descriptivo
git commit -m "MindStep v4.0: Motor TCC Offline completo, Adaptación de Pantalla Edge-to-Edge y Manual de Sistema"

# 4. Asegurar que la rama principal sea 'main' o 'master'
git branch -M main

# 5. Configurar el origen remoto si no está agregado
git remote add origin https://github.com/Axltz/MindStep.git 2>/dev/null || git remote set-url origin https://github.com/Axltz/MindStep.git

# 6. Subir todo los cambios al repositorio GitHub
git push -u origin main
```

---

## ✅ 5. GUÍA RÁPIDA DE USO PARA EL USUARIO FINAL

1. **Paso 1:** Abre la app e ingresa tu nombre en la pantalla de bienvenida.
2. **Paso 2:** Completa las 3 preguntas de la evaluación inicial para obtener tu diagnóstico de partida.
3. **Paso 3:** Desde el **Home**, presiona *"Capturar Hábitos Diarios"* cada noche para registrar cómo estuvo tu día.
4. **Paso 4:** Revisa tu evolución en la pestaña **Historial** y descubre tus patrones de estrés vs. sueño en **Estadísticas**.
5. **Paso 5:** Si sientes tensión o sobrecarga en cualquier momento, abre el **Asistente** para desahogarte o realizar una sesión de respiración guiada.
