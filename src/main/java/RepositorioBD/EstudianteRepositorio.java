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
    public Estudiante buscarPorId(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Estudiante estudiante = null;

        try {
            // Obtener la conexión a la base de datos
            connection = ConexionBaseDeDatos.getConnection();

            // Preparar la consulta SQL
            String sql = "SELECT * FROM Estudiante WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            // Ejecutar la consulta
            resultSet = statement.executeQuery();

            // Procesar el resultado
            if (resultSet.next()) {
                estudiante = new Estudiante();
                estudiante.setId(resultSet.getInt("id"));
                estudiante.setNombre(resultSet.getString("nombre"));
                estudiante.setCreditosmax(resultSet.getInt("creditos_max"));
                // Cargar más campos según lo que tengas en la tabla Estudiante
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar recursos
            try { if (resultSet != null) resultSet.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return estudiante; // Retorna null si no se encuentra el estudiante
    }

    public List<Estudiante> listaEstudiante() throws SQLException {
        List<Estudiante> estudiantes = new ArrayList<>();
        String consulta = "SELECT * FROM Estudiante";

        try (Connection conexion = getConnection2();
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

                // Obtener cursos activos y vistos con conexiones separadas
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

    private void obtenerCursosActivos(Estudiante estudiante) throws SQLException {
        String queryCurso = "SELECT c.id, c.capacidad, m.id AS materia_id, m.nombre AS materia_nombre " +
                "FROM Curso c " +
                "JOIN Inscripcion i ON c.id = i.curso_id " +
                "JOIN Materia m ON c.materia_id = m.id " +
                "WHERE i.estudiante_id = ? AND i.ha_aprobado = 0";

        try (Connection conexion = getConnection2();
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

        try (Connection conexion = getConnection2();
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

    // Asegúrate de que este método siempre devuelve una nueva conexión activa
    private Connection getConnection2() throws SQLException {
        Connection connection = null;
        Connection conexion;
        String conexionURL = "jdbc:sqlserver://MSI\\SQLEXPRESS;databaseName=master;user=sa;password=gaturro26;encrypt=true;trustServerCertificate=true;";
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(conexionURL);
                //System.out.println("Conexión establecida correctamente.");
            } catch (SQLException e) {
                System.out.println("Error al conectar a la base de datos: " + e.getMessage());
                throw e;
            }
        }
        return connection;
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
        List<Date> horarios = new ArrayList<>();
        String queryHorarios = "SELECT hora_inicio, hora_fin FROM Horario WHERE curso_id = ?";
        try (Connection conexion = getConnection2();
             PreparedStatement psHorario = conexion.prepareStatement(queryHorarios)) {
            psHorario.setString(1, cursoId);
            try (ResultSet rsHorario = psHorario.executeQuery()) {
                while (rsHorario.next()) {
                    Timestamp hora_inicio = rsHorario.getTimestamp("hora_inicio");
                    Timestamp hora_fin = rsHorario.getTimestamp("hora_fin");

                    horarios.add(hora_inicio);
                    horarios.add(hora_fin);
                }
            }
        }

        // Obtener salas del curso
        List<Sala> salas = new ArrayList<>();
        String querySalas = "SELECT s.id, s.ubicacion, s.capacidad FROM Sala s " +
                "JOIN Curso c ON c.sala_id = s.id WHERE c.id = ?";
        try (Connection conexion = getConnection2();
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
        List<Date> horarios = new ArrayList<>();
        String consulta = "SELECT hora_inicio, hora_fin FROM Horario WHERE curso_id = ?";

        try (Connection conexion = getConnection2();
             PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
            pstmt.setString(1, idCurso);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Timestamp hora_inicio = rs.getTimestamp("hora_inicio");
                    Timestamp hora_fin = rs.getTimestamp("hora_fin");
                    horarios.add(hora_inicio);
                    horarios.add(hora_fin);
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

        try (Connection conexion = getConnection2();
             PreparedStatement pstmt = conexion.prepareStatement(consulta)) {

            pstmt.setInt(1, estudianteId); // Usar el ID del estudiante en la consulta

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Crear un objeto Curso y asignar valores obtenidos del ResultSet
                    Curso curso = new Curso();

                    curso.setiD(rs.getString("id"));
                    curso.getMateria().setNombre(rs.getString("nombre"));
                    curso.getMateria().setCreditos(rs.getInt("creditos"));

                    // Asignar horarios (hora_inicio y hora_fin) a la lista de horarios del curso
                    Timestamp horaInicio = rs.getTimestamp("hora_inicio");
                    Timestamp horaFin = rs.getTimestamp("hora_fin");

                    // Si ya tienes una lista de horarios en el curso, añadimos ambas fechas
                    curso.getHorarios().add(new Date(horaInicio.getTime()));
                    curso.getHorarios().add(new Date(horaFin.getTime()));

                    // Añadir el curso a la lista de cursos
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
        try (Connection conexion = getConnection2();
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
        try (Connection conexion = getConnection2();
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
                "WHERE i.estudiante_id = ? AND i.ha_aprobado = 1";
        try (Connection conexion = getConnection2();
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

    // Método para inscribir un curso al estudiante en la base de datos
    public boolean inscribirCursoAlEstudiante(Estudiante estudiante, Curso curso) {
        if (estudiante == null || curso == null) {
            System.out.println("Estudiante o curso no válidos.");
            return false;
        }

        // Consulta SQL para insertar la inscripción en la tabla 'Inscripcion'
        String sql = "INSERT INTO Inscripcion (estudiante_id, curso_id) VALUES (?, ?)";

        try (Connection conexion = getConnection2();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, estudiante.getId());  // Asignar el ID del estudiante
            stmt.setString(2, curso.getiD());       // Asignar el ID del curso

            int filasAfectadas = stmt.executeUpdate();  // Ejecutar la inserción

            if (filasAfectadas > 0) {
                System.out.println("Inscripción en la base de datos completada para el curso: " + curso.getMateria().getNombre());
                return true;
            } else {
                System.out.println("No se pudo insertar la inscripción en la base de datos.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al inscribir el curso en la base de datos: " + e.getMessage());
            return false;
        }
    }
}