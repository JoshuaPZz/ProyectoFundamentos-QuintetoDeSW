package Entidades;

import java.util.ArrayList;
import java.util.List;
public class Materia {
    private String nombre;
    private String iD;
    private String descripcion;
    private int creditos;
    private List<Materia> preRequisitos = new ArrayList<>();
    private List<Materia> coRequisitos = new ArrayList<>();

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String ID) {
        this.iD = ID;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public List<Materia> getPrerequisitos() {
        return preRequisitos;
    }

    public void setPrerequisitos(List<Materia> prerequisitos) {
        this.preRequisitos = prerequisitos;
    }

    public List<Materia> getCorequisitos() {
        return coRequisitos;
    }

    public void setCorequisitos(List<Materia> corequisitos) {
        this.coRequisitos = corequisitos;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
