package RepositorioBD;
import Entidades.Curso;
import Entidades.Materia;
import Entidades.Sala;
import Entidades.Profesor;
import Entidades.Estudiante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class CursoRepositorio{
    private Connection getConnection() throws SQLException {
        return ConexionBaseDeDatos.getConnection();
    }

    // Método para obtener un curso por su ID
    public Curso obtenerCursoPorId(String idCurso) throws SQLException {
        Curso curso = null;
        String consulta = "SELECT c.id,/* c.cupos,*/ c.capacidad, m.id AS materia_id, m.nombre AS materia_nombre, s.id AS sala_id, s.ubicacion " +
                "FROM Curso c " +
                "LEFT JOIN Materia m ON c.materia_id = m.id " +
                "LEFT JOIN Sala s ON c.sala_id = s.id " +
                "WHERE c.id = ?";

        try (Connection conexion = getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
            pstmt.setString(1, idCurso);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Construir la materia
                    Materia materia = new Materia();
                    materia.setiD(rs.getString("materia_id"));
                    materia.setNombre(rs.getString("materia_nombre"));

                    // Construir la sala
                    Sala sala = new Sala();
                    sala.setiD(rs.getString("sala_id"));
                    sala.setUbicacion(rs.getString("ubicacion"));

                    // Construir el curso
                    curso = new Curso();
                    curso.setiD(rs.getString("id"));
                    //curso.setCupos(rs.getInt("cupos"));
                    curso.setCapacidad(rs.getInt("capacidad"));
                    curso.setMateria(materia);
                    curso.setSalas(new ArrayList<>()); // La lista de salas podría llenarse posteriormente
                    curso.getSalas().add(sala);
                    curso.setHorarios(obtenerHorarios(idCurso));
                    curso.setProfesores(obtenerProfesores(idCurso));
                    curso.setEstudiantes(obtenerEstudiantes(idCurso));
                }
            }
        }
        return curso;
    }
    // Método para obtener los horarios de un curso
    public List<Date> obtenerHorarios(String idCurso) throws SQLException {
        List<Date> horarios = new ArrayList<>();
        String consulta = "SELECT hora_inicio, hora_fin FROM Horario WHERE curso_id = ?";

        try (Connection conexion = getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
            pstmt.setString(1, idCurso);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    java.sql.Timestamp hora_inicio = rs.getTimestamp("hora_inicio");
                    java.sql.Timestamp hora_fin = rs.getTimestamp("hora_fin");

                    horarios.add(hora_inicio);
                    horarios.add(hora_fin);
                }
            }
        }

        return horarios;
    }

    public List<Profesor> obtenerProfesores(String idCurso) throws SQLException {
        List<Profesor> profesores = new ArrayList<>();
        String consulta = "SELECT p.id, p.nombre, p.apellido FROM Profesor p " +
                "JOIN Asignacion a ON p.id = a.profesor_id " +
                "WHERE a.curso_id = ?";

        try (Connection conexion = getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
            pstmt.setString(1, idCurso);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Profesor profesor = new Profesor();
                    profesor.setId(rs.getInt("id"));
                    profesor.setNombre(rs.getString("nombre"));
                    profesor.setApellido(rs.getString("apellido"));
                    profesores.add(profesor);
                }
            }
        }
        return profesores;
    }

    // Método para obtener los estudiantes de un curso
    public List<Estudiante> obtenerEstudiantes(String idCurso) throws SQLException {
        List<Estudiante> estudiantes = new ArrayList<>();
        String consulta = "SELECT e.id, e.nombre FROM Estudiante e " +
                "JOIN Inscripcion i ON e.id = i.estudiante_id " +
                "WHERE i.curso_id = ?";

        try (Connection conexion = getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
            pstmt.setString(1, idCurso);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Estudiante estudiante = new Estudiante();
                    estudiante.setId(rs.getInt("id"));
                    estudiante.setNombre(rs.getString("nombre"));
                    estudiantes.add(estudiante);
                }
            }
        }
        return estudiantes;
    }
}

