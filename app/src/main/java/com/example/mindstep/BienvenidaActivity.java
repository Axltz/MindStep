package com.example.mindstep;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Pantalla 1: Pantalla de Bienvenida (Onboarding inicial)
 * Punto de entrada a la plataforma donde se introduce MindStep
 * y se solicita el nombre de usuario local.
 */
public class BienvenidaActivity extends AppCompatActivity {

    private EditText etNombre;
    private Button btnComenzar;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferencesManager = new PreferencesManager(this);

        // Si ya realizó el diagnóstico inicial anteriormente, navegar a Home directamente
        if (!preferencesManager.isFirstTime()) {
            Intent intent = new Intent(BienvenidaActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_bienvenida);

        etNombre = findViewById(R.id.etNombre);
        btnComenzar = findViewById(R.id.btnComenzar);

        // Prellenar con nombre guardado si existe
        String savedName = preferencesManager.getUserName();
        if (!TextUtils.isEmpty(savedName) && !"Axel".equals(savedName)) {
            etNombre.setText(savedName);
        }

        btnComenzar.setOnClickListener(v -> {
            String nombreIngresado = etNombre.getText().toString().trim();
            if (TextUtils.isEmpty(nombreIngresado)) {
                nombreIngresado = "Axel"; // Valor por defecto amigable
            }

            // Guardar nombre en persistencia local
            preferencesManager.setUserName(nombreIngresado);

            // Iniciar flujo del test diagnóstico inicial (Paso 1 de 3)
            Intent intent = new Intent(BienvenidaActivity.this, EvaluacionActivity.class);
            startActivity(intent);
        });
    }
}
