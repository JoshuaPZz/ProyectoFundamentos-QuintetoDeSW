package RepositorioBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Entidades.*;

public class EstudianteRepositorio {
    private Connection getConnection() throws SQLException {
        return ConexionBaseDeDatos.getConnection();
    }
    public Estudiante buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Estudiante WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Crear el estudiante con datos básicos
                    Estudiante estudiante = new Estudiante(
                            resultSet.getInt("id"),
                            resultSet.getString("nombre"),
                            resultSet.getString("documento"),
                            resultSet.getString("correo"),
                            resultSet.getString("clave"),
                            resultSet.getInt("creditos_max"),
                            new ArrayList<>(),  // cursos
                            new ArrayList<>(),  // cursosVistos
                            new ArrayList<>()   // carrito
                    );

                    // Cargar las listas relacionadas usando una única conexión
                    try (Connection conn2 = getConnection()) {
                        estudiante.setCursos(obtenerCursosPorEstudianteId(conn2, id));
                        estudiante.setCursosVistos(obtenerCursosVistosPorEstudianteId(conn2, id));
                        estudiante.setCarrito(obtenerCarritoPorEstudianteId(conn2, id));
                    }

                    return estudiante;
                }
                return null;
            }
        }
    }

    private List<Curso> obtenerCursosPorEstudianteId(Connection conn, int estudianteId) throws SQLException {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT c.id, c.capacidad, c.cupos, " +
                "m.id AS materia_id, m.nombre AS materia_nombre, m.descripcion AS materia_descripcion, m.creditos AS materia_creditos " +
                "FROM Curso c " +
                "JOIN Materia m ON c.materia_id = m.id " +
                "JOIN Inscripcion i ON c.id = i.curso_id " +
                "WHERE i.estudiante_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, estudianteId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cursos.add(construirCursoDesdeResultSet(conn, resultSet));
                }
            }
        }
        return cursos;
    }

    private List<Curso> obtenerCursosVistosPorEstudianteId(Connection conn, int estudianteId) throws SQLException {
        List<Curso> cursosVistos = new ArrayList<>();
        String sql = "SELECT c.id, c.capacidad, c.cupos, " +
                "m.id AS materia_id, m.nombre AS materia_nombre, m.descripcion AS materia_descripcion, m.creditos AS materia_creditos " +
                "FROM Curso c " +
                "JOIN Materia m ON c.materia_id = m.id " +
                "JOIN Inscripcion i ON c.id = i.curso_id " +
                "WHERE i.estudiante_id = ? AND i.ha_aprobado = TRUE";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, estudianteId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cursosVistos.add(construirCursoDesdeResultSet(conn, resultSet));
                }
            }
        }
        return cursosVistos;
    }

    private List<Curso> obtenerCarritoPorEstudianteId(Connection conn, int estudianteId) throws SQLException {
        List<Curso> carrito = new ArrayList<>();
        String sql = "SELECT c.id, c.capacidad, c.cupos, " +
                "m.id AS materia_id, m.nombre AS materia_nombre, m.descripcion AS materia_descripcion, m.creditos AS materia_creditos " +
                "FROM Curso c " +
                "JOIN Materia m ON c.materia_id = m.id " +
                "JOIN Carrito car ON c.id = car.curso_id " +
                "WHERE car.estudiante_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, estudianteId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    carrito.add(construirCursoDesdeResultSet(conn, resultSet));
                }
            }
        }
        return carrito;
    }

    private Curso construirCursoDesdeResultSet(Connection conn, ResultSet rs) throws SQLException {
        String cursoId = rs.getString("id");
        int capacidad = rs.getInt("capacidad");
        int cupos = rs.getInt("cupos");

        // Construir la materia directamente desde el ResultSet
        Materia materia = new Materia();
        materia.setiD(rs.getString("materia_id"));
        materia.setNombre(rs.getString("materia_nombre"));
        materia.setDescripcion(rs.getString("materia_descripcion"));
        materia.setCreditos(rs.getInt("materia_creditos"));

        // Obtener horarios y salas
        List<Horario> horarios = obtenerHorariosPorCursoId(conn, cursoId);
        List<Sala> salas = obtenerSalasPorCursoId(conn, cursoId);

        return new Curso(cursoId, materia, capacidad, horarios, salas, cupos);
    }

    private Materia obtenerMateriaPorId(Connection conn, int materiaId) throws SQLException {
        String sql = "SELECT id, nombre, descripcion, creditos FROM Materia WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, materiaId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Materia materia = new Materia();
                    materia.setiD(String.valueOf(materiaId));
                    materia.setNombre(rs.getString("nombre"));
                    materia.setDescripcion(rs.getString("descripcion"));
                    materia.setCreditos(rs.getInt("creditos"));
                    return materia;
                }
                throw new SQLException("Materia no encontrada con ID: " + materiaId);
            }
        }
    }

    private List<Horario> obtenerHorariosPorCursoId(Connection conn, String cursoId) throws SQLException {
        List<Horario> horarios = new ArrayList<>();
        String sql = "SELECT * FROM Horario WHERE materia_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cursoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    horarios.add(new Horario(
                            rs.getString("dia_semana_id"),
                            rs.getTimestamp("hora_inicio"),
                            rs.getTimestamp("hora_fin")
                    ));
                }
            }
        }
        return horarios;
    }

    private List<Sala> obtenerSalasPorCursoId(Connection conn, String cursoId) throws SQLException {
        List<Sala> salas = new ArrayList<>();
        String sql = "SELECT s.* FROM Sala s JOIN Curso c ON c.sala_id = s.id WHERE c.id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cursoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Sala sala = new Sala();
                    sala.setiD(rs.getString("id"));
                    sala.setUbicacion(rs.getString("ubicacion"));
                    sala.setCapacidad(rs.getInt("capacidad"));
                    salas.add(sala);
                }
            }
        }
        return salas;
    }

    public Estudiante validarCredenciales(String usuario, String contrasenia) throws SQLException {
        String query = "SELECT id FROM Estudiante WHERE correo = ? AND clave = ?";
        int estudianteId=-1;

        try (Connection conexion = RepositorioBD.ConexionBaseDeDatos.getConnection();
             PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, usuario);
            ps.setString(2, contrasenia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    estudianteId = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error en la consulta de base de datos: " + e.getMessage());
        }
        Estudiante estudiante;
        EstudianteRepositorio estudianteRepositorio = new EstudianteRepositorio();
        estudiante = estudianteRepositorio.buscarPorId(estudianteId);
        Sesion.getInstancia().setEstudiante(estudiante);
        return estudiante;  // Credenciales incorrectas
    }

    public List<Curso> obtenerCarrito(int estudianteId) throws SQLException {
        try (Connection conn = getConnection()) {
            return obtenerCarritoPorEstudianteId(conn, estudianteId);
        }
    }

    public void agregarAlCarrito(int estudianteId, int cursoId) throws SQLException {
        String sql = "INSERT INTO Carrito (estudiante_id, curso_id) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, estudianteId);
            stmt.setInt(2, cursoId);
            stmt.executeUpdate();
        }
    }

    public void eliminarDelCarrito(int estudianteId, String cursoId) throws SQLException {
        String sql = "DELETE FROM Carrito WHERE estudiante_id = ? AND curso_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, estudianteId);
            stmt.setString(2, cursoId);
            stmt.executeUpdate();
        }
    }

    public boolean cursoEstaEnCarrito(int estudianteId, String cursoId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Carrito WHERE estudiante_id = ? AND curso_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, estudianteId);
            stmt.setString(2, cursoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }


