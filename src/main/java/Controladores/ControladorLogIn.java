package Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ControladorLogIn {


    @FXML
    private PasswordField textPass;

    @FXML
    private TextField textUser;

    @FXML
    void logInButtonPressed(ActionEvent event) {



        /*
        Realizar la consulta de bases de datos y pasar datos de usuario al metodo
        getText() de cada campo
        textPass = campo de contraseña
        textUser = campo de usuario
        Imprimir por consola la revision del usuario para luego hacer cambio de vista
         */
        if(textPass.getText().equals("") && textUser.getText().equals("")) {
            System.out.println("Si es el usuario!!!");
        }
        try {
            // Switch to the main application scene
            SceneManager.getInstance().switchScene("/Pantallas/pantallaInscripcion.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message to the user)
            System.err.println("Error al cambiar de escena: " + e.getMessage());
        }
    }




}
