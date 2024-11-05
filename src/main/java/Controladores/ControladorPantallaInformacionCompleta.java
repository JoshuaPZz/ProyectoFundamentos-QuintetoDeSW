package Controladores;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ControladorPantallaInformacionCompleta {

    @FXML
    private Button botonExitVerInfo;

    @FXML
    private Text labelInformacionCompleta;

    @FXML
    void botonExitVerInfoPressed(ActionEvent event) {
        this.botonExitVerInfo.getScene().getWindow().hide();
    }

    @FXML
    void initialize() {
        VBox parentVbox = (VBox) labelInformacionCompleta.getParent();
        this.labelInformacionCompleta.wrappingWidthProperty().bind(parentVbox.widthProperty());
    }
    public Text getLabelInformacionCompleta() {
        return this.labelInformacionCompleta;

    }
}
