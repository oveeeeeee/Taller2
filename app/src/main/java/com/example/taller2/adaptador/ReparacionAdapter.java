package com.example.taller2.adaptador;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taller2.R;
import com.example.taller2.modelo.Reparacion;
import com.example.taller2.modelo.Tarea;

import java.util.List;

/**
 * Adapter para gestionar la lista de reparaciones en un RecyclerView.
 * Además, maneja la visualización de las tareas asociadas a cada reparación.
 *
 * @author Laura Ovelleiro
 */
public class ReparacionAdapter extends RecyclerView.Adapter<ReparacionAdapter.ReparacionViewHolder> {

    // Interfaz para manejar clics en elementos de la lista
    public interface OnReparacionClickListener {
        void onReparacionClick(Reparacion reparacion);
    }

    private List<Reparacion> listaReparaciones; // Lista de reparaciones a mostrar
    private Context contexto; // Contexto de la actividad
    private OnReparacionClickListener listener; // Listener para manejar eventos de clic

    /**
     * Constructor del adaptador.
     *
     * @param listaReparaciones Lista de reparaciones a mostrar en el RecyclerView.
     * @param contexto Contexto de la actividad o fragmento donde se usa el RecyclerView.
     * @param listener Listener que maneja el evento de clic en las reparaciones.
     */
    public ReparacionAdapter(List<Reparacion> listaReparaciones, Context contexto, OnReparacionClickListener listener) {
        this.listaReparaciones = listaReparaciones;
        this.contexto = contexto;
        this.listener = listener;
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
    public ReparacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar la vista de cada elemento
        View vista = LayoutInflater.from(contexto).inflate(R.layout.item_reparacion, parent, false);
        return new ReparacionViewHolder(vista);
    }

    /**
     * Asocia los datos de la reparación a las vistas del ViewHolder.
     *
     * @param holder ViewHolder que contiene las vistas.
     * @param position Posición del item en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull ReparacionViewHolder holder, int position) {
        // Obtener la reparación actual
        Reparacion reparacion = listaReparaciones.get(position);

        // Configurar los valores en las vistas
        holder.textMatriculaCoche.setText("Matrícula: " + reparacion.getMatriculaCoche());
        holder.textMecanicoAsignado.setText("Mecánico: " + reparacion.getMecanicoAsignado());
        holder.textDescripcionReparacion.setText("Descripción: " + reparacion.getDescripcionReparacion());
        holder.textEstadoReparacion.setText("Estado: " + reparacion.getEstadoReparacion());

        // Mostrar las tareas asociadas si existen
        List<Tarea> tareas = reparacion.getListaTareas();
        if (tareas != null && !tareas.isEmpty()) {
            holder.recyclerTareas.setVisibility(View.VISIBLE);
            TareaAdapter tareaAdapter = new TareaAdapter(tareas, contexto); // Pasamos ambos parámetros
            holder.recyclerTareas.setLayoutManager(new LinearLayoutManager(contexto));
            holder.recyclerTareas.setAdapter(tareaAdapter);
        } else {
            holder.recyclerTareas.setVisibility(View.GONE);
        }

        // Configurar el clic en el elemento de la lista
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReparacionClick(reparacion); // Notificar al listener
            }
        });
    }

    /**
     * Devuelve la cantidad total de reparaciones en la lista.
     *
     * @return El tamaño de la lista de reparaciones.
     */
    @Override
    public int getItemCount() {
        return listaReparaciones.size(); // Número total de elementos
    }

    /**
     * ViewHolder para almacenar las vistas de cada ítem en la lista de reparaciones.
     */
    public static class ReparacionViewHolder extends RecyclerView.ViewHolder {
        TextView textMatriculaCoche, textMecanicoAsignado, textDescripcionReparacion;
        TextView textEstadoReparacion;
        RecyclerView recyclerTareas;

        /**
         * Constructor para inicializar las vistas del ViewHolder.
         *
         * @param itemView Vista inflada para cada ítem de la lista.
         */
        public ReparacionViewHolder(@NonNull View itemView) {
            super(itemView);

            // Vincular las vistas del layout
            textMatriculaCoche = itemView.findViewById(R.id.textMatriculaCoche);
            textMecanicoAsignado = itemView.findViewById(R.id.textMecanicoAsignado);
            textDescripcionReparacion = itemView.findViewById(R.id.textDescripcionReparacion);
            textEstadoReparacion = itemView.findViewById(R.id.textEstadoReparacion);
            recyclerTareas = itemView.findViewById(R.id.recyclerTareas);
        }
    }
}
