package com.example.taller2.modelo;

/**
 * Clase que representa un usuario en el sistema.
 * Un usuario tiene un nombre, correo electrónico, contraseña y un tipo de usuario.
 * El tipo de usuario puede ser "cliente" o "administrativo", por ejemplo.
 *
 * @author Laura Ovelleiro
 */
public class Usuario {

    private String nombre;
    private String email;
    private String password;
    private String tipo;

    /**
     * Constructor sin el parámetro userType, se asignará "cliente" por defecto.
     *
     * @param nombre El nombre del usuario.
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     */
    public Usuario(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.tipo = "cliente";  // Tipo de usuario por defecto
    }

    /**
     * Constructor vacío necesario para Firebase.
     */
    public Usuario() {
        // Constructor vacío
    }

    /**
     * Constructor con userType para cuando se quiera asignar un tipo de usuario específico.
     *
     * @param nombre El nombre del usuario.
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @param tipo El tipo de usuario (por ejemplo, "cliente" o "administrativo").
     */
    public Usuario(String nombre, String email, String password, String tipo) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.tipo = tipo;
    }

    // Getters y Setters

    /**
     * Obtiene el nombre del usuario.
     *
     * @return El nombre del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     *
     * @param nombre El nombre del usuario.
     */
    public void setName(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return El correo electrónico del usuario.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param email El correo electrónico del usuario.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return La contraseña del usuario.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param password La contraseña del usuario.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el tipo de usuario.
     *
     * @return El tipo de usuario.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de usuario.
     *
     * @param tipo El tipo de usuario (por ejemplo, "cliente" o "administrativo").
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}

