package com.example.taller2.baseLocal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.taller2.modelo.Usuario;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String NOMBRE_BD = "taller2.db";
    private static final int VERSION_BD = 1;

    // Tablas
    private static final String TABLA_USUARIOS = "usuarios";
    private static final String TABLA_PROVEEDORES = "proveedores";
    private static final String TABLA_PIEZAS = "piezas";
    private static final String TABLA_PEDIDOS = "pedidos";

    // Columnas de la tabla usuarios
    private static final String COLUMNA_USUARIO_NOMBRE = "nombre";
    private static final String COLUMNA_USUARIO_CORREO = "correo";
    private static final String COLUMNA_USUARIO_CONTRASENA = "contrasena";
    private static final String COLUMNA_USUARIO_TIPO = "tipo_usuario";

    // Columnas de la tabla proveedores
    private static final String COLUMNA_PROVEEDOR_ID = "id";
    private static final String COLUMNA_PROVEEDOR_NOMBRE = "nombre";
    private static final String COLUMNA_PROVEEDOR_CONTACTO = "contacto";

    // Columnas de la tabla piezas
    private static final String COLUMNA_PIEZA_ID = "id";
    private static final String COLUMNA_PIEZA_NOMBRE = "nombre";
    private static final String COLUMNA_PIEZA_CANTIDAD = "cantidad_stock";
    private static final String COLUMNA_PIEZA_PRECIO = "precio";

    // Columnas de la tabla pedidos
    private static final String COLUMNA_PEDIDO_ID = "id";
    private static final String COLUMNA_PEDIDO_PIEZA_ID = "pieza_id";
    private static final String COLUMNA_PEDIDO_PROVEEDOR_ID = "proveedor_id";
    private static final String COLUMNA_PEDIDO_CANTIDAD = "cantidad";

    public SQLiteHelper(Context contexto) {
        super(contexto, NOMBRE_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String crearTablaUsuarios = "CREATE TABLE " + TABLA_USUARIOS + " ("
                + COLUMNA_USUARIO_NOMBRE + " TEXT, "
                + COLUMNA_USUARIO_CORREO + " TEXT PRIMARY KEY, "
                + COLUMNA_USUARIO_CONTRASENA + " TEXT, "
                + COLUMNA_USUARIO_TIPO + " TEXT)";
        db.execSQL(crearTablaUsuarios);

        String crearTablaProveedores = "CREATE TABLE " + TABLA_PROVEEDORES + " ("
                + COLUMNA_PROVEEDOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMNA_PROVEEDOR_NOMBRE + " TEXT NOT NULL, "
                + COLUMNA_PROVEEDOR_CONTACTO + " TEXT NOT NULL)";
        db.execSQL(crearTablaProveedores);

        String crearTablaPiezas = "CREATE TABLE " + TABLA_PIEZAS + " ("
                + COLUMNA_PIEZA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMNA_PIEZA_NOMBRE + " TEXT NOT NULL, "
                + COLUMNA_PIEZA_CANTIDAD + " INTEGER NOT NULL, "
                + COLUMNA_PIEZA_PRECIO + " REAL NOT NULL)";
        db.execSQL(crearTablaPiezas);

        String crearTablaPedidos = "CREATE TABLE " + TABLA_PEDIDOS + " ("
                + COLUMNA_PEDIDO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMNA_PEDIDO_PIEZA_ID + " INTEGER NOT NULL, "
                + COLUMNA_PEDIDO_PROVEEDOR_ID + " INTEGER NOT NULL, "
                + COLUMNA_PEDIDO_CANTIDAD + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + COLUMNA_PEDIDO_PIEZA_ID + ") REFERENCES " + TABLA_PIEZAS + "(" + COLUMNA_PIEZA_ID + "), "
                + "FOREIGN KEY(" + COLUMNA_PEDIDO_PROVEEDOR_ID + ") REFERENCES " + TABLA_PROVEEDORES + "(" + COLUMNA_PROVEEDOR_ID + "))";
        db.execSQL(crearTablaPedidos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PROVEEDORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PIEZAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PEDIDOS);
        onCreate(db);
    }

    /**
     * Método para insertar un nuevo usuario en la base de datos.
     *
     * @param usuario El objeto User que representa al nuevo usuario.
     * @return true si la inserción fue exitosa, false si falló.
     */
    public boolean anadirUsuario(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMNA_USUARIO_NOMBRE, usuario.getNombre());
        values.put(COLUMNA_USUARIO_CORREO, usuario.getEmail());
        values.put(COLUMNA_USUARIO_CONTRASENA, usuario.getPassword());
        values.put(COLUMNA_USUARIO_TIPO, usuario.getTipo());

        // Insertar en la tabla de usuarios
        long resultado = db.insert(TABLA_USUARIOS, null, values);
        db.close();

        // Si el resultado es mayor que -1, la inserción fue exitosa
        return resultado != -1;
    }

    /**
     * Método para obtener un usuario por su correo electrónico.
     *
     * @param email Correo electrónico del usuario.
     * @return El objeto User si se encuentra, null si no se encuentra.
     */
    public Usuario getUsuarioConEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLA_USUARIOS, null, COLUMNA_USUARIO_CORREO + "=?", new String[]{email},
                null, null, null);

        // Verificar si el cursor tiene datos
        if (cursor != null && cursor.moveToFirst()) {
            // Comprobar si las columnas existen
            int nombreIndex = cursor.getColumnIndex(COLUMNA_USUARIO_NOMBRE);
            int emailIndex = cursor.getColumnIndex(COLUMNA_USUARIO_CORREO);
            int passwordIndex = cursor.getColumnIndex(COLUMNA_USUARIO_CONTRASENA);
            int tipoIndex = cursor.getColumnIndex(COLUMNA_USUARIO_TIPO);

            // Verificar si los índices son válidos
            if (nombreIndex >= 0 && emailIndex >= 0 && passwordIndex >= 0 && tipoIndex >= 0) {
                Usuario usuario = new Usuario(
                        cursor.getString(nombreIndex),
                        cursor.getString(emailIndex),
                        cursor.getString(passwordIndex),
                        cursor.getString(tipoIndex)
                );
                cursor.close();
                return usuario;
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
