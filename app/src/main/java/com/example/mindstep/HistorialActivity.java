package com.example.mindstep;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla 5: Historial de Bienestar
 * Listado cronológico de los registros del usuario almacenados en SQLite,
 * mostrando el nivel de riesgo, horas de descanso y etiquetas asociadas a cada día.
 */
public class HistorialActivity extends AppCompatActivity {

    private MaterialToolbar toolbarHistorial;
    private MaterialButton btnFiltroMes;
    private MaterialCardView cardResumenMensual;
    private TextView tvResumenTexto;
    private RecyclerView rvHistorialRegistros;
    private BottomNavigationView bottomNavigation;

    private DB dbHelper;
    private RegistrosAdapter adapter;
    private List<RegistroModel> listaRegistros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        dbHelper = new DB(this);

        initViews();
        setupToolbar();
        setupResumenMensual();
        setupRecyclerView();
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigation.setSelectedItemId(R.id.nav_historial);
        cargarDatosDesdeSQLite();
    }

    private void initViews() {
        toolbarHistorial = findViewById(R.id.toolbarHistorial);
        btnFiltroMes = findViewById(R.id.btnFiltroMes);
        cardResumenMensual = findViewById(R.id.cardResumenMensual);
        tvResumenTexto = findViewById(R.id.tvResumenTexto);
        rvHistorialRegistros = findViewById(R.id.rvHistorialRegistros);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setupToolbar() {
        if (btnFiltroMes != null) {
            btnFiltroMes.setOnClickListener(v -> mostrarFiltroMesDialog());
        }
    }

    private void setupResumenMensual() {
        if (cardResumenMensual != null) {
            cardResumenMensual.setOnClickListener(v -> {
                Toast.makeText(this, "Consistencia: 77% de registros completados", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void setupRecyclerView() {
        listaRegistros = new ArrayList<>();
        adapter = new RegistrosAdapter(listaRegistros);
        rvHistorialRegistros.setLayoutManager(new LinearLayoutManager(this));
        rvHistorialRegistros.setAdapter(adapter);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_inicio) {
                Intent intent = new Intent(HistorialActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_historial) {
                return true;
            } else if (itemId == R.id.nav_estadisticas) {
                Intent intent = new Intent(HistorialActivity.this, EstadisticasActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_ajustes) {
                Intent intent = new Intent(HistorialActivity.this, AjustesActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void cargarDatosDesdeSQLite() {
        if (dbHelper != null) {
            String resumen = dbHelper.obtenerResumenMensual();
            if (tvResumenTexto != null && resumen != null) {
                tvResumenTexto.setText(resumen);
            }

            List<RegistroModel> registrosFromDB = dbHelper.obtenerHistorialRegistros();
            listaRegistros.clear();
            if (registrosFromDB != null && !registrosFromDB.isEmpty()) {
                listaRegistros.addAll(registrosFromDB);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void mostrarFiltroMesDialog() {
        Toast.makeText(this, "Mostrando registros de Octubre 2026", Toast.LENGTH_SHORT).show();
    }

    // Adapter para RecyclerView
    private class RegistrosAdapter extends RecyclerView.Adapter<RegistrosAdapter.RegistroViewHolder> {

        private final List<RegistroModel> items;

        public RegistrosAdapter(List<RegistroModel> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public RegistroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_registro_diario, parent, false);
            return new RegistroViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RegistroViewHolder holder, int position) {
            RegistroModel model = items.get(position);

            holder.tvFechaRegistro.setText(model.getTituloFecha());
            holder.tvRiesgoBadge.setText(model.getRiesgo());

            try {
                int colorHex = Color.parseColor(model.getRiesgoColorHex());
                holder.viewRiesgoBadge.setBackgroundColor(colorHex);
                holder.tvRiesgoBadge.setTextColor(colorHex);
            } catch (Exception e) {
                holder.viewRiesgoBadge.setBackgroundColor(Color.parseColor("#22C55E"));
                holder.tvRiesgoBadge.setTextColor(Color.parseColor("#22C55E"));
            }

            String metricasTexto = model.getSueno() + " • " + model.getEstres() + " • " + model.getAnimo();
            holder.tvMetricas.setText(metricasTexto);
            holder.tvTags.setText(model.getTags());

            holder.itemView.setOnClickListener(v -> {
                Toast.makeText(HistorialActivity.this, "Detalle: " + model.getTituloFecha() + " (" + model.getRiesgo() + ")", Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class RegistroViewHolder extends RecyclerView.ViewHolder {
            View viewRiesgoBadge;
            TextView tvFechaRegistro;
            TextView tvRiesgoBadge;
            TextView tvMetricas;
            TextView tvTags;

            public RegistroViewHolder(@NonNull View itemView) {
                super(itemView);
                viewRiesgoBadge = itemView.findViewById(R.id.viewRiesgoBadge);
                tvFechaRegistro = itemView.findViewById(R.id.tvFechaRegistro);
                tvRiesgoBadge = itemView.findViewById(R.id.tvRiesgoBadge);
                tvMetricas = itemView.findViewById(R.id.tvMetricas);
                tvTags = itemView.findViewById(R.id.tvTags);
            }
        }
    }
}
