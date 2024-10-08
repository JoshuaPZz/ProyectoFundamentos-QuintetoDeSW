package RepositorioBD;
import Entidades.Curso;
import Entidades.Estudiante;
import Entidades.Materia;
import Entidades.Profesor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;


public class ConexionBaseDeDatos {

    private Connection conexion;
    private static String conexionURL = "jdbc:sqlserver://MSI\\SQLEXPRESS;databaseName=master;user=sa;password=gaturro26;encrypt=true;trustServerCertificate=true;";

    private static Connection connection;

    private ConexionBaseDeDatos() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(conexionURL);
                System.out.println("Conexión establecida correctamente.");
            } catch (SQLException e) {
                System.out.println("Error al conectar a la base de datos: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }


    public static void cerrarConexion() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
    }
}