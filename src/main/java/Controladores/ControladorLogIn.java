package Controladores;

import Entidades.Estudiante;
import Entidades.Profesor;
import Entidades.Sesion;
import RepositorioBD.EstudianteRepositorio;
import RepositorioBD.ProfesorRepositorio;
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
    private ProfesorRepositorio profesorRepositorio;

    public ControladorLogIn(EstudianteRepositorio estudianteRepositorio, ProfesorRepositorio profesorRepositorio) {
        this.estudianteRepositorio = estudianteRepositorio;
        this.profesorRepositorio = profesorRepositorio;
    }

    @FXML
    private PasswordField textPass;

    @FXML
    private TextField textUser;

    @FXML
    private Label labelError;

    @FXML
    void logInButtonPressed(ActionEvent event) throws SQLException {

        if (textPass.getText().isEmpty() || textUser.getText().isEmpty()) {
            labelError.setText("Por favor ingrese su usuario y contraseña.");
            return;
        }

        String usuario = textUser.getText();
        String contrasenia = textPass.getText();
        Estudiante estudiante = estudianteRepositorio.validarCredenciales(usuario,contrasenia);
        Profesor profesor = null;// profesorRepositorio.validarCredenciales(usuario, contrasenia) ;
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
        }
        else if (profesor != null){
            System.out.println("Usuario autenticado correctamente! " + profesor.getNombre());
            try {
                // Switch to the main application scene
                SceneManager.getInstance().switchScene("/Pantallas/PantallaProfesores.fxml", "/CssStyle/LoginStyle.css", false);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception (e.g., show an error message to the user)
                System.err.println("Error al cambiar de escena: " + e.getMessage());
            }
        }
        else {
            labelError.setText("Usuario y/o Contraseña Incorrecto!");
        }
    }






}
