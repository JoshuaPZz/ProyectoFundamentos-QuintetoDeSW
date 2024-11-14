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

    public Materia(){

    }
    public Materia(String nombre, String id, String descripcion, int creditos, List<Materia> preRequisitos, List<Materia> coRequisitos) {
        this.nombre = nombre;
        this.iD = id;
        this.descripcion = descripcion;
        this.creditos = creditos;
        this.preRequisitos = preRequisitos;
        this.coRequisitos = coRequisitos;

    }
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
        StringBuilder sb = new StringBuilder();
        sb.append("Materia: ").append(nombre).append('\'');
        sb.append(", creditos=").append(creditos);
        sb.append(", descripcion='").append(descripcion).append('\'');
        return sb.toString();
    }
}
