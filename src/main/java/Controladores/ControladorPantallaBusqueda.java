package Controladores;

import RepositorioBD.CursoRepositorio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.List;

public class ControladorPantallaBusqueda {

    CursoRepositorio repositorio = new CursoRepositorio(); //Se debe hacer la inyeccion desde principal

    @FXML
    private Button botonBuscarId;
    @FXML
    private TextField cursoPorIdLabel;

    //INYECCION DEL SERVICIO ESTUDIANTE POR MEDIO DE CONSTRUCTOR


    @FXML
    void buscarIdButtonPressed(ActionEvent event) throws Exception {
        /*
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pantallas/pantallaCursosEncontrados.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        */

        SceneManager.getInstance().switchScene("/Pantallas/pantallaCursosEncontrados.fxml", "/CssStyle/LoginStyle.css", true);
        List<String> cursos = repositorio.obtenerCursosPorMateria(cursoPorIdLabel.getText());
        if (!cursos.isEmpty()) {
            ControladorPantallaCursosEncontrados controladorPantallaCursosEncontrados = (ControladorPantallaCursosEncontrados) SceneManager.getInstance().getControllers().get("/Pantallas/pantallaCursosEncontrados.fxml");
            controladorPantallaCursosEncontrados.setLabelInfo("\n--- Cursos de la materia con ID: " + cursoPorIdLabel.getText() + " ---");
            for (String cursoInfo : cursos) {
                System.out.println(cursoInfo);
                controladorPantallaCursosEncontrados.setLabelInfo(controladorPantallaCursosEncontrados.getLabelInfo().getText() + "\n" + cursoInfo);
            }
        } else {
            System.out.println("No se encontraron cursos para la materia con ID: " + cursoPorIdLabel.getText());
        }


        /*
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/CssStyle/LoginStyle.css").toExternalForm());

        currentStage.setScene(scene);

         */

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
