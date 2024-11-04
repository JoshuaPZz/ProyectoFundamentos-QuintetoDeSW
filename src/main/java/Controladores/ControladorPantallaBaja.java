package Controladores;

import Entidades.Curso;
import Entidades.Sesion;
import Servicios.ServicioEstudiante;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class ControladorPantallaBaja {

    private ServicioEstudiante servicioEstudiante;

   public  ControladorPantallaBaja(ServicioEstudiante servicioEstudiante) {
        this.servicioEstudiante = servicioEstudiante;
    }

    @FXML
    private Button botonDarBaja;

    @FXML
    private ListView<Curso> listViewCursosBaja;

    @FXML
    void botonBajaPressed(ActionEvent event) {
        try {
            if(!servicioEstudiante.removerCurso(Sesion.getInstancia().getEstudiante(), this.listViewCursosBaja.getSelectionModel().getSelectedItem()))
                System.out.println("paila");

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        this.listViewCursosBaja.setItems(Sesion.getInstancia().getEstudiante().getCursosObservable());
    }

}
