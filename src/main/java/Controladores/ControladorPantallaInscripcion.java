package Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorPantallaInscripcion {

    @FXML
    private AnchorPane backInscPane;

    @FXML
    private Button botonBaja;

    @FXML
    private Button botonBuscar;

    @FXML
    private Button botonInscribir;

    @FXML
    private AnchorPane buscarMateriaPane;

    @FXML
    private AnchorPane cambiarCargaPane;

    @FXML
    private AnchorPane darBajaPane;

    @FXML
    private HBox hboxTop;

    @FXML
    private Label inscText;

    @FXML
    private AnchorPane inscribirMateriaPane;

    @FXML
    private Label labelCarritoTitle;

    @FXML
    private Label labelInscritasTitle;

    @FXML
    private AnchorPane revisarPlanPane;
    @FXML
    private Label labelInscritas;
    @FXML
    private Label labelCarrito;

    @FXML
    void botonBuscarPressed(ActionEvent event){


        try {
            Stage stageBuscar = SceneManager.getInstance().openNewWindow("/Pantallas/pantallaBusqueda.fxml", "/CssStyle/LoginStyle.css", "Buscar", true);
            stageBuscar.show();
            stageBuscar.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void setLabelCarrito(String string) {
        labelCarrito.setText(labelCarrito.getText() + "\n" + string);
    }

}