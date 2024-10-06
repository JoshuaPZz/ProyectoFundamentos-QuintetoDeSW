package Controladores;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void switchScene(String fxmlPath, String cssPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root);


        if (cssPath != null && !cssPath.isEmpty()) {
            scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        }

        primaryStage.setScene(scene);
        if(fxmlPath != "/Pantallas/pantallaLogin.fxml" && !primaryStage.isMaximized())
            primaryStage.setMaximized(true);

    }
}