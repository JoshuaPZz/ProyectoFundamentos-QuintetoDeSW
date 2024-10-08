package RepositorioBD;


import Entidades.Curso;
import Entidades.Estudiante;

import java.sql.SQLException;
import java.util.List;


import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        CursoRepositorio cursoRepositorio = new CursoRepositorio();
        EstudianteRepositorio estudianteRepositorio = new EstudianteRepositorio();

        try {
            // Probar obtener curso por ID
            System.out.println("=== Obtener Curso por ID ===");
            String idCurso = "1"; // Reemplazar por un ID de curso válido en tu base de datos
            Curso curso = cursoRepositorio.obtenerCursoPorId(idCurso);
            if (curso != null) {
                System.out.println("Curso ID: " + curso.getiD());
                System.out.println("Materia: " + curso.getMateria().getNombre());
                System.out.println("Capacidad: " + curso.getCapacidad());
                System.out.println("Número de Salas: " + curso.getSalas().size());
                System.out.println("Número de Profesores: " + curso.getProfesores().size());
                System.out.println("Número de Estudiantes: " + curso.getEstudiantes().size());
            } else {
                System.out.println("No se encontró el curso con ID: " + idCurso);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

