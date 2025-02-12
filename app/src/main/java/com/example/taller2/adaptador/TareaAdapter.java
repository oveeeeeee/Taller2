package com.example.taller2.adaptador;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taller2.R;
import com.example.taller2.modelo.Tarea;

import java.util.List;

/**
 * Adapter para gestionar la lista de tareas en un RecyclerView.
 * Permite navegar a la actividad de opciones de la tarea al hacer clic en un item.
 *
 * @author Laura Ovelleiro
 */
public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    private List<Tarea> listaTareas; // Lista de tareas
    private Context contexto; // Contexto para inflar vistas

    /**
     * Constructor para inicializar el adaptador.
     *
     * @param listaTareas Lista de tareas a mostrar en el RecyclerView.
     * @param contexto Contexto de la actividad o fragmento donde se usa el RecyclerView.
     */
    public TareaAdapter(List<Tarea> listaTareas, Context contexto) {
        this.listaTareas = listaTareas;
        this.contexto = contexto;
    }

    /**
     * Crea una nueva vista para cada ítem del RecyclerView.
     *
     * @param parent Vista del RecyclerView que contendrá cada ítem.
     * @param viewType Tipo de vista (no utilizado en este caso).
     * @return Un nuevo ViewHolder con la vista inflada.
     */
    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout para cada tarea
        View vista = LayoutInflater.from(contexto).inflate(R.layout.item_tarea, parent, false);
        return new TareaViewHolder(vista);
    }

    /**
     * Asocia los datos de la tarea a las vistas del ViewHolder.
     *
     * @param holder ViewHolder que contiene las vistas.
     * @param position Posición del item en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        // Obtener la tarea
        Tarea tarea = listaTareas.get(position);

        // Configurar los textos de los TextView
        holder.textNombreTarea.setText("Tarea: " + tarea.getNombreTarea());
        holder.textMatriculaCoche.setText("Matricula: " + tarea.getMatriculaCoche());
        holder.textEstadoTarea.setText("Estado: " + tarea.getEstadoTarea());

        // Configurar el clic en el item
        holder.itemView.setOnClickListener(v -> {
            // Crear el Intent para ir a TareaOpcionesActivity
            Intent intent = new Intent(contexto, TareaOpcionesActivity.class);

            // Pasar la información de la tarea (por ejemplo, su ID o nombre)
            intent.putExtra("TAREA_ID", tarea.getId());

            // Iniciar la actividad
            contexto.startActivity(intent);
        });
    }

    /**
     * Devuelve el número total de tareas en la lista.
     *
     * @return El tamaño de la lista de tareas.
     */
    @Override
    public int getItemCount() {
        return listaTareas.size(); // Número total de tareas
    }

    /**
     * ViewHolder para almacenar las vistas de cada ítem en la lista de tareas.
     */
    public static class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView textNombreTarea;
        TextView textEstadoTarea;
        TextView textMatriculaCoche;

        /**
         * Constructor para inicializar las vistas del ViewHolder.
         *
         * @param itemView Vista inflada para cada ítem de la lista.
         */
        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            // Asociar las vistas del layout
            textNombreTarea = itemView.findViewById(R.id.textNombreTarea);
            textEstadoTarea = itemView.findViewById(R.id.textEstadoTarea);
            textMatriculaCoche = itemView.findViewById(R.id.textMatriculaCoche);
        }
    }
}
