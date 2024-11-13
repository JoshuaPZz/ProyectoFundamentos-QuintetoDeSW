package RepositorioBD;
import Entidades.Sala;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaRepositorio {
    private Connection getConnection() throws SQLException {
        return ConexionBaseDeDatos.getConnection();
    }

    // Método para insertar una sala en la base de datos
    public boolean insertarSala(Sala sala) throws SQLException {
        String query = "INSERT INTO Sala (ubicacion, capacidad, tipo) VALUES (?, ?, ?)";

        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, sala.getUbicacion());
            ps.setInt(2, sala.getCapacidad());
            ps.setString(3, sala.getTipo());

            // Ejecutar la inserción
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                // Obtener el ID generado automáticamente
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        sala.setiD(rs.getString(1));  // Obtener el ID (por lo general es el primer campo)
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar la sala: " + e.getMessage());
            throw e;
        }

        return false;
    }

    public List<Sala> obtenerSalas() throws SQLException {
        List<Sala> salas = new ArrayList<>();
        String sql = "SELECT * FROM Sala";

        try (Connection conn = ConexionBaseDeDatos.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Sala sala = new Sala();
                sala.setiD(resultSet.getString("id"));
                sala.setUbicacion(resultSet.getString("ubicacion"));
                sala.setCapacidad(resultSet.getInt("capacidad"));

                sala.setTipo(resultSet.getString("tipo"));
                salas.add(sala);
            }
        }
        return salas;
    }

    // Método para actualizar una sala
    public void actualizarSala(Sala sala) throws SQLException {
        String query = "UPDATE Sala SET ubicacion = ?, capacidad = ?, tipo = ? WHERE id = ?";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, sala.getUbicacion());
            ps.setInt(2, sala.getCapacidad());
            ps.setString(3, sala.getTipo());
            ps.setInt(4, Integer.parseInt(sala.getiD()));
            ps.executeUpdate();
        }
    }

    // Método para eliminar una sala
    public void eliminarSala(int id) throws SQLException {
        String query = "DELETE FROM Sala WHERE id = ?";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Sala obtenerSalaPorId(String salaId) throws SQLException {
        String query = "SELECT * FROM Sala WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, salaId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Sala sala = new Sala();
                    sala.setiD(resultSet.getString("ID"));
                    sala.setUbicacion(resultSet.getString("ubicacion"));
                    sala.setCapacidad(resultSet.getInt("capacidad"));
                    sala.setTipo(resultSet.getString("tipo"));
                    return sala;
                }
            }
        }
        return null;
    }

    public int obtenerCapacidadSala(String salaId) throws SQLException {
        int capacidad = 0;
        String query = "SELECT capacidad FROM Sala WHERE ID = ?";
        try (Connection conexion = getConnection();
        PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, salaId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    capacidad = resultSet.getInt("capacidad");
                }
            }
        }
        return capacidad;
    }
}
