package Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControladorLogIn {


    @FXML
    private PasswordField textPass;

    @FXML
    private TextField textUser;

    @FXML
    void logInButtonPressed(ActionEvent event) {

        if (textPass.getText().isEmpty() || textUser.getText().isEmpty()) {
            System.out.println("Por favor ingrese su usuario y contraseña.");
            return;
        }

        String usuario = textUser.getText();
        String contrasenia = textPass.getText();

        if (validarCredenciales(usuario, contrasenia)) {
            System.out.println("Usuario autenticado correctamente!");
            try {
                // Switch to the main application scene
                SceneManager.getInstance().switchScene("/Pantallas/pantallaInscripcion.fxml", "/CssStyle/LoginStyle.css");
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception (e.g., show an error message to the user)
                System.err.println("Error al cambiar de escena: " + e.getMessage());
            }
        } else {
            System.out.println("Usuario y/o Contraseña Incorrecto!");
        }
    }

    private boolean validarCredenciales(String usuario, String contrasenia) {
        String query = "SELECT COUNT(*) FROM Estudiante WHERE correo = ? AND clave = ?";

        try (Connection conexion = RepositorioBD.ConexionBaseDeDatos.getConnection();
             PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, usuario);
            ps.setString(2, contrasenia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;  // Si el resultado es mayor a 0, las credenciales son correctas
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error en la consulta de base de datos: " + e.getMessage());
        }
        return false;  // Credenciales incorrectas
    }
}




        /*
        Realizar la consulta de bases de datos y pasar datos de usuario al metodo
        getText() de cada campo
        textPass = campo de contraseña
        textUser = campo de usuario
        Imprimir por consola la revision del usuario para luego hacer cambio de vista

        if(textPass.getText().equals("") && textUser.getText().equals("")) {
            System.out.println("Si es el usuario!!!");
        }
        try {
            // Switch to the main application scene
            SceneManager.getInstance().switchScene("/Pantallas/pantallaInscripcion.fxml", "/CssStyle/LoginStyle.css");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message to the user)
            System.err.println("Error al cambiar de escena: " + e.getMessage());
        }*/


