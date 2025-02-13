package com.example.taller2.baseLocal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.taller2.modelo.Pieza;

import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para manejar las operaciones de la base de datos relacionadas con las piezas.
 * Proporciona métodos para obtener las piezas, actualizar el stock, entre otros.
 *
 * @author Laura Ovelleiro
 */
public class PiezaRepository {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    /**
     * Constructor que inicializa el repositorio con el contexto de la aplicación.
     *
     * @param context El contexto de la aplicación.
     */
    public PiezaRepository(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    /**
     * Abre la base de datos en modo escritura.
     *
     * @throws SQLException Si no se puede abrir la base de datos.
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Cierra la base de datos y el helper.
     */
    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
        dbHelper.close();
    }

    /**
     * Obtiene todas las piezas desde la base de datos.
     *
     * @return Una lista de piezas.
     */
    public List<Pieza> obtenerPiezas() {
        List<Pieza> piezas = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = database.query(
                    "piezas",
                    new String[]{"id", "nombre", "cantidad_stock", "precio"},
                    null, null, null, null, null
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                    int cantidadStock = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad_stock"));
                    double precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"));

                    piezas.add(new Pieza(id, nombre, cantidadStock, precio));
                } while (cursor.moveToNext());
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return piezas;
    }

    /**
     * Actualiza el stock de una pieza en la base de datos.
     *
     * @param piezaId    El ID de la pieza que se va a actualizar.
     * @param nuevoStock El nuevo valor de stock para la pieza.
     * @return true si la actualización fue exitosa, false si no lo fue.
     */
    public boolean actualizarStock(int piezaId, int nuevoStock) {
        ContentValues values = new ContentValues();
        values.put("cantidad_stock", nuevoStock);

        int rowsAffected = database.update(
                "piezas",
                values,
                "id = ?",
                new String[]{String.valueOf(piezaId)}
        );

        return rowsAffected > 0;
    }
}
