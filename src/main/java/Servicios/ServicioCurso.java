package Servicios;
import Entidades.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
public class ServicioCurso {
    private List<Curso> cursos;

    // Constructor
    public ServicioCurso() {
        this.cursos = new ArrayList<>();
    }
    public Curso crearCurso(String ID, Materia materia, int capacidad, List<Date> horarios, List<Sala> salas, int cupos, List<Profesor> profesores) {
        Curso nuevoCurso = new Curso(ID, materia, capacidad, horarios, salas, cupos);
        nuevoCurso.setProfesores(profesores);
        cursos.add(nuevoCurso);
        return nuevoCurso;
    }


    // Método para consultar un curso
    public String consultarCurso(Curso curso) {
        if (curso != null) {
            return "Curso ID: " + curso.getiD() + ", Materia: " + curso.getMateria().getNombre() +
                    ", Capacidad: " + curso.getCapacidad() + ", Estudiantes: " + curso.getEstudiantes().size();
        }
        return "Curso no encontrado.";
    }

    // Método para ver los estudiantes de un curso
    public List<Estudiante> verEstudiantes(Curso curso) {
        if (curso != null) {
            return curso.getEstudiantes();
        }
        return new ArrayList<>();
    }

    // Método para ver el profesor principal de un curso
    public Profesor verProfesor(Curso curso) {
        if (curso != null && !curso.getProfesores().isEmpty()) {
            return curso.getProfesores().get(0);  // Suponemos que el primer profesor es el principal
        }
        return null;
    }


}
