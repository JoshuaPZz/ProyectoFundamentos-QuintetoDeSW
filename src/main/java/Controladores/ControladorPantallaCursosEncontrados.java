    package Controladores;

    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.control.TextField;
    import javafx.stage.Stage;


    import java.io.IOException;

    public class ControladorPantallaCursosEncontrados {

        @FXML
        private Button botonAgregar;

        @FXML
        private Button botonFinalizar;


        @FXML
        private Label labelDisponibles;
        @FXML
        private TextField textIdAgregar;


        public void setLabelInfo(String disponibles) {
            labelDisponibles.setText(disponibles);
        }

        @FXML
        public void agregarButtonPressed(ActionEvent actionEvent) {

            Stage mainStage = SceneManager.getInstance().getPrimaryStage();

            Scene inscripcionScene = mainStage.getScene();
            ControladorPantallaInscripcion controller = (ControladorPantallaInscripcion) inscripcionScene.getUserData();

            if (controller != null) {
                controller.setLabelCarrito(textIdAgregar.getText());
            }
        }
        @FXML
        public void finalizarButtonPressed(ActionEvent actionEvent) {

        }

    }
