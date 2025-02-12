package com.example.taller2.modelo;


/**
 * Clase que representa una pieza disponible en el taller.
 * Contiene información sobre la pieza, como su nombre, cantidad en stock y precio.
 *
 * @author Laura Ovelleiro
 */
public class Pieza {

    private int id;
    private String nombre;
    private int cantidadStock;
    private double precio;

    /**
     * Constructor de la clase Pieza.
     *
     * @param id El identificador de la pieza.
     * @param nombre El nombre de la pieza.
     * @param cantidadStock La cantidad de la pieza disponible en stock.
     * @param precio El precio de la pieza.
     */
    public Pieza(int id, String nombre, int cantidadStock, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.cantidadStock = cantidadStock;
        this.precio = precio;
    }

    // Getters y Setters

    /**
     * Obtiene el ID de la pieza.
     *
     * @return El identificador de la pieza.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID de la pieza.
     *
     * @param id El identificador de la pieza.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la pieza.
     *
     * @return El nombre de la pieza.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la pieza.
     *
     * @param nombre El nombre de la pieza.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la cantidad de la pieza disponible en stock.
     *
     * @return La cantidad de la pieza en stock.
     */
    public int getCantidadStock() {
        return cantidadStock;
    }

    /**
     * Establece la cantidad de la pieza disponible en stock.
     *
     * @param cantidadStock La cantidad de la pieza en stock.
     */
    public void setCantidadStock(int cantidadStock) {
        this.cantidadStock = cantidadStock;
    }

    /**
     * Obtiene el precio de la pieza.
     *
     * @return El precio de la pieza.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio de la pieza.
     *
     * @param precio El precio de la pieza.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
