package com.example.mindstep;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;
import java.util.Locale;

/**
 * Pantalla 8: Ajustes y Configuración
 * Administra el perfil local del usuario, switch de modo oscuro,
 * alertas preventivas, notificaciones y utilidades del sistema.
 */
public class AjustesActivity extends AppCompatActivity {

    private MaterialCardView cardPerfilUsuario;
    private TextView tvAvatarAjustes;
    private TextView tvNombreAjustes;
    private TextView tvIdUsuario;

    private View rowRecordatorioDiario;
    private TextView tvHoraRecordatorio;
    private SwitchMaterial switchRecordatorio;
    private SwitchMaterial switchAlertaEstres;
    private SwitchMaterial switchModoOscuro;

    private View itemIdioma;
    private TextView tvIdiomaTexto;
    private View itemSuscripcion;
    private View itemPerfil;
    private View itemContacto;
    private View itemAyuda;
    private View itemValorar;

    private MaterialButton btnReiniciarDiagnostico;
    private MaterialButton btnLimpiarHistorialBD;
    private BottomNavigationView bottomNavigation;

    private PreferencesManager preferencesManager;
    private DB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        preferencesManager = new PreferencesManager(this);
        dbHelper = new DB(this);

        initViews();
        cargarPreferencias();
        setupListeners();
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigation.setSelectedItemId(R.id.nav_ajustes);
        cargarPreferencias();
    }

    private void initViews() {
        cardPerfilUsuario = findViewById(R.id.cardPerfilUsuario);
        tvAvatarAjustes = findViewById(R.id.tvAvatarAjustes);
        tvNombreAjustes = findViewById(R.id.tvNombreAjustes);
        tvIdUsuario = findViewById(R.id.tvIdUsuario);

        rowRecordatorioDiario = findViewById(R.id.rowRecordatorioDiario);
        tvHoraRecordatorio = findViewById(R.id.tvHoraRecordatorio);
        switchRecordatorio = findViewById(R.id.switchRecordatorio);
        switchAlertaEstres = findViewById(R.id.switchAlertaEstres);
        switchModoOscuro = findViewById(R.id.switchModoOscuro);

        itemIdioma = findViewById(R.id.itemIdioma);
        tvIdiomaTexto = findViewById(R.id.tvIdiomaTexto);
        itemSuscripcion = findViewById(R.id.itemSuscripcion);
        itemPerfil = findViewById(R.id.itemPerfil);
        itemContacto = findViewById(R.id.itemContacto);
        itemAyuda = findViewById(R.id.itemAyuda);
        itemValorar = findViewById(R.id.itemValorar);

        btnReiniciarDiagnostico = findViewById(R.id.btnReiniciarDiagnostico);
        btnLimpiarHistorialBD = findViewById(R.id.btnLimpiarHistorialBD);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void cargarPreferencias() {
        String nombre = preferencesManager.getUserName();
        if (nombre == null || nombre.trim().isEmpty()) {
            nombre = "Axel Guillermo";
        }
        tvNombreAjustes.setText(nombre);
        tvAvatarAjustes.setText(String.valueOf(nombre.charAt(0)).toUpperCase());

        String userId = preferencesManager.getUserId();
        tvIdUsuario.setText("Usuario Local #" + userId);

        tvHoraRecordatorio.setText(preferencesManager.getReminderTime() + " (Toca para cambiar)");
        tvIdiomaTexto.setText(preferencesManager.getIdioma());

        switchRecordatorio.setChecked(preferencesManager.isRemindersEnabled());
        switchAlertaEstres.setChecked(preferencesManager.isStressAlertEnabled());
        switchModoOscuro.setChecked(preferencesManager.isDarkMode());
    }

    private void setupListeners() {
        // Editar Perfil (desde tarjeta superior o desde fila de perfil)
        View.OnClickListener perfilClickListener = v -> mostrarDialogoEditarPerfil();
        cardPerfilUsuario.setOnClickListener(perfilClickListener);
        itemPerfil.setOnClickListener(perfilClickListener);

        // Recordatorio Diario: TimePicker al tocar
        rowRecordatorioDiario.setOnClickListener(v -> mostrarTimePickerRecordatorio());

        switchRecordatorio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesManager.setRemindersEnabled(isChecked);
            Toast.makeText(this, isChecked ? "Recordatorio diario activado" : "Recordatorios desactivados", Toast.LENGTH_SHORT).show();
        });

        // Alerta de Estrés
        switchAlertaEstres.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesManager.setStressAlertEnabled(isChecked);
            Toast.makeText(this, isChecked ? "Alertas preventivas de sobrecarga activadas" : "Alertas desactivadas", Toast.LENGTH_SHORT).show();
        });

        // Modo Oscuro
        switchModoOscuro.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesManager.setDarkMode(isChecked);
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Idioma
        itemIdioma.setOnClickListener(v -> mostrarSelectorIdioma());

        // Suscripción
        itemSuscripcion.setOnClickListener(v -> mostrarDialogoSuscripcion());

        // Soporte / Contacto
        itemContacto.setOnClickListener(v -> mostrarDialogoContacto());

        // FAQ / Ayuda
        itemAyuda.setOnClickListener(v -> mostrarDialogoFAQ());

        // Valorar
        itemValorar.setOnClickListener(v -> mostrarDialogoValorar());

        // Repetir Evaluación Diagnóstica Inicial
        btnReiniciarDiagnostico.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Repetir Evaluación Diagnóstica")
                    .setMessage("¿Deseas reiniciar tu diagnóstico inicial de estrés y energía?")
                    .setPositiveButton("Sí, iniciar", (d, which) -> {
                        preferencesManager.setFirstTime(true);
                        Intent intent = new Intent(AjustesActivity.this, EvaluacionActivity.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        // Restablecer Datos de Ejemplo
        btnLimpiarHistorialBD.setOnClickListener(v -> confirmarRestablecerBD());
    }

    private void mostrarDialogoEditarPerfil() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Perfil Local");

        View view = LayoutInflater.from(this).inflate(android.R.layout.select_dialog_item, null);
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(60, 30, 60, 20);

        final EditText etNombre = new EditText(this);
        etNombre.setHint("Nombre completo");
        etNombre.setText(preferencesManager.getUserName());
        layout.addView(etNombre);

        final EditText etCodigo = new EditText(this);
        etCodigo.setHint("Código de estudiante");
        etCodigo.setText(preferencesManager.getUserId());
        layout.addView(etCodigo);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nuevoNombre = etNombre.getText().toString().trim();
            String nuevoCodigo = etCodigo.getText().toString().trim();

            if (!TextUtils.isEmpty(nuevoNombre)) {
                preferencesManager.setUserName(nuevoNombre);
            }
            if (!TextUtils.isEmpty(nuevoCodigo)) {
                preferencesManager.setUserId(nuevoCodigo);
            }

            cargarPreferencias();
            Toast.makeText(this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarTimePickerRecordatorio() {
        Calendar c = Calendar.getInstance();
        int horaActual = c.get(Calendar.HOUR_OF_DAY);
        int minutoActual = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            String amPm = hourOfDay >= 12 ? "PM" : "AM";
            int hora12 = hourOfDay % 12;
            if (hora12 == 0) hora12 = 12;

            String horaFormateada = String.format(Locale.getDefault(), "%02d:%02d %s", hora12, minute, amPm);
            preferencesManager.setReminderTime(horaFormateada);
            tvHoraRecordatorio.setText(horaFormateada + " (Toca para cambiar)");
            Toast.makeText(this, "Recordatorio fijado a las " + horaFormateada, Toast.LENGTH_SHORT).show();
        }, horaActual, minutoActual, false);

        timePickerDialog.show();
    }

    private void mostrarSelectorIdioma() {
        String[] idiomas = {"Español (Latinoamérica)", "Español (España)", "English (US)"};
        int seleccionado = 0;
        String guardado = preferencesManager.getIdioma();
        if ("Español (España)".equals(guardado)) seleccionado = 1;
        else if ("English (US)".equals(guardado)) seleccionado = 2;

        new AlertDialog.Builder(this)
                .setTitle("Seleccionar Idioma")
                .setSingleChoiceItems(idiomas, seleccionado, (dialog, which) -> {
                    preferencesManager.setIdioma(idiomas[which]);
                    tvIdiomaTexto.setText(idiomas[which]);
                    Toast.makeText(this, "Idioma configurado: " + idiomas[which], Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoSuscripcion() {
        new AlertDialog.Builder(this)
                .setTitle("Plan Estudiantil MindStep")
                .setMessage("Tu plan actual es 100% Gratuito e Ilimitado.\n\n" +
                        "• Almacenamiento local SQLite seguro sin límites\n" +
                        "• Asistente preventivo interactivo siempre disponible offline\n" +
                        "• Registro diario de métricas y análisis de patrones\n" +
                        "• Sin publicidad y con total privacidad de tus datos.")
                .setPositiveButton("Entendido", null)
                .show();
    }

    private void mostrarDialogoContacto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Soporte y Retroalimentación");

        final EditText input = new EditText(this);
        input.setHint("Escribe tu duda, reporte o sugerencia...");
        input.setMinLines(3);
        input.setPadding(40, 30, 40, 30);
        builder.setView(input);

        builder.setPositiveButton("Enviar", (dialog, which) -> {
            String texto = input.getText().toString().trim();
            if (!TextUtils.isEmpty(texto)) {
                Toast.makeText(this, "¡Gracias! Tu reporte ha sido recibido por el equipo de MindStep.", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Cerrar", null);
        builder.show();
    }

    private void mostrarDialogoFAQ() {
        new AlertDialog.Builder(this)
                .setTitle("Centro de Ayuda y Preguntas Frecuentes")
                .setMessage("1. ¿Cómo se calcula mi nivel de riesgo?\n" +
                        "El sistema analiza tus horas de sueño (<6h resta puntos), nivel de estrés percibido y estado de ánimo diario con el algoritmo de ValidadorMetricas.\n\n" +
                        "2. ¿Mis datos salen de mi teléfono?\n" +
                        "No. La app fue diseñada bajo el principio 'Offline First'. Toda la información se queda en la base de datos interna SQLite de tu celular.\n\n" +
                        "3. ¿Qué hago si tengo un ataque de pánico?\n" +
                        "Abre el Asistente Preventivo y presiona 'Respiración 4-7-8' o escribe 'ansiedad' para que te guíe paso a paso.")
                .setPositiveButton("Cerrar", null)
                .show();
    }

    private void mostrarDialogoValorar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Valorar MindStep");

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(60, 40, 60, 20);

        RatingBar ratingBar = new RatingBar(this, null, android.R.attr.ratingBarStyleIndicator);
        ratingBar.setIsIndicator(false);
        ratingBar.setNumStars(5);
        ratingBar.setRating(5);
        ratingBar.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        layout.addView(ratingBar);

        final EditText etComentario = new EditText(this);
        etComentario.setHint("¿Qué te ha parecido la experiencia?");
        etComentario.setPadding(0, 30, 0, 10);
        layout.addView(etComentario);

        builder.setView(layout);

        builder.setPositiveButton("Enviar Calificación", (dialog, which) -> {
            Toast.makeText(this, "¡Muchas gracias por tus " + (int) ratingBar.getRating() + " estrellas! ⭐", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void confirmarRestablecerBD() {
        new AlertDialog.Builder(this)
                .setTitle("Restablecer Base de Datos")
                .setMessage("¿Deseas reiniciar los datos locales de hábitos y volver a cargar los datos de prueba de Octubre?")
                .setPositiveButton("Sí, restablecer", (dialog, which) -> {
                    dbHelper.restablecerDatosEjemplo();
                    preferencesManager.setLastRisk("Bajo");
                    preferencesManager.setLastScore(85);
                    Toast.makeText(this, "Datos de prueba restablecidos en SQLite", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_inicio) {
                Intent intent = new Intent(AjustesActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_historial) {
                Intent intent = new Intent(AjustesActivity.this, HistorialActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_estadisticas) {
                Intent intent = new Intent(AjustesActivity.this, EstadisticasActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_ajustes) {
                return true;
            }
            return false;
        });
    }
}
