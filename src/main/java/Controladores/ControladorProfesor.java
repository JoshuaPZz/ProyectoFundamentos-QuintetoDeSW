package Controladores;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ControladorProfesor {

    @FXML
    private Button botonGestionClase;

    @FXML
    private Button botonGestionInsc;

    @FXML
    private Button botonGestionSalon;

    @FXML
    private Pane formamosImagePane;

    @FXML
    private AnchorPane gestionarClasePane;

    @FXML
    private AnchorPane gestionarInsripcionPane;

    @FXML
    private AnchorPane gestionarSalonPane;

    @FXML
    private ImageView hboxProfes;

    @FXML
    private HBox hboxTop;

    @FXML
    private Label inscText;

    @FXML
    void gestionClasePressed(ActionEvent event) {
        try {
            Stage cambio = SceneManager.getInstance().openNewWindow("/Pantallas/pantallaSeleccion.fxml", "/CssStyle/LoginStyle.css", null, true);
            cambio.show();
            cambio.setResizable(false);
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

}
