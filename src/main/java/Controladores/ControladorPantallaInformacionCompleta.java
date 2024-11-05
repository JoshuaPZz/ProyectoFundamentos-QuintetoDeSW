package Controladores;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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
    void initialize() {;
        VBox parentVbox = (VBox) labelInformacionCompleta.getParent();

        // Configure VBox
        parentVbox.setFillWidth(true);
        labelInformacionCompleta.setTextAlignment(TextAlignment.CENTER);
        // Bind the text wrapping width
        labelInformacionCompleta.wrappingWidthProperty().bind(
                parentVbox.widthProperty().subtract(20)
        );

        // Force layout recalculation when text changes
        labelInformacionCompleta.textProperty().addListener((obs, oldText, newText) -> {
            if (parentVbox.getScene() != null && parentVbox.getScene().getWindow() != null) {
                parentVbox.getScene().getWindow().sizeToScene();
            }
        });

    }
    public Text getLabelInformacionCompleta() {
        return this.labelInformacionCompleta;

    }
}
