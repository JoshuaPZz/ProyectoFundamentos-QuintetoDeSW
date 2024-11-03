package Controladores;

import Entidades.Estudiante;
import Entidades.Sesion;
import Servicios.ServicioCurso;
import Servicios.ServicioEstudiante;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControladorPantallaCursosEncontrados {

    private ServicioEstudiante servicioEstudiante;
    private ServicioCurso servicioCurso;

    @FXML
    private Button botonAgregar;

    @FXML
    private Button botonFinalizar;


    @FXML
    private Label labelDisponibles;

    @FXML
    private TextField textIdAgregar;



    public ControladorPantallaCursosEncontrados() {
    }
    //INYECICON DEL SERVICIO ESTUDIANTE POR MEDIO DE CONSTRUCTOR
    public ControladorPantallaCursosEncontrados(ServicioEstudiante servicioEstudiante, ServicioCurso servicioCurso) {
        this.servicioEstudiante = servicioEstudiante;
        this.servicioCurso = servicioCurso;
    }

    public void setLabelInfo(String disponibles) {
        this.labelDisponibles.setText(disponibles);
    }
    public Label getLabelInfo(){
        return labelDisponibles;
    }

    @FXML
    public void agregarButtonPressed(ActionEvent actionEvent) throws SQLException {

        this.servicioEstudiante.agregarCursoAlCarrito(Sesion.getInstancia().getEstudiante(), this.servicioCurso.buscarCursoPorID(textIdAgregar.getText()));
        System.out.println(this.servicioEstudiante.obtenerCarrito(Sesion.getInstancia().getEstudiante().getId()).size());

    }
    @FXML
    public void finalizarButtonPressed(ActionEvent actionEvent) {

    }

}