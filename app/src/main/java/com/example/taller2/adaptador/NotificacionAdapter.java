package com.example.taller2.adaptador;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taller2.R;
import com.example.taller2.modelo.Notificacion;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Adapter para mostrar las notificaciones en un RecyclerView.
 * Cada notificación muestra la matrícula, descripción del presupuesto e importe.
 * Permite gestionar el presupuesto, aceptándolo o rechazándolo.
 *
 * @author Laura Ovelleiro
 */
public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.ViewHolder> {

    private List<Notificacion> listaNotificaciones;
    private Context contexto;

    /**
     * Constructor para inicializar el adapter.
     *
     * @param listaNotificaciones Lista de notificaciones a mostrar.
     * @param contexto Contexto de la actividad o fragmento.
     */
    public NotificacionAdapter(List<Notificacion> listaNotificaciones, Context contexto) {
        this.listaNotificaciones = listaNotificaciones;
        this.contexto = contexto;
    }

    /**
     * Crea una nueva vista (ViewHolder) para un item del RecyclerView.
     *
     * @param parent Vista padre del RecyclerView.
     * @param viewType Tipo de vista (no utilizado en este caso).
     * @return Un nuevo ViewHolder con la vista inflada.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.item_notificacion, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Asocia los datos de la notificación a las vistas del ViewHolder.
     *
     * @param holder ViewHolder que contiene las vistas.
     * @param position Posición del item en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notificacion notificacion = listaNotificaciones.get(position);
        holder.textMatricula.setText("Matrícula: " + notificacion.getMatricula());
        holder.textDescripcion.setText("Descripción: " + notificacion.getDescripcionPresupuesto());
        holder.textImporte.setText("Importe: " + notificacion.getImportePresupuesto() + " €");

        // Configuración de botones para aceptar o rechazar el presupuesto
        holder.btnAceptar.setOnClickListener(v -> gestionarPresupuesto(holder, notificacion, "aceptado"));
        holder.btnRechazar.setOnClickListener(v -> gestionarPresupuesto(holder, notificacion, "rechazado"));
    }

    /**
     * Devuelve la cantidad total de notificaciones en la lista.
     *
     * @return El tamaño de la lista de notificaciones.
     */
    @Override
    public int getItemCount() {
        return listaNotificaciones.size();
    }

    /**
     * Clase interna que representa un ViewHolder para las notificaciones.
     * Contiene las vistas de cada item de la lista.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textMatricula, textDescripcion, textImporte;
        Button btnAceptar, btnRechazar;

        /**
         * Constructor para inicializar las vistas del ViewHolder.
         *
         * @param itemView Vista inflada para cada item de la lista.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textMatricula = itemView.findViewById(R.id.text_matricula);
            textDescripcion = itemView.findViewById(R.id.text_descripcion);
            textImporte = itemView.findViewById(R.id.text_importe);
            btnAceptar = itemView.findViewById(R.id.btn_aceptar);
            btnRechazar = itemView.findViewById(R.id.btn_rechazar);
        }
    }

    /**
     * Gestiona el presupuesto de la notificación, ya sea aceptado o rechazado.
     *
     * @param holder El ViewHolder donde están los botones.
     * @param notificacion La notificación cuyo presupuesto se va a gestionar.
     * @param estado El estado a asignar al presupuesto (aceptado o rechazado).
     */
    private void gestionarPresupuesto(ViewHolder holder, Notificacion notificacion, String estado) {
        // Actualiza el estado en la base de datos
        DatabaseReference notificacionRef = FirebaseDatabase.getInstance().getReference("notificaciones");
        notificacionRef.child(notificacion.getMatricula()).child("estado").setValue(estado);

        // Muestra el mensaje de confirmación con Snackbar
        String mensaje = "Presupuesto " + estado;
        Snackbar.make(holder.itemView, mensaje, Snackbar.LENGTH_SHORT).show();

        // Oculta los botones después de aceptar o rechazar
        holder.btnAceptar.setVisibility(View.GONE);
        holder.btnRechazar.setVisibility(View.GONE);
    }
}
