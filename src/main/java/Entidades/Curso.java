package Entidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Curso {

    private String iD;
    private int capacidad;
    private List<Date> horarios;
    private Materia materia;
    private List<Sala> salas;

    private List<Profesor> profesores;
    private List<Estudiante> estudiantes;

    public String getiD() { return iD;
    }

    public void setiD(String ID) {
        this.iD = ID;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public List<Date> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Date> horarios) {
        this.horarios = horarios;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public List<Sala> getSalas() {
        return salas;
    }

    public void setSalas(List<Sala> salas) {
        this.salas = salas;
    }

    public List<Profesor> getProfesores() {
        return profesores;
    }

    public void setProfesores(List<Profesor> profesores) {
        this.profesores = profesores;
    }

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public Curso(String ID, Materia materia, int capacidad, List<Date> horarios, List<Sala> salas) {
        this.iD = ID;
        this.capacidad = capacidad;
        this.horarios = horarios;
        this.materia = materia;
        this.salas = salas;
        this.profesores = new ArrayList<>();
        this.estudiantes = new ArrayList<>();
    }

}
