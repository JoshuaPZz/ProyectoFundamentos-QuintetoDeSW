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
    public void insertarSala(Sala sala) throws SQLException {
        String query = "INSERT INTO Sala (ubicacion, capacidad, tipo) VALUES (?, ?, ?)";
        try (Connection conexion = getConnection();
            PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, sala.getUbicacion());
            ps.setInt(2, sala.getCapacidad());
            ps.setString(3, sala.getTipo());
            ps.executeUpdate();
        }
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
}
