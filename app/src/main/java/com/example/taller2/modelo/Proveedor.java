package com.example.taller2.modelo;


/**
 * Clase que representa un proveedor en el sistema.
 * Contiene información acerca del proveedor, como su nombre y datos de contacto.
 *
 * @author Laura Ovelleiro
 */
public class Proveedor {

    private int id;
    private String nombre;
    private String contacto;

    /**
     * Constructor de la clase Proveedor.
     *
     * @param id El identificador único del proveedor.
     * @param nombre El nombre del proveedor.
     * @param contacto Los datos de contacto del proveedor (por ejemplo, teléfono, email).
     */
    public Proveedor(int id, String nombre, String contacto) {
        this.id = id;
        this.nombre = nombre;
        this.contacto = contacto;
    }

    // Getters y Setters

    /**
     * Obtiene el ID del proveedor.
     *
     * @return El identificador del proveedor.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del proveedor.
     *
     * @param id El identificador del proveedor.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del proveedor.
     *
     * @return El nombre del proveedor.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del proveedor.
     *
     * @param nombre El nombre del proveedor.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene los datos de contacto del proveedor.
     *
     * @return Los datos de contacto del proveedor.
     */
    public String getContacto() {
        return contacto;
    }

    /**
     * Establece los datos de contacto del proveedor.
     *
     * @param contacto Los datos de contacto del proveedor.
     */
    public void setContacto(String contacto) {
        this.contacto = contacto;
    }
}
