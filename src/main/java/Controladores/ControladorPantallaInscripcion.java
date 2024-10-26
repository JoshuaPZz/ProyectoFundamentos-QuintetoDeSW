package Controladores;

import Entidades.Curso;
import javafx.collections.FXCollections;
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
import javafx.scene.control.ListView;
import java.awt.*;
import javafx.collections.ObservableList;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    private ListView<String> listViewCarrito;

    @FXML
    void botonBuscarPressed(ActionEvent event){

        try {
            Stage stageBuscar = SceneManager.getInstance().openNewWindow("ControladorPantallaBusqueda", "/CssStyle/LoginStyle.css", "Buscar", true);
            stageBuscar.show();
            stageBuscar.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void setLabelCarrito(String string) {
        labelCarrito.setText(labelCarrito.getText() + "\n" + string);
    }
    void setListViewCarrito(ArrayList<String> materias) {
        ObservableList<String> observableMaterias = FXCollections.observableArrayList(materias);
        listViewCarrito.setItems(observableMaterias);
    }


    @FXML
    void revisarButtonPressed(ActionEvent event) {
        File archivoPDF = new File("src/main/resources/Images/Plan_Ing_Sistemas_v5.pdf");

        if (archivoPDF.exists()) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(archivoPDF);
                } catch (IOException e) {
                    System.out.println("Error al abrir el archivo: " + e.getMessage());
                }
            } else {
                System.out.println("Abrir PDF no soportado en esta plataforma.");
            }
        } else {
            System.out.println("El archivo no existe.");
        }
    }

}