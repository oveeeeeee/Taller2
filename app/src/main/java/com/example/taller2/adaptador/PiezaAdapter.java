package com.example.taller2.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taller2.R;
import com.example.taller2.modelo.Pieza;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador para mostrar las piezas en un RecyclerView.
 * Este adaptador maneja la lista de piezas y las vincula con las vistas correspondientes.
 *
 * @author Laura Ovelleiro
 */
public class PiezaAdapter extends RecyclerView.Adapter<PiezaAdapter.ViewHolder> {

    // Lista de piezas a mostrar en el RecyclerView
    private List<Pieza> piezas;
    private List<Pieza> piezasFiltradas;

    /**
     * Constructor del adaptador para inicializar la lista de piezas.
     *
     * @param listaPiezas Lista de piezas a mostrar.
     */
    public PiezaAdapter(List<Pieza> listaPiezas) {
        this.piezas = listaPiezas;
        this.piezasFiltradas = new ArrayList<>(listaPiezas);
    }

    /**
     * Crea el ViewHolder y vincula el layout item_pieza al RecyclerView.
     *
     * @param parent El grupo de vista al cual se añadirá la nueva vista.
     * @param viewType El tipo de vista (no se utiliza en este caso).
     * @return Un nuevo ViewHolder con la vista inflada.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout item_pieza y lo devuelve como ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pieza, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Asocia los datos de una pieza a los elementos de la vista en el ViewHolder.
     *
     * @param holder El ViewHolder que contiene las vistas a actualizar.
     * @param position La posición de la pieza en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            // Obtiene la pieza de la lista en la posición indicada
            Pieza pieza = piezasFiltradas.get(position);

            if (pieza != null) {
                // Asigna los datos de la pieza a las vistas correspondientes
                holder.textNombre.setText(pieza.getNombre());
                holder.textCantidad.setText("Disponible: " + pieza.getCantidadStock() + " unidades");
                holder.textPrecio.setText("Precio: " + pieza.getPrecio() + " €");
            }
        } catch (Exception e) {
            // En caso de error, imprime el stack trace
            e.printStackTrace();
        }
    }

    /**
     * Devuelve el número total de elementos en la lista de piezas.
     *
     * @return El tamaño de la lista de piezas o 0 si la lista es nula.
     */
    @Override
    public int getItemCount() {
        return piezasFiltradas != null ? piezasFiltradas.size() : 0;
    }

    /**
     * Filtra las piezas según el término de búsqueda.
     *
     * @param query El término de búsqueda que se utilizará para filtrar las piezas.
     */
    public void filtrar(String query) {
        if (query.isEmpty()) {
            piezasFiltradas = new ArrayList<>(piezas);
        } else {
            List<Pieza> listaFiltrada = new ArrayList<>();
            for (Pieza pieza : piezas) {
                if (pieza.getNombre().toLowerCase().contains(query.toLowerCase())) {
                    listaFiltrada.add(pieza);
                }
            }
            piezasFiltradas = listaFiltrada;
        }
        notifyDataSetChanged();
    }

    /**
     * ViewHolder para contener las vistas de cada elemento de la lista.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Vistas para mostrar los datos de la pieza
        TextView textNombre, textCantidad, textPrecio;

        /**
         * Constructor del ViewHolder que inicializa las vistas.
         *
         * @param itemView La vista del item que contiene los elementos visuales.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializa las vistas utilizando findViewById
            textNombre = itemView.findViewById(R.id.textNombrePieza);
            textCantidad = itemView.findViewById(R.id.textCantidadPieza);
            textPrecio = itemView.findViewById(R.id.textPrecioPieza);
        }
    }
}