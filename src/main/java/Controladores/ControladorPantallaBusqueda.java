package Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ControladorPantallaBusqueda {

    @FXML
    private Button botonBuscarId;


    @FXML
    void buscarIdButtonPressed(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pantallas/pantallaCursosEncontrados.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ControladorPantallaCursosEncontrados cursosEncontrados = loader.getController();
        cursosEncontrados.setLabelInfo("HOLA");

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/CssStyle/LoginStyle.css").toExternalForm());

        currentStage.setScene(scene);

    }


}
