package Controladores;

import Entidades.Curso;
import Entidades.Sesion;
import Servicios.ServicioEstudiante;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class ControladorPantallaVerInformacion {


    @FXML
    private Button botonVerInformacion;

    @FXML
    private ListView<Curso> listViewCursos;

    @FXML
    void botonVerInfoPressed(ActionEvent event) {
    }

    @FXML
    public void initialize() {
    }
}
