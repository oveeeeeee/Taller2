package com.example.taller2.modelo;

/**
 * Clase que representa un coche en el sistema.
 * Contiene la información del coche, como matrícula, marca, modelo, estado, cliente y mecánico asignado.
 *
 * @author Laura Ovelleiro
 */
public class Coche {
    private String matricula;
    private String marca;
    private String modelo;
    private String estado;
    private String clienteEmail;
    private String mecanicoJefeAsignado;

    /**
     * Constructor vacío para Firebase.
     * Necesario para que Firebase pueda crear instancias de esta clase.
     */
    public Coche() {}

    /**
     * Constructor con todos los parámetros para crear un coche con la información completa.
     *
     * @param matricula Matrícula del coche.
     * @param marca Marca del coche.
     * @param modelo Modelo del coche.
     * @param estado Estado del coche (por ejemplo, "En reparación", "Terminado").
     * @param clienteEmail Correo electrónico del cliente propietario del coche.
     * @param mecanicoAsignado Nombre del mecánico jefe asignado al coche.
     */
    public Coche(String matricula, String marca, String modelo, String estado, String clienteEmail, String mecanicoAsignado) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.estado = estado;
        this.clienteEmail = clienteEmail;
        this.mecanicoJefeAsignado = mecanicoAsignado;
    }

    // Métodos getter y setter

    /**
     * Obtiene la matrícula del coche.
     *
     * @return La matrícula del coche.
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * Establece la matrícula del coche.
     *
     * @param matricula La matrícula del coche.
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * Obtiene la marca del coche.
     *
     * @return La marca del coche.
     */
    public String getMarca() {
        return marca;
    }

    /**
     * Establece la marca del coche.
     *
     * @param marca La marca del coche.
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * Obtiene el modelo del coche.
     *
     * @return El modelo del coche.
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Establece el modelo del coche.
     *
     * @param modelo El modelo del coche.
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * Obtiene el estado del coche.
     *
     * @return El estado del coche.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado del coche.
     *
     * @param estado El estado del coche (por ejemplo, "En reparación", "Terminado").
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el correo electrónico del cliente propietario del coche.
     *
     * @return El correo electrónico del cliente.
     */
    public String getClienteEmail() {
        return clienteEmail;
    }

    /**
     * Establece el correo electrónico del cliente propietario del coche.
     *
     * @param clienteEmail El correo electrónico del cliente.
     */
    public void setClienteEmail(String clienteEmail) {
        this.clienteEmail = clienteEmail;
    }

    /**
     * Obtiene el nombre del mecánico jefe asignado al coche.
     *
     * @return El nombre del mecánico jefe asignado.
     */
    public String getMecanicoAsignado() {
        return mecanicoJefeAsignado;
    }

    /**
     * Establece el nombre del mecánico jefe asignado al coche.
     *
     * @param mecanicoAsignado El nombre del mecánico jefe asignado.
     */
    public void setMecanicoAsignado(String mecanicoAsignado) {
        this.mecanicoJefeAsignado = mecanicoAsignado;
    }
}
