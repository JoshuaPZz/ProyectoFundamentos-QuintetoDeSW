package Controladores;
import Entidades.Curso;
import Entidades.Sesion;
import Servicios.ServicioEstudiante;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;

public class ControladorPantallaInscribirCurso {


    private ServicioEstudiante servicioEstudiante;

    public ControladorPantallaInscribirCurso(ServicioEstudiante servicioEstudiante){
        this.servicioEstudiante = servicioEstudiante;
    }

    @FXML
    private Button botonInscribirCurso;

    @FXML
    private Label labelMensajeError;


    @FXML
    private ListView<Curso> listCursoInscripcion;

    @FXML
    void botonInscribirPressed(ActionEvent event) {
        if (!servicioEstudiante.inscribirCurso(Sesion.getInstancia().getEstudiante(), listCursoInscripcion.getSelectionModel().getSelectedItem())) {
            System.out.println("no boff");
            // labelMensajeError.setText("No se pudo inscribir el curso. Verifique los requisitos o cupos.");
        } else {
            System.out.println("boff");
            //labelMensajeError.setText("Se inscribio correctamente el curso");
        }
    }


    @FXML
    public void initialize() {
        this.listCursoInscripcion.setItems(Sesion.getInstancia().getEstudiante().getCarritosObservable());
    }
}