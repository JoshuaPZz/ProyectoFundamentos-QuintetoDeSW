package Entidades;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Curso {

    private String iD;
    private int cupos; //Hace referencia a los cupos maximos

    private int capacidad; //la capacidad ocupada actualmente
    private List<Horario> horarios;
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

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
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

    public int getCupos() {return cupos;}

    public void setCupos(int cupos) {this.cupos = cupos;}

    @Override
    public String toString() {

        StringBuilder resumen = new StringBuilder();
        SimpleDateFormat diaFormat = new SimpleDateFormat("EEEE"); // Formato solo del día
        SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm"); // Formato solo de hora

        // Detalles de la materia
        resumen.append("Materia: ").append(materia.getNombre()).append("\n")
                .append("Créditos: ").append(materia.getCreditos()).append("\n")
                .append("Descripción: ").append(materia.getDescripcion()).append("\n")
                .append("Horario: ");

        // Construimos el resumen para cada horario sin agrupar por día
        for (int i = 0; i < horarios.size(); i++) {
            Horario horario = horarios.get(i);
            String dia = horario.getDia();
            String horaInicio = horaFormat.format(horario.getHoraInicio());
            String horaFin = horaFormat.format(horario.getHoraFin());

            resumen.append(dia).append(" ").append(horaInicio).append("-").append(horaFin);

            if (i < horarios.size() - 1) {
                resumen.append(", ");
            }
        }
        resumen.append("\n" + "Salas: ");
        for (Sala sala : salas) {
            resumen.append(sala.getUbicacion()).append(" ");
        }

        return resumen.toString().trim();

    }

    public Curso() {
        this.iD = "";
        this.cupos = 0;
        this.capacidad = 0;
        this.horarios = new ArrayList<>();
        this.materia = new Materia();
        this.salas = new ArrayList<>();
        this.profesores = new ArrayList<>();
        this.estudiantes = new ArrayList<>();
    }

    public Curso(String ID, Materia materia, int capacidad, List<Horario> horarios, List<Sala> salas, int cupos) {
        this.iD = ID;
        this.capacidad = capacidad;
        this.horarios = horarios;
        this.materia = materia;
        this.salas = salas;
        this.profesores = new ArrayList<>();
        this.estudiantes = new ArrayList<>();
        this.cupos = cupos;
    }

    public Curso(Materia materia, int capacidad, List<Horario> horarios, List<Sala> salas, int cupos) {
        this.capacidad = capacidad;
        this.horarios = horarios;
        this.materia = materia;
        this.salas = salas;
        this.profesores = new ArrayList<>();
        this.estudiantes = new ArrayList<>();
        this.cupos = cupos;
    }

}

