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
 * Administra perfil local, switch de modo oscuro funcional,
 * alertas preventivas, notificaciones y control de datos.
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
            nombre = "Axel";
        }
        tvNombreAjustes.setText(nombre);
        tvAvatarAjustes.setText(String.valueOf(nombre.charAt(0)).toUpperCase());

        tvIdUsuario.setText("Perfil de Salud Mental");

        tvHoraRecordatorio.setText(preferencesManager.getReminderTime() + " (Toca para cambiar)");
        tvIdiomaTexto.setText(preferencesManager.getIdioma());

        switchRecordatorio.setChecked(preferencesManager.isRemindersEnabled());
        switchAlertaEstres.setChecked(preferencesManager.isStressAlertEnabled());
        switchModoOscuro.setChecked(preferencesManager.isDarkMode());
    }

    private void setupListeners() {
        View.OnClickListener perfilClickListener = v -> mostrarDialogoEditarPerfil();
        cardPerfilUsuario.setOnClickListener(perfilClickListener);
        itemPerfil.setOnClickListener(perfilClickListener);

        rowRecordatorioDiario.setOnClickListener(v -> mostrarTimePickerRecordatorio());

        switchRecordatorio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesManager.setRemindersEnabled(isChecked);
            Toast.makeText(this, isChecked ? "Recordatorio diario activado" : "Recordatorios desactivados", Toast.LENGTH_SHORT).show();
        });

        switchAlertaEstres.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesManager.setStressAlertEnabled(isChecked);
            Toast.makeText(this, isChecked ? "Alertas preventivas de sobrecarga activadas" : "Alertas desactivadas", Toast.LENGTH_SHORT).show();
        });

        // Switch Modo Oscuro Funcional con persistencia
        switchModoOscuro.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesManager.setDarkMode(isChecked);
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        itemIdioma.setOnClickListener(v -> mostrarSelectorIdioma());
        itemSuscripcion.setOnClickListener(v -> mostrarDialogoSuscripcion());
        itemContacto.setOnClickListener(v -> mostrarDialogoContacto());
        itemAyuda.setOnClickListener(v -> mostrarDialogoFAQ());
        itemValorar.setOnClickListener(v -> mostrarDialogoValorar());

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

        // Limpiar Historial de BD SQLite
        btnLimpiarHistorialBD.setOnClickListener(v -> confirmarLimpiarBD());
    }

    private void mostrarDialogoEditarPerfil() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Perfil");

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(60, 30, 60, 20);

        final EditText etNombre = new EditText(this);
        etNombre.setHint("Tu nombre");
        etNombre.setText(preferencesManager.getUserName());
        layout.addView(etNombre);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nuevoNombre = etNombre.getText().toString().trim();
            if (!TextUtils.isEmpty(nuevoNombre)) {
                preferencesManager.setUserName(nuevoNombre);
                cargarPreferencias();
                Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
            }
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
                    Toast.makeText(this, "Idioma: " + idiomas[which], Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoSuscripcion() {
        new AlertDialog.Builder(this)
                .setTitle("Plan MindStep")
                .setMessage("Tu plan actual es 100% Gratuito, Privado y Sin Límite.\n\n" +
                        "• Almacenamiento local SQLite seguro\n" +
                        "• Asistente preventivo con IA generativa\n" +
                        "• Monitoreo de hábitos y patrones\n" +
                        "• Sin anuncios y tus datos no salen de tu dispositivo.")
                .setPositiveButton("Excelente", null)
                .show();
    }

    private void mostrarDialogoContacto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Soporte y Sugerencias");

        final EditText input = new EditText(this);
        input.setHint("Escribe tu duda o sugerencia...");
        input.setMinLines(3);
        input.setPadding(40, 30, 40, 30);
        builder.setView(input);

        builder.setPositiveButton("Enviar", (dialog, which) -> {
            String texto = input.getText().toString().trim();
            if (!TextUtils.isEmpty(texto)) {
                Toast.makeText(this, "¡Mensaje recibido! Gracias por ayudarnos a mejorar.", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Cerrar", null);
        builder.show();
    }

    private void mostrarDialogoFAQ() {
        new AlertDialog.Builder(this)
                .setTitle("Preguntas Frecuentes")
                .setMessage("1. ¿Cómo se calcula mi riesgo?\n" +
                        "Analiza sueño (<6h resta), nivel de estrés y estado de ánimo.\n\n" +
                        "2. ¿Dónde se guardan mis datos?\n" +
                        "En tu teléfono (SQLite local). Nadie más tiene acceso.\n\n" +
                        "3. ¿Cómo uso el chatbot?\n" +
                        "Abre el asistente y háblale de lo que sea: ansiedad, exámenes, insomnio, etc.")
                .setPositiveButton("Entendido", null)
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
        layout.addView(ratingBar);

        builder.setView(layout);

        builder.setPositiveButton("Enviar", (dialog, which) -> {
            Toast.makeText(this, "¡Gracias por tus " + (int) ratingBar.getRating() + " estrellas! ⭐", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void confirmarLimpiarBD() {
        new AlertDialog.Builder(this)
                .setTitle("Limpiar Historial de Registros")
                .setMessage("¿Estás seguro de que deseas borrar todos los registros de hábitos guardados?")
                .setPositiveButton("Sí, borrar todo", (dialog, which) -> {
                    dbHelper.limpiarHistorial();
                    Toast.makeText(this, "Historial de registros vaciado", Toast.LENGTH_SHORT).show();
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
