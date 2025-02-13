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
    private SQLiteHelper dbHelper;

    /**
     * Constructor que inicializa el repositorio con el contexto de la aplicación.
     *
     * @param context El contexto de la aplicación.
     */
    public PedidoRepository(Context context) {
        dbHelper = new SQLiteHelper(context);
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
        values.put(SQLiteHelper.COLUMNA_PEDIDO_PIEZA_ID, pedido.getPieza().getId());
        values.put(SQLiteHelper.COLUMNA_PEDIDO_PROVEEDOR_ID, pedido.getProveedor().getId());
        values.put(SQLiteHelper.COLUMNA_PEDIDO_CANTIDAD, pedido.getCantidad());

        long result = database.insert(SQLiteHelper.TABLA_PEDIDOS, null, values);
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
        String query = "SELECT p." + SQLiteHelper.COLUMNA_PEDIDO_ID + " AS pedido_id, "
                + SQLiteHelper.COLUMNA_PEDIDO_PIEZA_ID + ", "
                + SQLiteHelper.COLUMNA_PEDIDO_PROVEEDOR_ID + ", "
                + SQLiteHelper.COLUMNA_PEDIDO_CANTIDAD + ", "
                + SQLiteHelper.COLUMNA_PIEZA_NOMBRE + " AS nombre_pieza, "
                + SQLiteHelper.COLUMNA_PIEZA_CANTIDAD + " AS cantidad_stock, "
                + SQLiteHelper.COLUMNA_PIEZA_PRECIO + ", "
                + SQLiteHelper.COLUMNA_PROVEEDOR_NOMBRE + " AS nombre_proveedor, "
                + SQLiteHelper.COLUMNA_PROVEEDOR_CONTACTO + " AS contacto_proveedor "
                + "FROM " + SQLiteHelper.TABLA_PEDIDOS + " p "
                + "JOIN " + SQLiteHelper.TABLA_PIEZAS + " pz ON p." + SQLiteHelper.COLUMNA_PEDIDO_PIEZA_ID + " = pz." + SQLiteHelper.COLUMNA_PIEZA_ID + " "
                + "JOIN " + SQLiteHelper.TABLA_PROVEEDORES + " pr ON p." + SQLiteHelper.COLUMNA_PEDIDO_PROVEEDOR_ID + " = pr." + SQLiteHelper.COLUMNA_PROVEEDOR_ID;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int pedidoId = cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMNA_PEDIDO_ID));
                int piezaId = cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMNA_PEDIDO_PIEZA_ID));
                String nombrePieza = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMNA_PIEZA_NOMBRE));
                int cantidadStock = cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMNA_PIEZA_CANTIDAD));
                double precio = cursor.getDouble(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMNA_PIEZA_PRECIO));

                int proveedorId = cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMNA_PEDIDO_PROVEEDOR_ID));
                String nombreProveedor = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMNA_PROVEEDOR_NOMBRE));
                String contactoProveedor = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMNA_PROVEEDOR_CONTACTO));

                int cantidad = cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMNA_PEDIDO_CANTIDAD));

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
