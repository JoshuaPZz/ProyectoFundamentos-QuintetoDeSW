package Entidades;

import java.util.List;

public class Sala {
    private String iD;
    private String ubicacion;
    private int capacidad;
    private String tipo;
    private List<Curso> cursos;

    public String getiD() {
        return iD;
    }

    public void setiD(String ID) {
        this.iD = ID;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }
}
