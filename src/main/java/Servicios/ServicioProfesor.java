package Servicios;
import Entidades.Curso;
import Entidades.Profesor;

import java.util.ArrayList;
import java.util.List;
public class ServicioProfesor {
    private List<Profesor> profesores;
    public ServicioProfesor() {
        this.profesores = new ArrayList<>();
    }

    public List<Curso> verHorario(Profesor profesor) {
        if (profesor != null && profesor.getCursos() != null) {
            System.out.println("Horario del Profesor " + profesor.getNombre() + ":");
            for (Curso curso : profesor.getCursos()) {
                System.out.println("Curso: " + curso.getiD() + " - Horarios: " + curso.getHorarios());
            }
            return profesor.getCursos(); // Retorna la lista de cursos
        } else {
            System.out.println("El profesor no tiene cursos asignados o es nulo.");
            return new ArrayList<>(); // Retorna una lista vacía en caso de error

        }
    }
}
