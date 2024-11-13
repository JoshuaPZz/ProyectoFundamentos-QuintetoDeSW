package Controladores;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControladorSeleccion {

    @FXML
    private Button botonCrearCurso;

    @FXML
    void crearClasePressed(ActionEvent event) {
        try {
            Stage stage = SceneManager.getInstance().openNewWindow("/Pantallas/pantallaCreacionCurso.fxml", "/CssStyle/LoginStyle.css", "Crear curso", true);
            stage.show();
            stage.setResizable(false);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void editarClasePressed(ActionEvent event) {

    }

}
