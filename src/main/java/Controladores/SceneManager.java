package Controladores;

import Servicios.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private static SceneManager instance;
    private Stage primaryStage;
    private Stage secondaryStage;
    private final Map<String, FXMLLoader> loaders = new HashMap<>();



    private SceneManager() {}

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public FXMLLoader findLoader(String controller) {
        if (loaders.containsKey(controller)) {
            return loaders.get(controller);
        }

        FXMLLoader loader = null;
        String fxmlPath = switch (controller) {
            case "ControladorPantallaInscripcion" -> "/Pantallas/pantallaInscripcion.fxml";
            case "ControladorPantallaCursosEncontrados" -> "/Pantallas/pantallaCursosEncontrados.fxml";
            case "ControladorPantallaBusqueda" -> "/Pantallas/pantallaBusqueda.fxml";
            case "ControladorLogIn" -> "/Pantallas/PantallaLogin.fxml";
            default -> null;
        };

        if (fxmlPath != null) {
            loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loaders.put(controller, loader);
        }

        return loader;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public <T> T getController(String fxmlPath) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            if (loader.getLocation() == null) {
                return null;
            }
            loader.load();
            return loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void switchScene(String controllerName, String cssPath, boolean secondary) throws IOException {
        FXMLLoader loader = loaders.get(controllerName);
        if (loader == null) {
            throw new IllegalStateException("No hay loader para el controlador: " + controllerName);
        }

        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        scene.setUserData(loader.getController());

        if (cssPath != null && !cssPath.isEmpty()) {
            scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        }
        if(!secondary) {
            primaryStage.setScene(scene);
        }
        else{
            secondaryStage.setScene(scene);
        }



        if (controllerName.equals("ControladorPantallaInscripcion")) {
            primaryStage.setMaximized(true);
        }
    }

    public void cargarServicios(Object controller){

    }

    public Stage openNewWindow(String controllerName, String cssPath, String title, boolean isModal) throws IOException {
        Stage newStage = new Stage();
        FXMLLoader loader = loaders.get(controllerName);
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        scene.setUserData(loader.getController());
        scene.getStylesheets().add(getClass().getResource("/CssStyle/LoginStyle.css").toExternalForm());
        newStage.setScene(scene);
        newStage.setTitle(title);

        if (isModal) {
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.initOwner(primaryStage);
        }
        secondaryStage = newStage;
        return newStage;
    }
}