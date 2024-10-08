package RepositorioBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Entidades.Curso;
import Entidades.Estudiante;
import Entidades.Materia;
import Entidades.Sala;

public class EstudianteRepositorio {
    private Connection getConnection() throws SQLException {
        return ConexionBaseDeDatos.getConnection();
    }

    public List<Estudiante> listaEstudiante() throws SQLException {
        List<Estudiante> estudiantes = new ArrayList<>();
        String consulta = "SELECT * FROM Estudiante";
        try (Connection conexion = getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(consulta);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setId(rs.getInt("id"));
                estudiante.setNombre(rs.getString("nombre"));
                estudiante.setDocumento(rs.getString("documento"));
                estudiante.setCorreo(rs.getString("correo"));
                estudiante.setClave(rs.getString("clave"));
                estudiante.setCursos(new ArrayList<>());
                estudiante.setCursosVistos(new ArrayList<>());
                estudiante.setCarrito(new ArrayList<>());

                // Obtener cursos activos
                String queryCurso = "SELECT c.id,/* c.cupos,*/ c.capacidad, m.id AS materia_id, m.nombre AS materia_nombre " +
                        "FROM Curso c " +
                        "JOIN Inscripcion i ON c.id = i.curso_id " +
                        "JOIN Materia m ON c.materia_id = m.id " +
                        "WHERE i.estudiante_id = ? AND i.ha_aprobado = 0";
                try (PreparedStatement psCurso = conexion.prepareStatement(queryCurso)) {
                    psCurso.setInt(1, estudiante.getId());
                    try (ResultSet rsCurso = psCurso.executeQuery()) {
                        while (rsCurso.next()) {
                            estudiante.getCursos().add(obtenerCurso(rsCurso, conexion));
                        }
                    }
                }

                // Obtener cursos aprobados (vistos)
                // Obtener cursos aprobados (vistos)
                String queryCursosVistos = "SELECT c.id, c.capacidad, m.id AS materia_id, m.nombre AS materia_nombre " +
                        "FROM Curso c " +
                        "JOIN Inscripcion i ON c.id = i.curso_id " +
                        "JOIN Materia m ON c.materia_id = m.id " +
                        "WHERE i.estudiante_id = ? AND i.ha_aprobado = 1";

                try (PreparedStatement psCursosVisto = conexion.prepareStatement(queryCursosVistos)) {
                    psCursosVisto.setInt(1, estudiante.getId());
                    try (ResultSet rsCursosVisto = psCursosVisto.executeQuery()) {
                        List<Curso> cursosVistos = new ArrayList<>();
                        while (rsCursosVisto.next()) {
                            Curso cursoVisto = obtenerCurso(rsCursosVisto, conexion);
                            cursosVistos.add(cursoVisto);
                        }
                        estudiante.setCursosVistos(cursosVistos);

                        // Imprimir cuántos cursos vistos se encontraron
                        System.out.println("Estudiante ID: " + estudiante.getId() + " - Cursos Vistos: " + cursosVistos.size());
                    }
                }

                estudiantes.add(estudiante);
            }
        }
        return estudiantes;
    }

    // Método auxiliar para obtener cursos y evitar código duplicado
    private Curso obtenerCurso(ResultSet rsCurso, Connection conexion) throws SQLException {
        String cursoId = rsCurso.getString("id");
        int capacidad = rsCurso.getInt("capacidad");
        //int cupos = rsCurso.getInt("cupos");
        String materiaId = rsCurso.getString("materia_id");
        String materiaNombre = rsCurso.getString("materia_nombre");

        Materia materia = new Materia();
        materia.setiD(materiaId);
        materia.setNombre(materiaNombre);

        // Obtener horarios del curso
        List<java.util.Date> horarios = new ArrayList<>();
        String queryHorarios = "SELECT hora_inicio, hora_fin FROM Horario WHERE curso_id = ?";
        try (PreparedStatement psHorario = conexion.prepareStatement(queryHorarios)) {
            psHorario.setString(1, cursoId);
            try (ResultSet rsHorario = psHorario.executeQuery()) {
                while (rsHorario.next()) {
                    java.sql.Timestamp hora_inicio = rsHorario.getTimestamp("hora_inicio");
                    java.sql.Timestamp hora_fin = rsHorario.getTimestamp("hora_fin");

                    horarios.add(hora_inicio);
                    horarios.add(hora_fin);
                }
            }
        }

        // Obtener salas del curso
        List<Sala> salas = new ArrayList<>();
        String querySalas = "SELECT s.id, s.ubicacion, s.capacidad FROM Sala s " +
                "JOIN Curso c ON c.sala_id = s.id WHERE c.id = ?";
        try (PreparedStatement psSala = conexion.prepareStatement(querySalas)) {
            psSala.setString(1, cursoId);
            try (ResultSet rsSala = psSala.executeQuery()) {
                while (rsSala.next()) {
                    Sala sala = new Sala();
                    sala.setiD(rsSala.getString("id"));
                    sala.setUbicacion(rsSala.getString("ubicacion"));
                    sala.setCapacidad(rsSala.getInt("capacidad"));
                    salas.add(sala);
                }
            }
        }

        return new Curso(cursoId, materia, capacidad, horarios, salas, 0);
    }

    public List<Curso> obtenerHorarios(String idCurso) throws SQLException {
        List<Curso> horarios = new ArrayList<>();
        String consulta = "SELECT hora_inicio, hora_fin FROM Horario WHERE curso_id = ?";

        try (Connection conexion = getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
            pstmt.setString(1, idCurso);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    java.sql.Timestamp hora_inicio = rs.getTimestamp("hora_inicio");
                    java.sql.Timestamp hora_fin = rs.getTimestamp("hora_fin");

                  // samuel corrija
                }
            }
        }

        return horarios;
    }
}