/*
    public List<Estudiante> listaEstudiante() throws SQLException {
        List<Estudiante> estudiantes = new ArrayList<>();
        String consulta = "SELECT * FROM Estudiante";

        try (Connection connection = ConexionBaseDeDatos.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(consulta);
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
                obtenerCursosActivos(estudiante);
                obtenerCursosVistos(estudiante);

                estudiantes.add(estudiante);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la lista de estudiantes: " + e.getMessage());
            throw e;
        }
        return estudiantes;
    }
 */

    private void obtenerCursosActivos(Estudiante estudiante) throws SQLException {
        String queryCurso = "SELECT c.id, c.capacidad, m.id AS materia_id, m.nombre AS materia_nombre " +
                "FROM Curso c " +
                "JOIN Inscripcion i ON c.id = i.curso_id " +
                "JOIN Materia m ON c.materia_id = m.id " +
                "WHERE i.estudiante_id = ? AND i.ha_aprobado = 0";

        try (Connection conexion = ConexionBaseDeDatos.getConnection();
             PreparedStatement psCurso = conexion.prepareStatement(queryCurso)) {
            psCurso.setInt(1, estudiante.getId());
            try (ResultSet rsCurso = psCurso.executeQuery()) {
                while (rsCurso.next()) {
                    estudiante.getCursos().add(obtenerCurso(rsCurso));
                }
            }
        }
    }

    private void obtenerCursosVistos(Estudiante estudiante) throws SQLException {
        String queryCursosVistos = "SELECT c.id, c.capacidad, m.id AS materia_id, m.nombre AS materia_nombre " +
                "FROM Curso c " +
                "JOIN Inscripcion i ON c.id = i.curso_id " +
                "JOIN Materia m ON c.materia_id = m.id " +
                "WHERE i.estudiante_id = ? AND i.ha_aprobado = 1";

        try (Connection conexion = ConexionBaseDeDatos.getConnection();
             PreparedStatement psCursosVisto = conexion.prepareStatement(queryCursosVistos)) {
            psCursosVisto.setInt(1, estudiante.getId());
            try (ResultSet rsCursosVisto = psCursosVisto.executeQuery()) {
                List<Curso> cursosVistos = new ArrayList<>();
                while (rsCursosVisto.next()) {
                    Curso cursoVisto = obtenerCurso(rsCursosVisto);
                    cursosVistos.add(cursoVisto);
                }
                estudiante.setCursosVistos(cursosVistos);
                System.out.println("Estudiante ID: " + estudiante.getId() + " - Cursos Vistos: " + cursosVistos.size());
            }
        }
    }

    // Método auxiliar para obtener cursos y evitar código duplicado
    private Curso obtenerCurso(ResultSet rsCurso) throws SQLException {
        String cursoId = rsCurso.getString("id");
        int capacidad = rsCurso.getInt("capacidad");
        //int cupos = rsCurso.getInt("cupos");
        String materiaId = rsCurso.getString("materia_id");
        String materiaNombre = rsCurso.getString("materia_nombre");

        Materia materia = new Materia();
        materia.setiD(materiaId);
        materia.setNombre(materiaNombre);

        // Obtener horarios del curso
        List<Horario> horarios = new ArrayList<>();
        String queryHorarios = "SELECT hora_inicio, hora_fin FROM Horario WHERE curso_id = ?";
        try (Connection conexion = ConexionBaseDeDatos.getConnection();
             PreparedStatement psHorario = conexion.prepareStatement(queryHorarios)) {
            psHorario.setString(1, cursoId);
            try (ResultSet rsHorario = psHorario.executeQuery()) {
                while (rsHorario.next()) {
                    String dia = rsHorario.getString("dia_semana_id");
                    Date horaInicio = rsHorario.getTimestamp("hora_inicio");
                    Date horaFin = rsHorario.getTimestamp("hora_fin");
                    horarios.add(new Horario(dia, horaInicio, horaFin));
                }
            }
        }

        // Obtener salas del curso
        List<Sala> salas = new ArrayList<>();
        String querySalas = "SELECT s.id, s.ubicacion, s.capacidad FROM Sala s " +
                "JOIN Curso c ON c.sala_id = s.id WHERE c.id = ?";
        try (Connection conexion = ConexionBaseDeDatos.getConnection();
             PreparedStatement psSala = conexion.prepareStatement(querySalas)) {
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

    public Curso obtenerHorarios(String idCurso) throws SQLException {
        Curso curso = new Curso();
        List<Horario> horarios = new ArrayList<>();
        String consulta = "SELECT hora_inicio, hora_fin FROM Horario WHERE curso_id = ?";

        try (Connection conexion = ConexionBaseDeDatos.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
            pstmt.setString(1, idCurso);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String dia = rs.getString("dia_semana_id");
                    Date horaInicio = rs.getTimestamp("hora_inicio");
                    Date horaFin = rs.getTimestamp("hora_fin");
                    horarios.add(new Horario(dia, horaInicio, horaFin));
                }
            }
        }

        curso.setHorarios(horarios);
        return curso;
    }

    public List<Curso> obtenerCursosPorEstudiante(int estudianteId) throws SQLException {
        List<Curso> cursos = new ArrayList<>();

        String consulta = "SELECT m.id, m.nombre, m.creditos, h.hora_inicio, h.hora_fin " +
                "FROM Materia m " +
                "JOIN Inscripcion i ON m.id = i.curso_id " +
                "JOIN Horario h ON m.id = h.curso_id " +
                "WHERE i.estudiante_id = ?";

        try (Connection conexion = ConexionBaseDeDatos.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(consulta)) {

            pstmt.setInt(1, estudianteId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Curso curso = new Curso();
                    curso.setiD(rs.getString("id"));
                    curso.getMateria().setNombre(rs.getString("nombre"));
                    curso.getMateria().setCreditos(rs.getInt("creditos"));
                    String dia = rs.getString("dia_semana_id");
                    Date horaInicio = rs.getTimestamp("hora_inicio");
                    Date horaFin = rs.getTimestamp("hora_fin");
                    curso.getHorarios().add(new Horario(dia, horaInicio, horaFin));
                    cursos.add(curso);
                }
            }
        }

        return cursos;
    }

    public List<Curso> obtenerCursosInscritos(int estudianteId) throws SQLException {
        List<Curso> cursosInscritos = new ArrayList<>();
        String queryCurso = "SELECT c.id, c.capacidad, m.id AS materia_id, m.nombre AS materia_nombre " +
                "FROM Curso c " +
                "JOIN Inscripcion i ON c.id = i.curso_id " +
                "JOIN Materia m ON c.materia_id = m.id " +
                "WHERE i.estudiante_id = ? AND i.ha_aprobado = 0";
        try (Connection conexion = ConexionBaseDeDatos.getConnection();
             PreparedStatement psCurso = conexion.prepareStatement(queryCurso)) {
            psCurso.setInt(1, estudianteId);
            try (ResultSet rsCurso = psCurso.executeQuery()) {
                while (rsCurso.next()) {
                    cursosInscritos.add(obtenerCurso(rsCurso));
                }
            }
        }
        return cursosInscritos;
    }

    // Obtener cursos aprobados (vistos)
    public List<Curso> obtenerCursosVistos(int estudianteId) throws SQLException {
        List<Curso> cursosVistos = new ArrayList<>();
        String queryCursosVistos = "SELECT c.id, c.capacidad, m.id AS materia_id, m.nombre AS materia_nombre " +
                "FROM Curso c " +
                "JOIN Inscripcion i ON c.id = i.curso_id " +
                "JOIN Materia m ON c.materia_id = m.id " +
                "WHERE i.estudiante_id = ? AND i.ha_aprobado = 1";
        try (Connection conexion = ConexionBaseDeDatos.getConnection();
             PreparedStatement psCursosVisto = conexion.prepareStatement(queryCursosVistos)) {
            psCursosVisto.setInt(1, estudianteId);
            try (ResultSet rsCursosVisto = psCursosVisto.executeQuery()) {
                while (rsCursosVisto.next()) {
                    cursosVistos.add(obtenerCurso(rsCursosVisto));
                }
            }
        }
        return cursosVistos;
    }

    // Obtener las materias vistas por el estudiante (solo id y nombre de materias)
    public List<Materia> obtenerMateriasVistasPorEstudiante(int estudianteId) throws SQLException {
        List<Materia> materiasVistas = new ArrayList<>();
        String query = "SELECT m.* FROM Materia m " +
                "JOIN Inscripcion i ON m.id = i.curso_id " +
                "WHERE i.estudiante_id = ? AND i.ha_aprobado = true";
        try (Connection conexion = ConexionBaseDeDatos.getConnection();
             PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, estudianteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Materia materia = new Materia();
                    materia.setiD(String.valueOf(rs.getInt("id")));
                    materia.setNombre(rs.getString("nombre"));
                    materia.setCreditos(rs.getInt("creditos"));

                    materiasVistas.add(materia);
                }
            }
        }
        return materiasVistas;
    }

    public void inscribirCurso(int estudianteId, String cursoId) throws SQLException {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Inicio de la transacción

            // Primero inscribimos el curso
            String sqlInscripcion = "INSERT INTO Inscripcion (estudiante_id, curso_id, ha_aprobado) VALUES (?, ?, 0)";
            try (PreparedStatement stmtInscripcion = conn.prepareStatement(sqlInscripcion)) {
                stmtInscripcion.setInt(1, estudianteId);
                stmtInscripcion.setString(2, cursoId);
                stmtInscripcion.executeUpdate();
            }

            // Luego eliminamos el curso del carrito
            String sqlEliminarCarrito = "DELETE FROM Carrito WHERE estudiante_id = ? AND curso_id = ?";
            try (PreparedStatement stmtEliminar = conn.prepareStatement(sqlEliminarCarrito)) {
                stmtEliminar.setInt(1, estudianteId);
                stmtEliminar.setString(2, cursoId);
                stmtEliminar.executeUpdate();
            }

            conn.commit(); // Confirmar la transacción
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Deshacer la transacción si algo sale mal
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new SQLException("Error al inscribir el curso y eliminar del carrito: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaurar el auto-commit
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean verificarInscripcion(int estudianteId, String cursoId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Inscripcion WHERE estudiante_id = ? AND curso_id = ? AND ha_aprobado = false";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, estudianteId);
            stmt.setString(2, cursoId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean eliminarInscripcion(int estudianteId, String cursoId) throws SQLException {
        String sql = "DELETE FROM Inscripcion WHERE estudiante_id = ? AND curso_id = ? AND ha_aprobado = false";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, estudianteId);
            stmt.setString(2, cursoId);

            int filasAfectadas = stmt.executeUpdate();
            System.out.println("Filas afectadas al eliminar inscripción: " + filasAfectadas);

            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar inscripción: " + e.getMessage());
            System.err.println("EstudianteID: " + estudianteId + ", CursoID: " + cursoId);
            throw e;
        }
    }

    public void imprimirEstadoInscripcion(int estudianteId, String cursoId) throws SQLException {
        String sql = "SELECT * FROM Inscripcion WHERE estudiante_id = ? AND curso_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, estudianteId);
            stmt.setString(2, cursoId);

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Estado de inscripción:");
                if (rs.next()) {
                    System.out.println("Encontrada inscripción:");
                    System.out.println("  EstudianteID: " + rs.getInt("estudiante_id"));
                    System.out.println("  CursoID: " + rs.getString("curso_id"));
                    System.out.println("  Aprobado: " + rs.getBoolean("ha_aprobado"));
                } else {
                    System.out.println("No se encontró inscripción");
                }
            }
        }
    }
}