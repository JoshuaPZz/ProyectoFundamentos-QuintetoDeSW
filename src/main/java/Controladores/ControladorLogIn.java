package Controladores;

import Entidades.Estudiante;
import Entidades.Sesion;
import RepositorioBD.EstudianteRepositorio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.IOException;

public class ControladorLogIn {


    @FXML
    private PasswordField textPass;

    @FXML
    private TextField textUser;

    @FXML
    private Label labelError;

    @FXML
    void logInButtonPressed(ActionEvent event) {

        if (textPass.getText().isEmpty() || textUser.getText().isEmpty()) {
            labelError.setText("Por favor ingrese su usuario y contraseña.");
            return;
        }

        String usuario = textUser.getText();
        String contrasenia = textPass.getText();
        Estudiante estudiante = validarCredenciales(usuario,contrasenia);
        if (estudiante != null) {
            System.out.println("Usuario autenticado correctamente! " + estudiante.getNombre());
            try {
                // Switch to the main application scene
                SceneManager.getInstance().switchScene("ControladorPantallaInscripcion", "/CssStyle/LoginStyle.css", false);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception (e.g., show an error message to the user)
                System.err.println("Error al cambiar de escena: " + e.getMessage());
            }
        } else {
            labelError.setText("Usuario y/o Contraseña Incorrecto!");
        }
    }

    private Estudiante validarCredenciales(String usuario, String contrasenia) {
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
        Estudiante estudiante = new Estudiante();
        EstudianteRepositorio estudianteRepositorio = new EstudianteRepositorio();
        estudiante = estudianteRepositorio.buscarPorId(estudianteId);
        Sesion.getInstancia().setEstudiante(estudiante);
        return estudiante;  // Credenciales incorrectas
    }




}
