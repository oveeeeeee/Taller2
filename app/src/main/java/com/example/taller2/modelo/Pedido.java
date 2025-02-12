package com.example.taller2.modelo;


/**
 * Clase que representa un pedido de piezas realizado a un proveedor.
 * Contiene información sobre la pieza, el proveedor y la cantidad solicitada.
 *
 * @author Laura Ovelleiro
 */
public class Pedido {

    private int id;
    private Pieza pieza;
    private Proveedor proveedor;
    private int cantidad;

    /**
     * Constructor de la clase Pedido sin ID (para crear un nuevo pedido).
     *
     * @param pieza La pieza solicitada en el pedido.
     * @param proveedor El proveedor que suministrará la pieza.
     * @param cantidad La cantidad de la pieza solicitada.
     */
    public Pedido(Pieza pieza, Proveedor proveedor, int cantidad) {
        this.pieza = pieza;
        this.proveedor = proveedor;
        this.cantidad = cantidad;
    }

    /**
     * Constructor de la clase Pedido con ID (para recuperar un pedido desde la base de datos).
     *
     * @param id El identificador del pedido.
     * @param pieza La pieza solicitada en el pedido.
     * @param proveedor El proveedor que suministrará la pieza.
     * @param cantidad La cantidad de la pieza solicitada.
     */
    public Pedido(int id, Pieza pieza, Proveedor proveedor, int cantidad) {
        this.id = id;
        this.pieza = pieza;
        this.proveedor = proveedor;
        this.cantidad = cantidad;
    }

    // Getters y Setters

    /**
     * Obtiene el ID del pedido.
     *
     * @return El identificador del pedido.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del pedido.
     *
     * @param id El identificador del pedido.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene la pieza solicitada en el pedido.
     *
     * @return La pieza solicitada.
     */
    public Pieza getPieza() {
        return pieza;
    }

    /**
     * Establece la pieza solicitada en el pedido.
     *
     * @param pieza La pieza solicitada.
     */
    public void setPieza(Pieza pieza) {
        this.pieza = pieza;
    }

    /**
     * Obtiene el proveedor que suministra la pieza.
     *
     * @return El proveedor que suministrará la pieza.
     */
    public Proveedor getProveedor() {
        return proveedor;
    }

    /**
     * Establece el proveedor que suministrará la pieza.
     *
     * @param proveedor El proveedor que suministrará la pieza.
     */
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    /**
     * Obtiene la cantidad de la pieza solicitada.
     *
     * @return La cantidad de la pieza solicitada.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad de la pieza solicitada.
     *
     * @param cantidad La cantidad de la pieza solicitada.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
