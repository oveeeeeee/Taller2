package com.example.taller2.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una reparación de un coche en el sistema.
 * Contiene información acerca de la reparación, como la matrícula del coche, el mecánico asignado,
 * el estado de la reparación, y una lista de tareas asociadas.
 *
 * @author Laura Ovelleiro
 */
public class Reparacion {

    private String idReparacion;
    private String matriculaCoche;
    private String mecanicoAsignado;
    private String descripcionReparacion;
    private String estadoReparacion; // Puede ser "pendiente", "en curso", "finalizado"
    private String presupuestoAprobado;
    private List<Tarea> listaTareas; // Lista de tareas asociadas

    /**
     * Constructor vacío necesario para Firebase.
     * Inicializa la lista de tareas como una lista vacía.
     */
    public Reparacion() {
        this.listaTareas = new ArrayList<>();
    }

    /**
     * Constructor con todos los parámetros necesarios para crear una reparación.
     *
     * @param idReparacion El ID de la reparación.
     * @param matriculaCoche La matrícula del coche a reparar.
     * @param mecanicoAsignado El nombre del mecánico asignado a la reparación.
     * @param descripcionReparacion La descripción de la reparación.
     * @param estadoReparacion El estado de la reparación (pendiente, en curso, finalizado).
     * @param presupuestoAprobado El presupuesto aprobado para la reparación.
     */
    public Reparacion(String idReparacion, String matriculaCoche, String mecanicoAsignado, String descripcionReparacion,
                      String estadoReparacion, String presupuestoAprobado) {
        this.idReparacion = idReparacion;
        this.matriculaCoche = matriculaCoche;
        this.mecanicoAsignado = mecanicoAsignado;
        this.descripcionReparacion = descripcionReparacion;
        this.estadoReparacion = estadoReparacion;
        this.presupuestoAprobado = presupuestoAprobado;
        this.listaTareas = new ArrayList<>();
    }

    /**
     * Constructor simplificado con algunos parámetros.
     *
     * @param descripcionReparacion La descripción de la reparación.
     * @param matriculaCoche La matrícula del coche a reparar.
     * @param estadoReparacion El estado de la reparación.
     * @param mecanicoAsignado El mecánico asignado a la reparación.
     */
    public Reparacion(String descripcionReparacion, String matriculaCoche, String estadoReparacion, String mecanicoAsignado) {
        this.descripcionReparacion = descripcionReparacion;
        this.matriculaCoche = matriculaCoche;
        this.mecanicoAsignado = mecanicoAsignado;
        this.estadoReparacion = "pendiente"; // Estado por defecto
        this.presupuestoAprobado = ""; // Presupuesto vacío por defecto
        this.listaTareas = new ArrayList<>();
    }

    /**
     * Constructor alternativo con algunos parámetros.
     *
     * @param descripcion La descripción de la reparación.
     * @param cocheSeleccionado La matrícula del coche a reparar.
     * @param estadoReparacion El estado de la reparación.
     */
    public Reparacion(String descripcion, String cocheSeleccionado, String estadoReparacion) {
        this.descripcionReparacion = descripcion;
        this.matriculaCoche = cocheSeleccionado;
        this.estadoReparacion = estadoReparacion;
    }

    // Getters y setters para todos los campos, incluyendo tareas

    public String getIdReparacion() {
        return idReparacion;
    }

    public void setIdReparacion(String idReparacion) {
        this.idReparacion = idReparacion;
    }

    public String getMatriculaCoche() {
        return matriculaCoche;
    }

    public void setMatriculaCoche(String matriculaCoche) {
        this.matriculaCoche = matriculaCoche;
    }

    public String getMecanicoAsignado() {
        return mecanicoAsignado;
    }

    public void setMecanicoAsignado(String mecanicoAsignado) {
        this.mecanicoAsignado = mecanicoAsignado;
    }

    public String getDescripcionReparacion() {
        return descripcionReparacion;
    }

    public void setDescripcionReparacion(String descripcionReparacion) {
        this.descripcionReparacion = descripcionReparacion;
    }

    public String getEstadoReparacion() {
        return estadoReparacion;
    }

    public void setEstadoReparacion(String estadoReparacion) {
        this.estadoReparacion = estadoReparacion;
    }

    public String getPresupuestoAprobado() {
        return presupuestoAprobado;
    }

    public void setPresupuestoAprobado(String presupuestoAprobado) {
        this.presupuestoAprobado = presupuestoAprobado;
    }

    public List<Tarea> getListaTareas() {
        return listaTareas;
    }

    public void setListaTareas(List<Tarea> listaTareas) {
        this.listaTareas = listaTareas;
    }

    /**
     * Método para agregar una tarea a la lista de tareas de la reparación.
     *
     * @param tarea La tarea a agregar.
     */
    public void agregarTarea(Tarea tarea) {
        this.listaTareas.add(tarea);
    }
}
