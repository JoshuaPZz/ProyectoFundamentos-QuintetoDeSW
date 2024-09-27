package Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ControladorLogIn {

    @FXML
    private Button logInButton;

    @FXML
    private Pane logInPane;

    @FXML
    private Text logInTitle;

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
    }




}
