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

public class ControladorPantallaInscribirCurso {


    private ServicioEstudiante servicioEstudiante;
    @FXML
    private Button botonInscribirCurso;

    @FXML
    private ListView<Curso> listCursoInscripcion;

    @FXML
    void botonInscribirPressed(ActionEvent event) {

    }
    public ControladorPantallaInscribirCurso(ServicioEstudiante servicioEstudiante){
        this.servicioEstudiante = servicioEstudiante;
    }

    @FXML
    public void initialize() {
        ArrayList<Curso>  cursos = servicioEstudiante.verCarrito(Sesion.getInstancia().getEstudiante());
        ObservableList<Curso> observableCursos = FXCollections.observableArrayList(cursos);

        this.listCursoInscripcion.setItems(observableCursos);
    }
}