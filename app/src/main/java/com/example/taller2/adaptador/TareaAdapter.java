package com.example.taller2.adaptador;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taller2.R;
import com.example.taller2.activity.mecanico.TareaOpcionesActivity;
import com.example.taller2.modelo.Tarea;

import java.util.List;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    private List<Tarea> listaTareas;
    private Context contexto;

    public TareaAdapter(List<Tarea> listaTareas, Context contexto) {
        this.listaTareas = listaTareas;
        this.contexto = contexto;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(contexto).inflate(R.layout.item_tarea, parent, false);
        return new TareaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        Tarea tarea = listaTareas.get(position);
        String tareaId = tarea.getId();

        holder.textNombreTarea.setText("Tarea: " + tarea.getNombreTarea());
        holder.textMatriculaCoche.setText("Matricula: " + tarea.getMatriculaCoche());
        holder.textEstadoTarea.setText("Estado: " + tarea.getEstadoTarea());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(contexto, TareaOpcionesActivity.class);
            intent.putExtra("TAREA_ID", tareaId);
            contexto.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    public static class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView textNombreTarea;
        TextView textEstadoTarea;
        TextView textMatriculaCoche;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombreTarea = itemView.findViewById(R.id.textNombreTarea);
            textEstadoTarea = itemView.findViewById(R.id.textEstadoTarea);
            textMatriculaCoche = itemView.findViewById(R.id.textMatriculaCoche);
        }
    }
}
