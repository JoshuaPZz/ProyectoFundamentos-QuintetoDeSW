package RepositorioBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import Entidades.*;

public class ProfesorRepositorio {
    private Connection getConnection() throws SQLException {
        return ConexionBaseDeDatos.getConnection();
    }

    public Profesor buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Profesor WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Crear el profesor con datos básicos
                    Profesor profesor = new Profesor();
                    profesor.setId(resultSet.getInt("id"));
                    profesor.setNombre(resultSet.getString("nombre"));
                    profesor.setApellido(resultSet.getString("apellido"));
                    profesor.setDocumento(resultSet.getString("documento"));
                    profesor.setCorreo(resultSet.getString("correo"));
                    profesor.setClave(resultSet.getString("clave"));

                    // Cargar los cursos asignados al profesor
                    try (Connection conn2 = getConnection()) {
                        profesor.setCursos(obtenerCursosPorProfesorId(conn2, id));
                    }

                    return profesor;
                }
                return null;
            }
        }
    }

    // Método auxiliar para obtener los cursos del profesor
    private List<Curso> obtenerCursosPorProfesorId(Connection connection, int profesorId) throws SQLException {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT c.id AS curso_id, c.cupos, c.capacidad, m.id AS materia_id, m.nombre AS materia_nombre, m.descripcion AS materia_descripcion, m.creditos AS materia_creditos, s.id AS sala_id, s.ubicacion " +
                "FROM Curso c " +
                "JOIN Asignacion a ON c.id = a.curso_id " +
                "JOIN Materia m ON c.materia_id = m.id " +
                "LEFT JOIN Sala s ON c.sala_id = s.id " +
                "WHERE a.profesor_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, profesorId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Crear la materia
                    Materia materia = new Materia();
                    materia.setiD(String.valueOf(resultSet.getInt("materia_id")));
                    materia.setNombre(resultSet.getString("materia_nombre"));
                    materia.setDescripcion(resultSet.getString("materia_descripcion"));
                    materia.setCreditos(resultSet.getInt("materia_creditos"));

                    // Obtener horarios para este curso
                    List<Horario> horarios = obtenerHorariosPorMateriaId(connection, Integer.parseInt(materia.getiD()));

                    // Obtener salas para este curso (a través de los horarios)
                    List<Sala> salas = obtenerSalasPorMateriaId(connection, Integer.parseInt(materia.getiD()));

                    // Convertir el ID numérico a String para el curso
                    String cursoId = String.valueOf(resultSet.getInt("curso_id"));

                    // Crear el curso con todos sus componentes
                    Curso curso = new Curso(
                            cursoId,
                            materia,
                            resultSet.getInt("capacidad"),
                            horarios,
                            salas,
                            resultSet.getInt("cupos")
                    );

                    cursos.add(curso);
                }
            }
        }
        return cursos;
    }

    // Método auxiliar para obtener los horarios de una materia
    private List<Horario> obtenerHorariosPorMateriaId(Connection connection, int materiaId) throws SQLException {
        List<Horario> horarios = new ArrayList<>();
        String sql = "SELECT h.*, d.nombre as dia_nombre FROM Horario h " +
                "JOIN DiasSemana d ON h.dia_semana_id = d.id " +
                "WHERE h.materia_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, materiaId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    horarios.add(new Horario(
                            resultSet.getString("dia_semana_id"),
                            resultSet.getTimestamp("hora_inicio"),
                            resultSet.getTimestamp("hora_fin")
                    ));
                }
            }
        }
        return horarios;
    }

    // Método auxiliar para obtener las salas de una materia
    private List<Sala> obtenerSalasPorMateriaId(Connection connection, int materiaId) throws SQLException {
        List<Sala> salas = new ArrayList<>();
        String sql = "SELECT DISTINCT s.* FROM Sala s " +
                "JOIN Horario h ON s.id = h.sala_id " +
                "WHERE h.materia_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, materiaId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Sala sala = new Sala();
                    sala.setiD(String.valueOf(resultSet.getInt("id")));
                    sala.setUbicacion(resultSet.getString("ubicacion"));
                    sala.setCapacidad(resultSet.getInt("capacidad"));
                    sala.setTipo(resultSet.getString("tipo"));
                    salas.add(sala);
                }
            }
        }
        return salas;
    }

    // Método para obtener la lista de todos los profesores
    public List<Profesor> obtenerProfesores() throws SQLException {
        List<Profesor> profesores = new ArrayList<>();
        String consulta = "SELECT * FROM Profesor";

        try (Connection conexion = getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(consulta);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Profesor profesor = new Profesor();
                profesor.setId(rs.getInt("id"));
                profesor.setNombre(rs.getString("nombre"));
                profesor.setApellido(rs.getString("apellido"));
                profesor.setDocumento(rs.getString("documento"));
                profesor.setCorreo(rs.getString("correo"));
                profesor.setClave(rs.getString("clave"));
                profesor.setCursos(obtenerCursosPorProfesor(profesor.getId()));

                profesores.add(profesor);
            }
        }
        return profesores;
    }

    // Método para insertar un nuevo profesor
    public void insertarProfesor(Profesor profesor) throws SQLException {
        String consulta = "INSERT INTO Profesor (nombre, apellido, documento, correo, clave) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(consulta)) {

            pstmt.setString(1, profesor.getNombre());
            pstmt.setString(2, profesor.getApellido());
            pstmt.setString(3, profesor.getDocumento());
            pstmt.setString(4, profesor.getCorreo());
            pstmt.setString(5, profesor.getClave());

            pstmt.executeUpdate();
        }
    }

    // Método para obtener los cursos asignados a un profesor
    public List<Curso> obtenerCursosPorProfesor(int profesorId) throws SQLException {
        List<Curso> cursos = new ArrayList<>();
        String consulta = "SELECT c.id,/* c.cupos,*/ c.capacidad FROM Curso c " +
                "JOIN Asignacion a ON c.id = a.curso_id " +
                "WHERE a.profesor_id = ?";

        try (Connection conexion = getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
            pstmt.setInt(1, profesorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Curso curso = new Curso();
                    curso.setiD(rs.getString("id"));
                    //curso.setCupos(rs.getInt("cupos"));
                    curso.setCapacidad(rs.getInt("capacidad"));
                    cursos.add(curso);
                }
            }
        }
        return cursos;
    }

    public void asignarCursoAProfesor(Profesor profesor, Curso nuevoCurso) {
        String consulta = "INSERT INTO Asignacion (profesor_id, curso_id) VALUES (?, ?)";
        try (Connection conexion = ConexionBaseDeDatos.getConnection();
             PreparedStatement ps = conexion.prepareStatement(consulta)) {

            // Configuración de los parámetros de la consulta
            ps.setInt(1, profesor.getId());
            ps.setInt(2, Integer.parseInt(nuevoCurso.getiD()));

            // Ejecuta la inserción
            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas > 0) {
                System.out.println("Curso asignado exitosamente al profesor.");
            } else {
                System.out.println("No se pudo asignar el curso al profesor.");
            }

        } catch (SQLException e) {
            System.out.println("Error al asignar el curso al profesor: " + e.getMessage());
        }
    }
}
