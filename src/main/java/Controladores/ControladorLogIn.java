package Controladores;

import Entidades.Estudiante;
import Entidades.Profesor;
import Entidades.Sesion;
import RepositorioBD.EstudianteRepositorio;
import RepositorioBD.ProfesorRepositorio;
import Servicios.ServicioAutenticacion;
import Servicios.ServicioEstudiante;
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

    private ServicioAutenticacion servicioAutenticacion;

    public ControladorLogIn() {
        this.servicioAutenticacion = new ServicioAutenticacion();
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
        ServicioAutenticacion.ResultadoAutenticacion resultado = servicioAutenticacion.validarCredenciales(usuario, contrasenia);
        switch (resultado.getTipo()) {
            case ESTUDIANTE:
                Estudiante estudiante = resultado.getEstudiante();
                if (estudiante != null) {
                    System.out.println("Estudiante autenticado: " + estudiante.getNombre());
                    cambiarAEscenaEstudiante();
                } else {
                    labelError.setText("Error al cargar datos del estudiante");
                }
                break;

            case PROFESOR:
                Profesor profesor = resultado.getProfesor();
                if (profesor != null) {
                    System.out.println("Profesor autenticado: " + profesor.getNombre() + " " + profesor.getApellido());
                    cambiarAEscenaProfesor();
                } else {
                    labelError.setText("Error al cargar datos del profesor");
                }
                break;

            case NO_ENCONTRADO:
                labelError.setText("Usuario o contraseña incorrectos");
                break;
        }
    }

    private void cambiarAEscenaEstudiante() {
        try {
            SceneManager.getInstance().switchScene(
                    "/Pantallas/pantallaInscripcion.fxml",
                    "/CssStyle/LoginStyle.css",
                    false
            );
        } catch (IOException e) {
            manejarErrorCambioEscena(e);
        }
    }

    private void cambiarAEscenaProfesor() {
        try {
            SceneManager.getInstance().switchScene(
                    "/Pantallas/PantallaProfesores.fxml",
                    "/CssStyle/LoginStyle.css",
                    false
            );
        } catch (IOException e) {
            manejarErrorCambioEscena(e);
        }
    }

    private void manejarErrorCambioEscena(IOException e) {
        e.printStackTrace();
        labelError.setText("Error al cambiar de pantalla");
        System.err.println("Error al cambiar de escena: " + e.getMessage());
    }






}
