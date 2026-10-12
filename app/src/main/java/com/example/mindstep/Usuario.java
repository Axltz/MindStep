package com.example.mindstep;

/**
 * Modelo de datos POJO para representar al Usuario
 * Según la arquitectura de la aplicación (Capa de Negocio)
 */
public class Usuario {
    private String nombre;
    private String idEstudiante;
    private int estadoEmocionalBase; // 1: Bueno, 2: Regular, 3: Vulnerable

    public Usuario(String nombre, String idEstudiante, int estadoEmocionalBase) {
        this.nombre = nombre;
        this.idEstudiante = idEstudiante;
        this.estadoEmocionalBase = estadoEmocionalBase;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(String idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getEstadoEmocionalBase() {
        return estadoEmocionalBase;
    }

    public void setEstadoEmocionalBase(int estadoEmocionalBase) {
        this.estadoEmocionalBase = estadoEmocionalBase;
    }
}
