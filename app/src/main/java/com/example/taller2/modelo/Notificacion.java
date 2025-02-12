package com.example.taller2.modelo;


/**
 * Clase que representa una notificación relacionada con una reparación.
 * Contiene información sobre la matrícula del coche, tipo de notificación,
 * descripción del presupuesto y el importe del presupuesto.
 *
 * @author Laura Ovelleiro
 */
public class Notificacion {
    private String matricula;
    private String tipoNotificacion;
    private String descripcionPresupuesto;
    private String importePresupuesto;

    /**
     * Constructor de la notificación con todos los parámetros necesarios.
     *
     * @param matricula Matrícula del coche relacionado con la notificación.
     * @param tipoNotificacion Tipo de la notificación (por ejemplo, "Presupuesto aprobado").
     * @param descripcionPresupuesto Descripción del presupuesto asociado a la reparación.
     * @param importePresupuesto Importe del presupuesto.
     */
    public Notificacion(String matricula, String tipoNotificacion, String descripcionPresupuesto, String importePresupuesto) {
        this.matricula = matricula;
        this.tipoNotificacion = tipoNotificacion;
        this.descripcionPresupuesto = descripcionPresupuesto;
        this.importePresupuesto = importePresupuesto;
    }

    /**
     * Constructor vacío para Firebase.
     */
    public Notificacion() {
    }

    // Getters y setters

    /**
     * Obtiene la matrícula del coche asociado a la notificación.
     *
     * @return La matrícula del coche.
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * Obtiene el tipo de notificación.
     *
     * @return El tipo de notificación (por ejemplo, "Presupuesto aprobado").
     */
    public String getTipoNotificacion() {
        return tipoNotificacion;
    }

    /**
     * Obtiene la descripción del presupuesto asociado a la notificación.
     *
     * @return La descripción del presupuesto.
     */
    public String getDescripcionPresupuesto() {
        return descripcionPresupuesto;
    }

    /**
     * Obtiene el importe del presupuesto asociado a la notificación.
     *
     * @return El importe del presupuesto.
     */
    public String getImportePresupuesto() {
        return importePresupuesto;
    }
}
