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

    private EstudianteRepositorio estudianteRepositorio;

    public ControladorLogIn(EstudianteRepositorio estudianteRepositorio) {
        this.estudianteRepositorio = estudianteRepositorio;
    }

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
        Estudiante estudiante = estudianteRepositorio.validarCredenciales(usuario,contrasenia);
        if (estudiante != null) {
            System.out.println("Usuario autenticado correctamente! " + estudiante.getNombre());
            try {
                // Switch to the main application scene
                SceneManager.getInstance().switchScene("/Pantallas/pantallaInscripcion.fxml", "/CssStyle/LoginStyle.css", false);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception (e.g., show an error message to the user)
                System.err.println("Error al cambiar de escena: " + e.getMessage());
            }
        } else {
            labelError.setText("Usuario y/o Contraseña Incorrecto!");
        }
    }






}
