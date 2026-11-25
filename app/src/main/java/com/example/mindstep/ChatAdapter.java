package com.example.mindstep;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    public interface OnEjercicioClickListener {
        void onEjercicioClick(int tipoEjercicio);
    }

    private final List<ChatMessage> mensajes;
    private final OnEjercicioClickListener listener;

    public ChatAdapter(List<ChatMessage> mensajes, OnEjercicioClickListener listener) {
        this.mensajes = mensajes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_mensaje, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage msg = mensajes.get(position);

        if (msg.isEsBot()) {
            holder.layoutMensajeBot.setVisibility(View.VISIBLE);
            holder.layoutMensajeUsuario.setVisibility(View.GONE);
            holder.tvTextoBot.setText(msg.getTexto());
            holder.tvHoraBot.setText(msg.getHora());

            if (msg.isSugiereEjercicio()) {
                holder.btnAccionEjercicioBot.setVisibility(View.VISIBLE);
                switch (msg.getTipoEjercicio()) {
                    case 1:
                        holder.btnAccionEjercicioBot.setText("🌬️ Iniciar Respiración 4-7-8");
                        break;
                    case 2:
                        holder.btnAccionEjercicioBot.setText("🧘‍♂️ Iniciar Anclaje 5-4-3-2-1");
                        break;
                    case 4:
                        holder.btnAccionEjercicioBot.setText("⏱️ Temporizador Pomodoro (15 min)");
                        break;
                    case 5:
                        holder.btnAccionEjercicioBot.setText("💧 Pausa Somática y Estiramiento");
                        break;
                    default:
                        holder.btnAccionEjercicioBot.setText("▶ Iniciar Ejercicio Guiado");
                        break;
                }

                holder.btnAccionEjercicioBot.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onEjercicioClick(msg.getTipoEjercicio());
                    }
                });
            } else {
                holder.btnAccionEjercicioBot.setVisibility(View.GONE);
            }
        } else {
            holder.layoutMensajeBot.setVisibility(View.GONE);
            holder.layoutMensajeUsuario.setVisibility(View.VISIBLE);
            holder.tvTextoUsuario.setText(msg.getTexto());
            holder.tvHoraUsuario.setText(msg.getHora());
        }
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutMensajeBot;
        TextView tvTextoBot;
        TextView tvHoraBot;
        Button btnAccionEjercicioBot;

        LinearLayout layoutMensajeUsuario;
        TextView tvTextoUsuario;
        TextView tvHoraUsuario;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutMensajeBot = itemView.findViewById(R.id.layoutMensajeBot);
            tvTextoBot = itemView.findViewById(R.id.tvTextoBot);
            tvHoraBot = itemView.findViewById(R.id.tvHoraBot);
            btnAccionEjercicioBot = itemView.findViewById(R.id.btnAccionEjercicioBot);

            layoutMensajeUsuario = itemView.findViewById(R.id.layoutMensajeUsuario);
            tvTextoUsuario = itemView.findViewById(R.id.tvTextoUsuario);
            tvHoraUsuario = itemView.findViewById(R.id.tvHoraUsuario);
        }
    }
}
