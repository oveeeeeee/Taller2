package com.example.taller2.baseLocal;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.taller2.modelo.Proveedor;

import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio encargado de interactuar con la base de datos para gestionar los proveedores.
 * Proporciona métodos para obtener la lista de proveedores almacenados en la base de datos.
 *
 * @author Laura Ovelleiro
 */
public class ProveedorRepository {

    private SQLiteDatabase database;  // Instancia de la base de datos
    private final DBHelper dbHelper;  // Helper para manejar la base de datos

    /**
     * Constructor de la clase ProveedorRepository.
     *
     * @param context Contexto de la aplicación.
     */
    public ProveedorRepository(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * Abre la base de datos en modo escritura.
     *
     * @throws SQLException Si ocurre un error al abrir la base de datos.
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Cierra la conexión con la base de datos.
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Obtiene todos los proveedores almacenados en la base de datos.
     *
     * @return Una lista de proveedores.
     */
    public List<Proveedor> obtenerProveedores() {
        List<Proveedor> proveedores = new ArrayList<>();
        Cursor cursor = database.query("Proveedores", null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Obtener los valores de las columnas
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PROVEEDOR_ID));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PROVEEDOR_NOMBRE));
                String contacto = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PROVEEDOR_CONTACTO));

                // Crear un nuevo proveedor y agregarlo a la lista
                proveedores.add(new Proveedor(id, nombre, contacto));
            } while (cursor.moveToNext());

            cursor.close();
        }

        return proveedores;
    }
}
