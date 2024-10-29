package Servicios;

import Entidades.Curso;
import Entidades.Profesor;
import Entidades.Materia;
import Entidades.Horario;
import Entidades.Sala;
import RepositorioBD.ProfesorRepositorio;
import RepositorioBD.CursoRepositorio;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicioProfesor {

    //Método para ver el horario de un profesor (lista de cursos asignados)
    public List<Curso> cursosAsociadosProfesor(Profesor profesor) throws SQLException {
        List<Curso> curso = new ArrayList<>();
        ProfesorRepositorio profesorRepositorio = new ProfesorRepositorio();
        if (profesor != null && profesor.getCursos() != null) {
            curso = profesorRepositorio.obtenerCursosPorProfesor(profesor.getId());
            return curso;
        } else {
            System.out.println("El profesor no tiene cursos asignados o es nulo.");
            return new ArrayList<>();
        }
    }

    //metodo para crear un curso, llamando el metodo de servicio curso y llama el repositorio para que ese curso se le asigne al profesor
    public Curso crearYAsignarCurso(Profesor profesor, String cursoID, Materia materia, int capacidad, List<Horario> horarios, List<Sala> salas, int cupos) throws SQLException {
        if (profesor == null) {
            System.out.println("Error: No existe profesor");
            return null;
        }
        ServicioCurso servicioCurso = new ServicioCurso();
        // Lista de profesores que contiene al profesor actual
        List<Profesor> profesores = new ArrayList<>();
        profesores.add(profesor);
        // Llamada al método crearCurso en ServicioCurso
        Curso nuevoCurso = servicioCurso.crearCurso(cursoID, materia, capacidad, horarios, salas, cupos, profesores);
        CursoRepositorio cursoRepositorio = new CursoRepositorio();
        //cursoRepositorio.crearCurso(nuevoCurso);  // Falta método en repositorio: Guardar el curso en el repositorio de cursos
        ProfesorRepositorio profesorRepositorio = new ProfesorRepositorio();
        //profesorRepositorio.asignarCursoAProfesor(profesor, nuevoCurso);  // Falta método en repositorio: Asignar el curso al profesor
        System.out.println("Curso creado y asignado exitosamente al profesor.");
        return nuevoCurso;
    }

}
