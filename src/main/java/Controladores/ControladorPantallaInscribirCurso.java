package Controladores;
import Entidades.Curso;
import Entidades.Sesion;
import Servicios.ServicioEstudiante;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;

public class ControladorPantallaInscribirCurso {


    private ServicioEstudiante servicioEstudiante;

    @FXML
    private Button botonInscribirCurso;

    @FXML
    private ListView<Curso> listCursoInscripcion;

    @FXML
    void botonInscribirPressed(ActionEvent event) {
        ControladorPantallaInscripcion controladorPantallaInscripcion = (ControladorPantallaInscripcion) SceneManager.getInstance().getControllers().get("/Pantallas/pantallaInscripcion.fxml");
        servicioEstudiante.inscribirCurso(Sesion.getInstancia().getEstudiante(), listCursoInscripcion.getSelectionModel().getSelectedItem());
    }
    public ControladorPantallaInscribirCurso(ServicioEstudiante servicioEstudiante){
        this.servicioEstudiante = servicioEstudiante;
    }

    @FXML
    public void initialize() {
        this.listCursoInscripcion.setItems(Sesion.getInstancia().getEstudiante().getCarritosObservable());
    }
}