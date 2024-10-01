package RepositorioBD;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionBaseDeDatos {

    private Connection conexion;
    private static String conexionURL = "jdbc:sqlserver://SAMUEL\\SQLEXPRESS;databaseName=Prueba;user=sa;password=farito94;encrypt=true;trustServerCertificate=true;";

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
/*
    public static void main(String[] args){
        try {
            Connection conn = ConexionBaseDeDatos.getConnection();

            cerrarConexion();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
  
 */
}