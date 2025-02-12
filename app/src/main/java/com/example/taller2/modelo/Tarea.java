package com.example.taller2.modelo;


/**
 * Clase que representa una tarea asociada a una reparación de coche.
 * Contiene información sobre la tarea, como el nombre, el mecánico asignado,
 * el estado de la tarea, el comentario relacionado y la matrícula del coche.
 *
 * @author Laura Ovelleiro
 */
public class Tarea {
    private String id;  // Nuevo campo para identificar la tarea
    private String nombreTarea;
    private String mecanicoAsignado;
    private String matriculaCoche;
    private String estadoTarea;
    private String comentario;  // Solo un comentario

    /**
     * Constructor vacío necesario para Firebase.
     */
    public Tarea() {}

    /**
     * Constructor con los parámetros necesarios para crear una tarea.
     *
     * @param nombreTarea El nombre de la tarea.
     * @param mecanicoAsignado El mecánico asignado a la tarea.
     * @param estadoTarea El estado de la tarea (pendiente, en curso, finalizado).
     * @param comentario Un comentario asociado a la tarea.
     * @param matriculaCoche La matrícula del coche asociado a la tarea.
     */
    public Tarea(String nombreTarea, String mecanicoAsignado, String estadoTarea, String comentario, String matriculaCoche) {
        this.nombreTarea = nombreTarea;
        this.mecanicoAsignado = mecanicoAsignado;
        this.estadoTarea = estadoTarea;
        this.comentario = comentario;
        this.matriculaCoche = matriculaCoche;
    }

    /**
     * Constructor con todos los parámetros incluyendo el ID de la tarea.
     *
     * @param id El ID de la tarea.
     * @param nombreTarea El nombre de la tarea.
     * @param matriculaCoche La matrícula del coche asociado a la tarea.
     * @param mecanicoAsignado El mecánico asignado a la tarea.
     * @param estadoTarea El estado de la tarea.
     * @param comentario El comentario asociado a la tarea.
     */
    public Tarea(String id, String nombreTarea, String matriculaCoche, String mecanicoAsignado, String estadoTarea, String comentario) {
        this.id = id;
        this.nombreTarea = nombreTarea;
        this.mecanicoAsignado = mecanicoAsignado;
        this.matriculaCoche = matriculaCoche;
        this.estadoTarea = estadoTarea;
        this.comentario = comentario;
    }

    // Getters y Setters

    /**
     * Obtiene el ID de la tarea.
     *
     * @return El ID de la tarea.
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el ID de la tarea.
     *
     * @param id El ID de la tarea.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la tarea.
     *
     * @return El nombre de la tarea.
     */
    public String getNombreTarea() {
        return nombreTarea;
    }

    /**
     * Establece el nombre de la tarea.
     *
     * @param nombreTarea El nombre de la tarea.
     */
    public void setNombreTarea(String nombreTarea) {
        this.nombreTarea = nombreTarea;
    }

    /**
     * Obtiene el mecánico asignado a la tarea.
     *
     * @return El mecánico asignado.
     */
    public String getMecanicoAsignado() {
        return mecanicoAsignado;
    }

    /**
     * Establece el mecánico asignado a la tarea.
     *
     * @param mecanicoAsignado El nombre del mecánico.
     */
    public void setMecanicoAsignado(String mecanicoAsignado) {
        this.mecanicoAsignado = mecanicoAsignado;
    }

    /**
     * Obtiene el estado de la tarea.
     *
     * @return El estado de la tarea.
     */
    public String getEstadoTarea() {
        return estadoTarea;
    }

    /**
     * Establece el estado de la tarea.
     *
     * @param estadoTarea El estado de la tarea.
     */
    public void setEstadoTarea(String estadoTarea) {
        this.estadoTarea = estadoTarea;
    }

    /**
     * Obtiene la matrícula del coche asociado a la tarea.
     *
     * @return La matrícula del coche.
     */
    public String getMatriculaCoche() {
        return matriculaCoche;
    }

    /**
     * Establece la matrícula del coche asociado a la tarea.
     *
     * @param matriculaCoche La matrícula del coche.
     */
    public void setMatriculaCoche(String matriculaCoche) {
        this.matriculaCoche = matriculaCoche;
    }

    /**
     * Obtiene el comentario asociado a la tarea.
     *
     * @return El comentario de la tarea.
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * Establece el comentario asociado a la tarea.
     *
     * @param comentario El comentario de la tarea.
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
