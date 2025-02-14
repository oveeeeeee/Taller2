package com.example.taller2.activity.administrativo;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.taller2.R;
import com.example.taller2.baseLocal.PedidoRepository;
import com.example.taller2.baseLocal.PiezaRepository;
import com.example.taller2.baseLocal.ProveedorRepository;
import com.example.taller2.modelo.Pedido;
import com.example.taller2.modelo.Pieza;
import com.example.taller2.modelo.Proveedor;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * Actividad para realizar pedidos de piezas a proveedores.
 * Permite seleccionar una pieza, un proveedor y la cantidad deseada,
 * validando los datos antes de registrar el pedido en la base de datos.
 *
 * @author Laura Ovelleiro
 */
public class HacerPedidoActivity extends AppCompatActivity {

    private Spinner spinnerPieza, spinnerProveedor;
    private EditText editCantidad;
    private Button botonHacerPedido;
    private PiezaRepository piezaRepository;
    private ProveedorRepository proveedorRepository;
    private PedidoRepository pedidoRepository;

    /**
     * Método que se ejecuta cuando la actividad es creada.
     * Inicializa los repositorios, los elementos de la interfaz y configura el botón para realizar el pedido.
     *
     * @param savedInstanceState Estado previo de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hacer_pedido);

        // Inicializar los elementos de la interfaz
        spinnerPieza = findViewById(R.id.spinnerPieza);
        spinnerProveedor = findViewById(R.id.spinnerProveedor);
        editCantidad = findViewById(R.id.editCantidad);
        botonHacerPedido = findViewById(R.id.botonHacerPedido);

        // Inicializar los repositorios
        piezaRepository = new PiezaRepository(this);
        proveedorRepository = new ProveedorRepository(this);
        pedidoRepository = new PedidoRepository(this);

        // Abrir la base de datos
        piezaRepository.open();
        proveedorRepository.open();
        pedidoRepository.open();

        // Cargar datos en los Spinners
        cargarPiezas();
        cargarProveedores();

        // Configurar el botón para realizar el pedido
        botonHacerPedido.setOnClickListener(v -> realizarPedido());
    }

    /**
     * Carga la lista de piezas en el Spinner correspondiente.
     */
    private void cargarPiezas() {
        List<Pieza> piezas = piezaRepository.obtenerPiezas();
        ArrayAdapter<Pieza> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, piezas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPieza.setAdapter(adapter);
    }

    /**
     * Carga la lista de proveedores en el Spinner correspondiente.
     */
    private void cargarProveedores() {
        List<Proveedor> proveedores = proveedorRepository.obtenerProveedores();
        ArrayAdapter<Proveedor> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, proveedores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProveedor.setAdapter(adapter);
    }

    /**
     * Realiza un pedido de una pieza a un proveedor.
     * Valida los datos de entrada y, si son correctos, registra el pedido en la base de datos.
     */
    private void realizarPedido() {
        // Validar si se ha seleccionado una pieza
        if (spinnerPieza.getSelectedItem() == null) {
            mostrarMensaje("Seleccione una pieza.");
            return;
        }

        // Validar si se ha seleccionado un proveedor
        if (spinnerProveedor.getSelectedItem() == null) {
            mostrarMensaje("Seleccione un proveedor.");
            return;
        }

        // Validar si se ha introducido la cantidad
        String cantidadStr = editCantidad.getText().toString().trim();
        if (cantidadStr.isEmpty()) {
            mostrarMensaje("Debe introducir una cantidad.");
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
        } catch (NumberFormatException e) {
            mostrarMensaje("Cantidad no válida.");
            return;
        }

        if (cantidad <= 0) {
            mostrarMensaje("La cantidad debe ser mayor a cero.");
            return;
        }

        // Obtener los datos seleccionados (pieza y proveedor)
        Pieza piezaSeleccionada = (Pieza) spinnerPieza.getSelectedItem();
        Proveedor proveedorSeleccionado = (Proveedor) spinnerProveedor.getSelectedItem();

        // Crear el pedido
        Pedido nuevoPedido = new Pedido(piezaSeleccionada, proveedorSeleccionado, cantidad);
        boolean exito = pedidoRepository.realizarPedido(nuevoPedido);

        if (exito) {
            // Actualizar el stock de la pieza
            int nuevoStock = piezaSeleccionada.getCantidadStock() + cantidad;
            boolean stockActualizado = piezaRepository.actualizarStock(piezaSeleccionada.getId(), nuevoStock);

            if (stockActualizado) {
                mostrarMensaje("Pedido realizado y stock actualizado con éxito.");
            } else {
                mostrarMensaje("Pedido realizado, pero error al actualizar el stock.");
            }
        } else {
            mostrarMensaje("Error al realizar el pedido.");
        }
    }

    /**
     * Muestra un mensaje usando un Snackbar.
     *
     * @param mensaje El mensaje a mostrar.
     */
    private void mostrarMensaje(String mensaje) {
        View view = findViewById(android.R.id.content); // Obtiene la vista raíz
        Snackbar.make(view, mensaje, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Método que se ejecuta cuando la actividad es destruida.
     * Cierra las conexiones a la base de datos para evitar fugas de memoria.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        piezaRepository.close();
        proveedorRepository.close();
        pedidoRepository.close();
    }
}