package Controladores;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneManager {
    private static SceneManager instance;
    private Stage primaryStage;

    private SceneManager() {}

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void switchScene(Stage stage, String fxmlPath, String cssPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        // Get the controller instance and set it as user data for the scene
        if(fxmlPath == "/Pantallas/pantallaInscripcion.fxml") {
            ControladorPantallaInscripcion controller = loader.getController();
            scene.setUserData(controller);
        }

        if (cssPath != null && !cssPath.isEmpty()) {
            scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        }

        stage.setScene(scene);  // Use the passed stage

        if(fxmlPath.equals("/Pantallas/pantallaInscripcion.fxml") && !stage.isMaximized())
            stage.setMaximized(true);
    }

    // Method for switching scenes only on primary stage
    public void switchScene(String fxmlPath, String cssPath) throws IOException {
        switchScene(primaryStage, fxmlPath, cssPath);
    }

    public Stage openNewWindow(String fxmlPath, String cssPath, String title, boolean isModal) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        if (cssPath != null && !cssPath.isEmpty()) {
            scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        }

        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setTitle(title);

        if (isModal) {
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.initOwner(primaryStage);
        }

        return newStage;
    }

}
