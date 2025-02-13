package com.example.taller2.activity.administrativo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taller2.R;
import com.example.taller2.baseLocal.PedidoRepository;
import com.example.taller2.baseLocal.PiezaRepository;
import com.example.taller2.baseLocal.ProveedorRepository;
import com.example.taller2.modelo.Pedido;
import com.example.taller2.modelo.Pieza;
import com.example.taller2.modelo.Proveedor;
import com.google.android.material.snackbar.Snackbar;


/**
 * Actividad para realizar un pedido de piezas a proveedores.
 * Permite seleccionar una pieza y proveedor, ingresar la cantidad y realizar el pedido.
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
     * Método que se llama cuando la actividad es creada.
     * Inicializa los repositorios, los elementos de la interfaz y configura el botón para realizar el pedido.
     *
     * @param savedInstanceState Información sobre el estado previo de la actividad.
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

        // Configurar el botón para realizar el pedido
        botonHacerPedido.setOnClickListener(v -> realizarPedido());
    }

    /**
     * Método para realizar el pedido de una pieza a un proveedor.
     * Valida la entrada del usuario, crea un nuevo pedido y actualiza el stock de la pieza.
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
            int nuevoStock = piezaSeleccionada.getCantidadStock() - cantidad;
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
     * Método para mostrar un mensaje usando Snackbar.
     *
     * @param mensaje El mensaje a mostrar.
     */
    private void mostrarMensaje(String mensaje) {
        // Mostrar mensaje usando Snackbar
        View view = findViewById(android.R.id.content); // Obtiene la vista raíz
        Snackbar.make(view, mensaje, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Método llamado cuando la actividad es destruida.
     * Cierra las conexiones a la base de datos.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cerrar la base de datos al destruir la actividad
        piezaRepository.close();
        proveedorRepository.close();
        pedidoRepository.close();
    }
}
