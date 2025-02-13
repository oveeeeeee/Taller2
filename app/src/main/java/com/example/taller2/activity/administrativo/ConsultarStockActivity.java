package com.example.taller2.activity.administrativo;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taller2.R;
import com.example.taller2.adaptador.PiezaAdapter;
import com.example.taller2.baseLocal.PiezaRepository;
import com.example.taller2.modelo.Pieza;

import java.util.List;


/**
 * Actividad para consultar el stock de piezas.
 * Permite visualizar las piezas en stock en un RecyclerView y filtrarlas usando un SearchView.
 *
 * @author Laura Ovelleiro
 */
public class ConsultarStockActivity extends AppCompatActivity {

    private PiezaRepository piezaRepository;
    private RecyclerView recyclerViewPiezas;
    private PiezaAdapter piezaAdapter;
    private SearchView searchViewPiezas;

    /**
     * Método que se llama cuando la actividad es creada.
     * Inicializa la base de datos, el RecyclerView y el SearchView.
     * Luego consulta las piezas disponibles y configura los adaptadores y listeners.
     *
     * @param savedInstanceState Información sobre el estado previo de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_stock);

        // Inicializar la base de datos y el RecyclerView
        piezaRepository = new PiezaRepository(this);
        recyclerViewPiezas = findViewById(R.id.recyclerViewPiezas);
        searchViewPiezas = findViewById(R.id.searchViewPiezas);

        // Abrir la base de datos
        piezaRepository.open();

        // Consultar las piezas
        List<Pieza> piezas = piezaRepository.obtenerPiezas();

        // Configurar el RecyclerView
        piezaAdapter = new PiezaAdapter(piezas);
        recyclerViewPiezas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPiezas.setAdapter(piezaAdapter);

        // Configurar el SearchView
        searchViewPiezas.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            /**
             * Método que se llama cuando el texto es enviado (no utilizado en este caso).
             *
             * @param query El texto que fue enviado.
             * @return true si se manejó la acción, false en caso contrario.
             */
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            /**
             * Método que se llama cuando el texto del SearchView cambia.
             * Filtra las piezas mostradas en el RecyclerView según el texto ingresado.
             *
             * @param newText El nuevo texto ingresado.
             * @return true si se manejó el cambio de texto, false en caso contrario.
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                piezaAdapter.filtrar(newText);
                return true;
            }
        });

        // Cerrar la base de datos
        piezaRepository.close();
    }
}
