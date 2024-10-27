package RepositorioBD;
import Entidades.*;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class CursoRepositorio{
    private MateriaRepositorio materiaRepositorio;

    public CursoRepositorio() {
        this.materiaRepositorio = new MateriaRepositorio(); // Inicializar el repositorio de materia
    }

    private Connection getConnection() throws SQLException {
        return ConexionBaseDeDatos.getConnection();
    }

    // Método para obtener un curso por su ID

    public Curso obtenerCursoPorId(String idCurso) throws SQLException {
        Curso curso = null;
        String consulta = "SELECT c.id,c.cupos,c.capacidad, m.id AS materia_id, m.nombre AS materia_nombre, " +
                "m.creditos, m.descripcion, s.id AS sala_id, s.ubicacion " +
                "FROM Curso c " +
                "LEFT JOIN Materia m ON c.materia_id = m.id " +
                "LEFT JOIN Sala s ON c.sala_id = s.id " +
                "WHERE c.id = ?";

        try (Connection conexion = getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
            pstmt.setString(1, idCurso);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Materia materia = new Materia();
                    materia.setiD(rs.getString("materia_id"));
                    materia.setNombre(rs.getString("materia_nombre"));
                    materia.setCreditos(rs.getInt("creditos"));
                    materia.setDescripcion(rs.getString("descripcion"));

                    Sala sala = new Sala();
                    sala.setiD(rs.getString("sala_id"));
                    sala.setUbicacion(rs.getString("ubicacion"));

                    curso = new Curso();
                    curso.setiD(rs.getString("id"));
                    curso.setCupos(rs.getInt("cupos"));
                    curso.setCapacidad(rs.getInt("capacidad"));
                    curso.setMateria(materia);
                    curso.setSalas(new ArrayList<>());
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
    public List<Horario> obtenerHorarios(String idCurso) throws SQLException {
        List<Horario> horarios = new ArrayList<>();
        String consulta = "SELECT DISTINCT h.hora_inicio, h.hora_fin, ds.nombre AS dia, s.ubicacion AS sala " +
                        "FROM Horario h JOIN DiasSemana ds ON h.dia_semana_id = ds.id " +
                        "JOIN Curso c ON h.materia_id = c.materia_id " +
                        "LEFT JOIN Sala s ON h.sala_id = s.id " +
                        "WHERE c.id = ?";

        try (Connection conexion = getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
            pstmt.setString(1, idCurso);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String dia = rs.getString("dia");
                    java.sql.Timestamp horaInicio = rs.getTimestamp("hora_inicio");
                    java.sql.Timestamp horaFin = rs.getTimestamp("hora_fin");
                    /*
                    java.sql.Timestamp hora_inicio = rs.getTimestamp("hora_inicio");
                    java.sql.Timestamp hora_fin = rs.getTimestamp("hora_fin");
                     */
                    Horario horario = new Horario(dia, horaInicio, horaFin);
                    horarios.add(horario);
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

    // Método para ver si hay cruses de horario
    public boolean hayCruceHorarios(String cursoId, int estudianteId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Horario h1 " +
                "JOIN Curso c1 ON h1.materia_id = c1.materia_id " +
                "JOIN Inscripcion i ON i.curso_id = c1.id " +
                "JOIN Horario h2 ON h1.dia_semana_id = h2.dia_semana_id " +
                "JOIN Curso c2 ON h2.materia_id = c2.materia_id " +
                "AND i.estudiante_id = ? AND c2.id = ? " +
                "AND (h1.hora_inicio < h2.hora_fin AND h1.hora_fin > h2.hora_inicio)";

        try (Connection conexion = getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(query)) {
            pstmt.setInt(1, estudianteId);
            pstmt.setString(2, cursoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Curso buscarPorId(String id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Curso curso = null;

        try {
            connection = ConexionBaseDeDatos.getConnection();
            String sql = "SELECT * FROM Curso WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                curso = new Curso();
                curso.setiD(resultSet.getString("id"));
                curso.setCapacidad(resultSet.getInt("capacidad"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (resultSet != null) resultSet.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return curso;
    }

    public List<String> obtenerCursosPorMateria(String materiaId) throws SQLException {
        List<String> cursos = new ArrayList<>();

        // Consulta SQL
        String query = "SELECT c.id AS curso_id, c.materia_id, m.nombre AS nombre_materia, STRING_AGG(CONCAT(d.nombre, ' ', CONVERT(varchar, h.hora_inicio, 108), '-', CONVERT(varchar, h.hora_fin, 108)), ', ') AS horarios " +
                "FROM Curso c " +
                "JOIN Materia m ON c.materia_id = m.id " +
                "JOIN Horario h ON c.materia_id = h.materia_id " +
                "JOIN DiasSemana d ON h.dia_semana_id = d.id " +
                "WHERE m.id = ?" +
                "GROUP BY c.id, c.materia_id, m.nombre";

        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(query)) {

            ps.setString(1, materiaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String cursoId = rs.getString("curso_id");
                    String materiaNombre = rs.getString("nombre_materia");
                    String horarios = rs.getString("horarios");
                    String infoCurso = "Materia: " + materiaNombre + ", Curso ID: " + cursoId + ", Horario: " + horarios;
                    cursos.add(infoCurso);
                }
            }
        }
        return cursos;
    }

    public Materia obtenerMateriaporCursoID(String cursoId) throws SQLException {
        Materia materia = null;
        String consulta = "SELECT materia_id FROM Curso WHERE id = ?";

        try (Connection conexion = getConnection();
        PreparedStatement ps = conexion.prepareStatement(consulta)) {
            ps.setString(1, cursoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int materiaId = rs.getInt("materia_id");
                    materia = materiaRepositorio.obtenerMateriaPorId(materiaId);
                }
            }
        }
        return materia;
    }
/*
    public Curso agregarCurso(Curso curso) throws SQLException {
        String query = "INSERT INTO Curso (cupos, capacidad, materia_id, sala_id, estado_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = getConnection();
        PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, curso.getCupos());
            ps.setInt(2, curso.getCapacidad());
            ps.setString(3, curso.getMateria().getiD());
            if (curso.getSalas() != null) {
                ps.setInt(4, );

            }
        }
    }

 */


}

