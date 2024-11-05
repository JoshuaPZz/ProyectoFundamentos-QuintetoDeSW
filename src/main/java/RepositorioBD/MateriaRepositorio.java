package RepositorioBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import Entidades.Materia;

public class MateriaRepositorio {
    private Connection getConnection() throws SQLException {
        return ConexionBaseDeDatos.getConnection();
    }

    public Materia obtenerMateriaPorId(int id) throws SQLException {
        Materia materia = null;
        String consulta = "SELECT * FROM Materia WHERE id = ?";
        try (Connection conexion = getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    materia = new Materia();
                    materia.setiD(String.valueOf(rs.getInt("id")));
                    materia.setNombre(rs.getString("nombre"));
                    materia.setDescripcion(rs.getString("descripcion"));
                    materia.setCreditos(rs.getInt("creditos"));
                    materia.setPrerequisitos(obtenerPrerrequisitos(id, conexion));
                    materia.setCorequisitos(obtenerCorrequisitos(id, conexion));
                }
            }
        }
        return materia;
    }

    public String obtenerNombreMateria(String id) {
        int idCurso = Integer.parseInt(id);
        try {
            Materia materia = obtenerMateriaPorId(idCurso);
            return materia != null ? materia.getNombre() : "Materia no encontrada";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error al obtener materia";
        }
    }

    private List<Materia> obtenerPrerrequisitos(int materiaId, Connection conexion) throws SQLException {
        List<Materia> prerrequisitos = new ArrayList<>();
        String consultaPrerrequisitos = "SELECT m.* FROM Materia m JOIN Prerrequisito p ON m.id = p.PrerrequisitoID WHERE p.MateriaID = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(consultaPrerrequisitos)) {
            pstmt.setInt(1, materiaId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Materia materia = new Materia();
                    materia.setiD(String.valueOf(rs.getInt("id")));
                    materia.setNombre(rs.getString("nombre"));
                    materia.setDescripcion(rs.getString("descripcion"));
                    materia.setCreditos(rs.getInt("creditos"));
                    prerrequisitos.add(materia);
                }
            }
        }
        return prerrequisitos;
    }

    private List<Materia> obtenerCorrequisitos(int materiaId, Connection conexion) throws SQLException {
        List<Materia> correquisitos = new ArrayList<>();
        String consultaCorrequisitos = "SELECT m.* FROM Materia m JOIN Correquisito c ON m.id = c.CorrequisitoID WHERE c.MateriaID = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(consultaCorrequisitos)) {
            pstmt.setInt(1, materiaId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Materia materia = new Materia();
                    materia.setiD(String.valueOf(rs.getInt("id")));
                    materia.setNombre(rs.getString("nombre"));
                    materia.setDescripcion(rs.getString("descripcion"));
                    materia.setCreditos(rs.getInt("creditos"));
                    correquisitos.add(materia);
                }
            }
        }
        return correquisitos;
    }

    public List<Materia> listarMaterias() throws SQLException {
        List<Materia> materias = new ArrayList<>();
        String consulta = "SELECT * FROM Materia";
        try (Connection conexion = getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(consulta);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Materia materia = new Materia();
                materia.setiD(String.valueOf(rs.getInt("id")));
                materia.setNombre(rs.getString("nombre"));
                materia.setDescripcion(rs.getString("descripcion"));
                materia.setCreditos(rs.getInt("creditos"));

                // Cargar prerrequisitos y correquisitos
                materia.setPrerequisitos(obtenerPrerrequisitos(rs.getInt("id"), conexion));
                materia.setCorequisitos(obtenerCorrequisitos(rs.getInt("id"), conexion));

                materias.add(materia);
            }
        }
        return materias;
    }

    // Método para agregar una nueva materia
    public void agregarMateria(Materia materia) throws SQLException {
        String insertSQL = "INSERT INTO Materia (nombre, descripcion, creditos) VALUES (?, ?, ?)";
        try (Connection conexion = getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, materia.getNombre());
            pstmt.setString(2, materia.getDescripcion());
            pstmt.setInt(3, materia.getCreditos());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        materia.setiD(String.valueOf(generatedKeys.getInt(1))); // Obtener el ID generado
                        System.out.println("Materia agregada con ID: " + materia.getiD());
                    } else {
                        throw new SQLException("Fallo al obtener el ID generado para la materia.");
                    }
                }
            } else {
                throw new SQLException("No se insertó la materia, no se afectaron filas.");
            }
        }catch (SQLException e){
            System.out.println("Error SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void eliminarMateria(String materiaId) throws SQLException {
        // Primero eliminamos las relaciones en las tablas dependientes
        eliminarRelacionesMateria(materiaId);

        // Luego eliminamos la materia
        String sql = "DELETE FROM Materia WHERE id = ?";

        try (Connection conn = ConexionBaseDeDatos.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, materiaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar la materia: " + e.getMessage());
        }
    }

    private void eliminarRelacionesMateria(String materiaId) throws SQLException {
        try (Connection conn = ConexionBaseDeDatos.getConnection()) {
            // Eliminar prerrequisitos
            String sqlPre = "DELETE FROM Prerrequisito WHERE MateriaID = ? OR PrerrequisitoID = ?";
            try (PreparedStatement stmtPre = conn.prepareStatement(sqlPre)) {
                stmtPre.setString(1, materiaId);
                stmtPre.setString(2, materiaId);
                stmtPre.executeUpdate();
            }

            // Eliminar correquisitos
            String sqlCo = "DELETE FROM Correquisito WHERE MateriaID = ? OR CorrequisitoID = ?";
            try (PreparedStatement stmtCo = conn.prepareStatement(sqlCo)) {
                stmtCo.setString(1, materiaId);
                stmtCo.setString(2, materiaId);
                stmtCo.executeUpdate();
            }

            // Eliminar horarios
            String sqlHorario = "DELETE FROM Horario WHERE materia_id = ?";
            try (PreparedStatement stmtHorario = conn.prepareStatement(sqlHorario)) {
                stmtHorario.setString(1, materiaId);
                stmtHorario.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar las relaciones de la materia: " + e.getMessage());
        }
    }
}
