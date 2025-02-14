package com.example.taller2.baseLocal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.taller2.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase SQLiteHelper para gestionar la base de datos de usuarios.
 * Proporciona métodos para agregar y obtener usuarios en la base de datos.
 *
 * @autor Laura Ovelleiro
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "taller.db"; // Nombre de la base de datos
    private static final int DATABASE_VERSION = 1;

    // Tabla de usuarios
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_USER_TYPE = "userType";

    /**
     * Constructor de la clase SQLiteHelper.
     *
     * @param context Contexto de la aplicación.
     */
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Se ejecuta cuando la base de datos es creada por primera vez.
     *
     * @param db Instancia de la base de datos.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla de usuarios
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT PRIMARY KEY,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_USER_TYPE + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);
    }

    /**
     * Se ejecuta cuando se actualiza la base de datos.
     *
     * @param db Instancia de la base de datos.
     * @param oldVersion Versión anterior de la base de datos.
     * @param newVersion Nueva versión de la base de datos.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    /**
     * Método para insertar un nuevo usuario en la base de datos.
     *
     * @param user El objeto User que representa al nuevo usuario.
     * @return true si la inserción fue exitosa, false si falló.
     */
    public boolean anadirUsuario(Usuario user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getNombre());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_TYPE, user.getTipo());

        // Insertar en la tabla de usuarios
        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        // Si el resultado es mayor que -1, la inserción fue exitosa
        return result != -1;
    }

    /**
     * Método para obtener un usuario por su correo electrónico.
     *
     * @param email Correo electrónico del usuario.
     * @return El objeto User si se encuentra, null si no se encuentra.
     */
    public Usuario getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_EMAIL + "=?", new String[]{email},
                null, null, null);

        // Verificar si el cursor tiene datos
        if (cursor != null && cursor.moveToFirst()) {
            // Comprobar si las columnas existen
            int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
            int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
            int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
            int userTypeIndex = cursor.getColumnIndex(COLUMN_USER_TYPE);

            // Verificar si los índices son válidos
            if (nameIndex >= 0 && emailIndex >= 0 && passwordIndex >= 0 && userTypeIndex >= 0) {
                Usuario user = new Usuario(
                        cursor.getString(nameIndex),
                        cursor.getString(emailIndex),
                        cursor.getString(passwordIndex),
                        cursor.getString(userTypeIndex)
                );
                cursor.close();
                return user;
            } else {
                cursor.close();
                return null; // Si alguna columna no se encuentra, retornar null
            }
        } else {
            cursor.close();
            return null; // Si no se encuentran resultados, retornar null
        }
    }
}
