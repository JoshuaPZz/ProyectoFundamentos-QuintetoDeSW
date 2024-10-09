package Servicios;

import Entidades.Curso;
import Entidades.Profesor;
import RepositorioBD.ProfesorRepositorio;

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

}
