package Entidades;

import Controladores.SceneManager;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class Principal extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Set the primary stage in SceneManager
        SceneManager.getInstance().setPrimaryStage(stage);

        // Load custom font
        Font.loadFont(getClass().getResourceAsStream("/fonts/Lato-Bold.ttf"), 10);
        Font.loadFont(getClass().getResourceAsStream("/fonts/LeagueGothic.ttf"), 10);
        // Set stage properties
        stage.setTitle("Bienvenido......");
        stage.setResizable(false);

        // Switch to the login scene using SceneManager
        SceneManager.getInstance().switchScene("/Pantallas/PantallaLogin.fxml", "/CssStyle/LoginStyle.css");

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}