package Controladores;

import RepositorioBD.CursoRepositorio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ControladorPantallaBusqueda {

    CursoRepositorio repositorio = new CursoRepositorio(); //Se debe hacer la inyeccion desde principal

    @FXML
    private Button botonBuscarId;
    @FXML
    private TextField cursoPorIdLabel;


    @FXML
    void buscarIdButtonPressed(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pantallas/pantallaCursosEncontrados.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ControladorPantallaCursosEncontrados cursosEncontrados = loader.getController();
        List<String> cursos = repositorio.obtenerCursosPorMateria(cursoPorIdLabel.getText());
        if (!cursos.isEmpty()) {
            cursosEncontrados.setLabelInfo("\n--- Cursos de la materia con ID: " + cursoPorIdLabel.getText() + " ---");
            for (String cursoInfo : cursos) {

                System.out.println(cursoInfo);
                cursosEncontrados.setLabelInfo(cursoInfo);
            }
        } else {
            System.out.println("No se encontraron cursos para la materia con ID: " + cursoPorIdLabel.getText());
        }


        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/CssStyle/LoginStyle.css").toExternalForm());

        currentStage.setScene(scene);
/*
        System.out.print("\nIntroduce el ID de la materia: ");
        String materiaId = scanner.next();
        List<String> cursos = cursoRepositorio.obtenerCursosPorMateria(materiaId);

        if (!cursos.isEmpty()) {
            System.out.println("\n--- Cursos de la materia con ID: " + materiaId + " ---");
            for (String cursoInfo : cursos) {
                System.out.println(cursoInfo);
            }
        } else {
            System.out.println("No se encontraron cursos para la materia con ID: " + materiaId);
        }
        break;

 */

    }


}
