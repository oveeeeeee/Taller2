package com.example.taller2.activity.mecanico;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taller2.R;
import com.example.taller2.baseLocal.DBHelper;
import com.example.taller2.baseLocal.SQLiteHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


/**
 * Actividad para solicitar piezas en el taller.
 * Permite seleccionar una pieza, indicar la cantidad y registrar el pedido en la base de datos.
 *
 * @autor Laura Ovelleiro
 */
public class SolicitarPiezasActivity extends AppCompatActivity {

    private Spinner spinnerPiezas;
    private EditText editCantidad;
    private Button btnSolicitar;
    private DBHelper dbHelper;
    private String piezaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_piezas);

        dbHelper = new DBHelper(this);

        spinnerPiezas = findViewById(R.id.spinnerPiezas);
        editCantidad = findViewById(R.id.editCantidad);
        btnSolicitar = findViewById(R.id.btnSolicitar);

        cargarPiezasEnSpinner();

        // Detecta la selección de pieza
        spinnerPiezas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                piezaSeleccionada = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                piezaSeleccionada = null;
            }
        });

        // Acción al pulsar "Solicitar"
        btnSolicitar.setOnClickListener(v -> solicitarPieza());
    }

    /**
     * Carga las piezas almacenadas en la base de datos y las muestra en el Spinner.
     */
    private void cargarPiezasEnSpinner() {
        List<String> piezas = dbHelper.obtenerPiezas();

        if (piezas.isEmpty()) {
            piezas.add("No hay piezas disponibles");
            btnSolicitar.setEnabled(false);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, piezas);
        spinnerPiezas.setAdapter(adapter);
    }

    /**
     * Registra el pedido en la base de datos.
     */
    private void solicitarPieza() {
        String cantidadTexto = editCantidad.getText().toString().trim();

        if (piezaSeleccionada == null || cantidadTexto.isEmpty()) {
            mostrarSnackbar("Seleccione una pieza y una cantidad válida");
            return;
        }

        int cantidad = Integer.parseInt(cantidadTexto);
        if (cantidad <= 0) {
            mostrarSnackbar("La cantidad debe ser mayor a 0");
            return;
        }

        dbHelper.insertarPedido(piezaSeleccionada, cantidad);
        mostrarSnackbar("Pedido realizado con éxito");
        editCantidad.setText(""); // Limpia el campo
    }

    /**
     * Muestra un mensaje usando Snackbar.
     *
     * @param mensaje Texto del mensaje.
     */
    private void mostrarSnackbar(String mensaje) {
        Snackbar.make(findViewById(android.R.id.content), mensaje, Snackbar.LENGTH_SHORT).show();
    }
}