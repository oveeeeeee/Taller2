package com.example.taller2.baseLocal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona la base de datos SQLite para la aplicación.
 * Permite almacenar información sobre proveedores, piezas y pedidos.
 *
 * @author Laura Ovelleiro
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "stock_piezas.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PROVEEDORES = "proveedores";
    public static final String TABLE_PIEZAS = "piezas";
    public static final String TABLE_PEDIDOS = "pedidos";

    // Columnas de la tabla proveedores
    public static final String COLUMN_PROVEEDOR_ID = "id";
    public static final String COLUMN_PROVEEDOR_NOMBRE = "nombre";
    public static final String COLUMN_PROVEEDOR_CONTACTO = "contacto";

    // Columnas de la tabla piezas
    public static final String COLUMN_PIEZA_ID = "id";
    public static final String COLUMN_PIEZA_NOMBRE = "nombre";
    public static final String COLUMN_PIEZA_CANTIDAD = "cantidad_stock";
    public static final String COLUMN_PIEZA_PRECIO = "precio";

    // Columnas de la tabla pedidos
    public static final String COLUMN_PEDIDO_ID = "id";
    public static final String COLUMN_PEDIDO_PIEZA_ID = "pieza_id";
    public static final String COLUMN_PEDIDO_PROVEEDOR_ID = "proveedor_id";
    public static final String COLUMN_PEDIDO_CANTIDAD = "cantidad";

    // Creación de tablas
    private static final String CREATE_TABLE_PROVEEDORES =
            "CREATE TABLE " + TABLE_PROVEEDORES + " (" +
                    COLUMN_PROVEEDOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PROVEEDOR_NOMBRE + " TEXT NOT NULL, " +
                    COLUMN_PROVEEDOR_CONTACTO + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_PIEZAS =
            "CREATE TABLE " + TABLE_PIEZAS + " (" +
                    COLUMN_PIEZA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PIEZA_NOMBRE + " TEXT NOT NULL, " +
                    COLUMN_PIEZA_CANTIDAD + " INTEGER NOT NULL, " +
                    COLUMN_PIEZA_PRECIO + " REAL NOT NULL);";

    private static final String CREATE_TABLE_PEDIDOS =
            "CREATE TABLE " + TABLE_PEDIDOS + " (" +
                    COLUMN_PEDIDO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PEDIDO_PIEZA_ID + " INTEGER NOT NULL, " +
                    COLUMN_PEDIDO_PROVEEDOR_ID + " INTEGER NOT NULL, " +
                    COLUMN_PEDIDO_CANTIDAD + " INTEGER NOT NULL, " +
                    "FOREIGN KEY(" + COLUMN_PEDIDO_PIEZA_ID + ") REFERENCES " + TABLE_PIEZAS + "(" + COLUMN_PIEZA_ID + "), " +
                    "FOREIGN KEY(" + COLUMN_PEDIDO_PROVEEDOR_ID + ") REFERENCES " + TABLE_PROVEEDORES + "(" + COLUMN_PROVEEDOR_ID + "));";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PROVEEDORES);
        db.execSQL(CREATE_TABLE_PIEZAS);
        db.execSQL(CREATE_TABLE_PEDIDOS);

        insertarProveedoresEjemplo(db);
        insertarPiezasEjemplo(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROVEEDORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PIEZAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEDIDOS);
        onCreate(db);
    }

    /**
     * Inserta proveedores de ejemplo si no hay registros.
     */
    private void insertarProveedoresEjemplo(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PROVEEDORES, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count == 0) { // Solo inserta si la tabla está vacía
            String[][] proveedores = {
                    {"Proveedor A", "proveedorA@email.com"},
                    {"Proveedor B", "proveedorB@email.com"},
                    {"Proveedor C", "proveedorC@email.com"}
            };

            ContentValues valores = new ContentValues();
            for (String[] proveedor : proveedores) {
                valores.put(COLUMN_PROVEEDOR_NOMBRE, proveedor[0]);
                valores.put(COLUMN_PROVEEDOR_CONTACTO, proveedor[1]);
                db.insert(TABLE_PROVEEDORES, null, valores);
                valores.clear();
            }
        }
    }

    /**
     * Inserta piezas de ejemplo si no hay registros.
     */
    private void insertarPiezasEjemplo(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PIEZAS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count == 0) { // Solo inserta si la tabla está vacía
            String[] nombres = {"Filtro de aceite", "Pastillas de freno", "Batería", "Correa de distribución"};
            int[] cantidades = {10, 20, 5, 7};
            double[] precios = {15.99, 40.50, 89.99, 120.75};

            ContentValues valores = new ContentValues();
            for (int i = 0; i < nombres.length; i++) {
                valores.put(COLUMN_PIEZA_NOMBRE, nombres[i]);
                valores.put(COLUMN_PIEZA_CANTIDAD, cantidades[i]);
                valores.put(COLUMN_PIEZA_PRECIO, precios[i]);
                db.insert(TABLE_PIEZAS, null, valores);
                valores.clear();
            }
        }
    }

    /**
     * Obtiene la lista de nombres de piezas disponibles.
     */
    public List<String> obtenerPiezas() {
        List<String> listaPiezas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PIEZA_NOMBRE + " FROM " + TABLE_PIEZAS, null);

        if (cursor.moveToFirst()) {
            do {
                listaPiezas.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaPiezas;
    }

    /**
     * Registra un pedido en la base de datos.
     */
    public void insertarPedido(String piezaNombre, int cantidad) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PIEZA_ID + " FROM " + TABLE_PIEZAS +
                " WHERE " + COLUMN_PIEZA_NOMBRE + " = ?", new String[]{piezaNombre});
        if (cursor.moveToFirst()) {
            int piezaId = cursor.getInt(0);
            ContentValues valores = new ContentValues();
            valores.put(COLUMN_PEDIDO_PIEZA_ID, piezaId);
            valores.put(COLUMN_PEDIDO_PROVEEDOR_ID, 1); // Proveedor predeterminado
            valores.put(COLUMN_PEDIDO_CANTIDAD, cantidad);

            db.insert(TABLE_PEDIDOS, null, valores);
        }
        cursor.close();
        db.close();
    }
}
