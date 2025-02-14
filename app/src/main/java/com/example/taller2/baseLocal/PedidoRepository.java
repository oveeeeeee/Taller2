package com.example.taller2.baseLocal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.taller2.modelo.Pedido;
import com.example.taller2.modelo.Pieza;
import com.example.taller2.modelo.Proveedor;

import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para gestionar los pedidos en la base de datos.
 * Permite realizar operaciones sobre los pedidos como insertar nuevos pedidos y obtener una lista de todos los pedidos.
 *
 * @author Laura Ovelleiro
 */
public class PedidoRepository {

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    /**
     * Constructor que inicializa el repositorio con el contexto de la aplicación.
     *
     * @param context El contexto de la aplicación.
     */
    public PedidoRepository(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * Abre la base de datos en modo escritura.
     *
     * @throws SQLException Si hay un error al abrir la base de datos.
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Cierra la base de datos.
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Realiza un pedido insertando los datos en la tabla "Pedidos".
     *
     * @param pedido El objeto Pedido que contiene la información a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean realizarPedido(Pedido pedido) {
        ContentValues values = new ContentValues();
        values.put("pieza_id", pedido.getPieza().getId());
        values.put("proveedor_id", pedido.getProveedor().getId());
        values.put("cantidad", pedido.getCantidad());

        long result = database.insert("Pedidos", null, values);
        return result != -1; // Si la inserción fue exitosa, devuelve true
    }

    /**
     * Obtiene todos los pedidos almacenados en la base de datos.
     * Realiza una consulta JOIN entre las tablas "Pedidos", "Piezas" y "Proveedores" para obtener
     * la información relacionada.
     *
     * @return Una lista de objetos Pedido con la información obtenida de la base de datos.
     */
    public List<Pedido> obtenerPedidos() {
        List<Pedido> pedidos = new ArrayList<>();

        // Query para obtener los pedidos con la información de la pieza y el proveedor
        String query = "SELECT p.id AS pedido_id, pieza_id, proveedor_id, cantidad, " +
                "pz.nombre AS nombre_pieza, pz.cantidad_stock, " +
                "pr.nombre AS nombre_proveedor " +
                "FROM Pedidos p " +
                "JOIN Piezas pz ON p.pieza_id = pz.id " +
                "JOIN Proveedores pr ON p.proveedor_id = pr.id";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int pedidoId = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PEDIDO_ID));
                int piezaId = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PIEZA_ID));
                String nombrePieza = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PIEZA_NOMBRE));
                int cantidadStock = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PIEZA_CANTIDAD));
                double precio = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PIEZA_PRECIO));

                int proveedorId = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PROVEEDOR_ID));
                String nombreProveedor = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PROVEEDOR_NOMBRE));
                String contactoProveedor = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PROVEEDOR_CONTACTO));

                int cantidad = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PEDIDO_CANTIDAD));

                // Creación de los objetos Pieza, Proveedor y Pedido
                Pieza pieza = new Pieza(piezaId, nombrePieza, cantidadStock, precio);
                Proveedor proveedor = new Proveedor(proveedorId, nombreProveedor, contactoProveedor);
                Pedido pedido = new Pedido(pedidoId, pieza, proveedor, cantidad);

                pedidos.add(pedido); // Añadir el pedido a la lista
            } while (cursor.moveToNext());

            cursor.close();
        }

        return pedidos;
    }
}
