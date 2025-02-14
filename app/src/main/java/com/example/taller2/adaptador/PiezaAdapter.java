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
 * Este adaptador maneja la lista de piezas y permite filtrar los resultados.
 *
 * @author Laura Ovelleiro
 */
public class PiezaAdapter extends RecyclerView.Adapter<PiezaAdapter.ViewHolder> {

    // Lista de piezas a mostrar en el RecyclerView
    private List<Pieza> listaPiezas;
    private List<Pieza> listaPiezasOriginal; // Lista original para filtrar

    /**
     * Constructor del adaptador para inicializar la lista de piezas.
     *
     * @param listaPiezas Lista de piezas a mostrar.
     */
    public PiezaAdapter(List<Pieza> listaPiezas) {
        this.listaPiezas = new ArrayList<>(listaPiezas);
        this.listaPiezasOriginal = new ArrayList<>(listaPiezas);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pieza, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Asocia los datos de una pieza a los elementos de la vista en el ViewHolder.
     *
     * @param holder   El ViewHolder que contiene las vistas a actualizar.
     * @param position La posición de la pieza en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Pieza pieza = listaPiezas.get(position);

            if (pieza != null) {
                holder.textNombre.setText(pieza.getNombre());
                holder.textCantidad.setText("Disponible: " + pieza.getCantidadStock() + " unidades");
                holder.textPrecio.setText("Precio: " + pieza.getPrecio() + " €");
            }
        } catch (Exception e) {
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
        return listaPiezas != null ? listaPiezas.size() : 0;
    }

    /**
     * Filtra la lista de piezas según el texto ingresado en el SearchView.
     *
     * @param texto El texto de búsqueda.
     */
    public void filtrar(String texto) {
        List<Pieza> listaFiltrada = new ArrayList<>();

        if (texto.isEmpty()) {
            listaFiltrada.addAll(listaPiezasOriginal); // Si el texto está vacío, mostramos todos los elementos
        } else {
            String textoLower = texto.toLowerCase();
            for (Pieza pieza : listaPiezasOriginal) {
                if (pieza.getNombre().toLowerCase().contains(textoLower)) {
                    listaFiltrada.add(pieza);
                }
            }
        }

        listaPiezas.clear();
        listaPiezas.addAll(listaFiltrada);
        notifyDataSetChanged(); // Notifica al RecyclerView que los datos han cambiado
    }

    /**
     * ViewHolder para contener las vistas de cada elemento de la lista.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre, textCantidad, textPrecio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.textNombrePieza);
            textCantidad = itemView.findViewById(R.id.textCantidadPieza);
            textPrecio = itemView.findViewById(R.id.textPrecioPieza);
        }
    }
}
