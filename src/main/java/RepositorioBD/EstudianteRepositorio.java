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
    public Estudiante buscarPorId(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Estudiante estudiante = null;

        try {
            connection = ConexionBaseDeDatos.getConnection();
            String sql = "SELECT * FROM Estudiante WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Obtener datos principales del estudiante
                int estudianteId = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String documento = resultSet.getString("documento");
                String correo = resultSet.getString("correo");
                String clave = resultSet.getString("clave");
                int creditosMax = resultSet.getInt("creditos_max");

                // Consultar cursos, cursosVistos, y carrito
                List<Curso> cursos = obtenerCursosPorEstudianteId(estudianteId);
                List<Curso> cursosVistos = obtenerCursosVistosPorEstudianteId(estudianteId);
                List<Curso> carrito = obtenerCarritoPorEstudianteId(estudianteId);

                // Llenar el constructor completo
                estudiante = new Estudiante(estudianteId, nombre, documento, correo, clave, creditosMax, cursos, cursosVistos, carrito);
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

    private List<Curso> obtenerCursosPorEstudianteId(int estudianteId) {
        List<Curso> cursos = new ArrayList<>();
        MateriaRepositorio materiaRepositorio = new MateriaRepositorio();
        CursoRepositorio cursoRepositorio = new CursoRepositorio();
        String sql = "SELECT c.id, c.capacidad, c.cupos, m.id AS materia_id" +
                "FROM Curso c " +
                "JOIN Materia m ON c.materia_id = m.id " +
                "JOIN Inscripcion i ON c.id = i.curso_id " +
                "WHERE i.estudiante_id = ?";

        try (Connection connection = ConexionBaseDeDatos.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, estudianteId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String cursoId = resultSet.getString("id");
                int capacidad = resultSet.getInt("capacidad");
                int cupos = resultSet.getInt("cupos");

                // Obtener la entidad Materia
                Materia materia = materiaRepositorio.obtenerMateriaPorId(Integer.parseInt(resultSet.getString("materia_id")));

                // Obtener horarios y salas asociados al curso
                List<Horario> horarios = cursoRepositorio.obtenerHorarios(cursoId);
                List<Sala> salas = cursoRepositorio.obtenerSalasPorCursoId(cursoId);

                // Crear el objeto Curso con todos los atributos completos
                Curso curso = new Curso(cursoId, materia, capacidad, horarios, salas, cupos);
                cursos.add(curso);
                cursos.add(curso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cursos;
    }

    private List<Curso> obtenerCursosVistosPorEstudianteId(int estudianteId) {
        List<Curso> cursosVistos = new ArrayList<>();
        MateriaRepositorio materiaRepositorio = new MateriaRepositorio();
        CursoRepositorio cursoRepositorio = new CursoRepositorio();
        String sql = "SELECT c.id, c.capacidad, c.cupos, m.id AS materia_id " +
                "FROM Curso c " +
                "JOIN Materia m ON c.materia_id = m.id " +
                "JOIN Inscripcion i ON c.id = i.curso_id " +
                "WHERE i.estudiante_id = ? AND i.ha_aprobado = TRUE";

        try (Connection connection = ConexionBaseDeDatos.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, estudianteId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String cursoId = resultSet.getString("id");
                int capacidad = resultSet.getInt("capacidad");
                int cupos = resultSet.getInt("cupos");

                // Obtener la entidad Materia
                Materia materia = materiaRepositorio.obtenerMateriaPorId(Integer.parseInt(resultSet.getString("materia_id")));

                // Obtener horarios y salas asociados al curso
                List<Horario> horarios = cursoRepositorio.obtenerHorarios(cursoId);
                List<Sala> salas = cursoRepositorio.obtenerSalasPorCursoId(cursoId);

                // Crear el objeto Curso con todos los atributos completos
                Curso curso = new Curso(cursoId, materia, capacidad, horarios, salas, cupos);
                cursosVistos.add(curso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cursosVistos;
    }

    private List<Curso> obtenerCarritoPorEstudianteId(int estudianteId) {
        List<Curso> carrito = new ArrayList<>();
        MateriaRepositorio materiaRepositorio = new MateriaRepositorio();
        CursoRepositorio cursoRepositorio = new CursoRepositorio();
        String sql = "SELECT c.id, c.capacidad, c.cupos, m.id AS materia_id " +
                "FROM Curso c " +
                "JOIN Materia m ON c.materia_id = m.id " +
                "JOIN Carrito car ON c.id = car.curso_id " +
                "WHERE car.estudiante_id = ?";

        try (Connection connection = ConexionBaseDeDatos.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, estudianteId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String cursoId = resultSet.getString("id");
                int capacidad = resultSet.getInt("capacidad");
                int cupos = resultSet.getInt("cupos");

                // Obtener la entidad Materia
                Materia materia = materiaRepositorio.obtenerMateriaPorId(Integer.parseInt(resultSet.getString("materia_id")));

                // Obtener horarios y salas asociados al curso
                List<Horario> horarios = cursoRepositorio.obtenerHorarios(cursoId);
                List<Sala> salas = cursoRepositorio.obtenerSalasPorCursoId(cursoId);

                // Crear el objeto Curso con todos los atributos completos
                Curso curso = new Curso(cursoId, materia, capacidad, horarios, salas, cupos);
                carrito.add(curso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carrito;
    }

    public Estudiante validarCredenciales(String usuario, String contrasenia) {
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
        List<Curso> carrito = new ArrayList<>();
        CursoRepositorio cursoRepositorio = new CursoRepositorio();
        String sql = "SELECT c.id, c.cupos, c.capacidad, c.materia_id, c.sala_id, c.estado_id " +
                "FROM Carrito ca " +
                "JOIN Curso c ON ca.curso_id = c.id " +
                "WHERE ca.estudiante_id = ?";

        try (Connection conn = ConexionBaseDeDatos.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, estudianteId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Recupera los datos de la tabla Curso
                String cursoID = resultSet.getString("id");
                int capacidad = resultSet.getInt("capacidad");
                int cupos = resultSet.getInt("cupos");
                int materiaId = resultSet.getInt("materia_id");
                String salaId = resultSet.getString("sala_id");

                // Recupera la materia asociada al curso
                Materia materia = cursoRepositorio.obtenerMateriaporCursoID(cursoID);

                // Recupera los horarios asociados al curso
                List<Horario> horarios = cursoRepositorio.obtenerHorarios(cursoID);

                // Recupera las salas asociadas al curso
                List<Sala> salas = cursoRepositorio.obtenerSalasPorCursoId(salaId);

                // Crea el objeto Curso
                Curso curso = new Curso(cursoID, materia, capacidad, horarios, salas, cupos);
                carrito.add(curso);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el carrito: " + e.getMessage());
            throw e;
        }

        return carrito;
    }

    public void agregarAlCarrito(int estudianteId, int cursoId) throws SQLException {
        String sql = "INSERT INTO Carrito (estudiante_id, curso_id) VALUES (?, ?)";
        try (Connection conn = ConexionBaseDeDatos.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, estudianteId);
            stmt.setInt(2, cursoId);
            stmt.executeUpdate();
        }
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
        String sql = "INSERT INTO Inscripcion (estudiante_id, curso_id, ha_aprobado) VALUES (?, ?, 0)";
        try (Connection connection = ConexionBaseDeDatos.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, estudianteId);
            ps.setString(2, cursoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al inscribir el curso: " + e.getMessage());
            throw e;
        }
    }

    public Boolean eliminarInscripcion(int estudianteId, String cursoId) throws SQLException {
        String sql = "DELETE FROM Inscripcion WHERE estudiante_id = ? and curso_id = ? AND ha_aprobado = 0";
        try (Connection connection = ConexionBaseDeDatos.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, estudianteId);
            ps.setString(2, cursoId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e){
            System.out.println("Error al eliminar inscripción: " + e.getMessage());
            return false;
        }
    }
}