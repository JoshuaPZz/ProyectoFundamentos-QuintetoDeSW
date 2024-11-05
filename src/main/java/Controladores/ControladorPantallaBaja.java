package Controladores;

import Entidades.Curso;
import Entidades.Sesion;
import Servicios.ServicioEstudiante;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;

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
    private ToggleButton btnTextBaja;

    @FXML
    void botonBajaPressed(ActionEvent event) {
        try {
            if(btnTextBaja.isSelected()) {
                if(!servicioEstudiante.removerCurso(Sesion.getInstancia().getEstudiante(), this.listViewCursosBaja.getSelectionModel().getSelectedItem()))
                    System.out.println("paila");
            }
            else {
                if(!servicioEstudiante.removerCursoCarrito(Sesion.getInstancia().getEstudiante(), this.listViewCursosBaja.getSelectionModel().getSelectedItem()))
                    System.out.println("paila");
            }


        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void btnTextBajaPressed(ActionEvent event) {
        if (btnTextBaja.isSelected()) {
            this.listViewCursosBaja.setItems(Sesion.getInstancia().getEstudiante().getCursosObservable());
            this.btnTextBaja.setText("INSCRITAS");
        }else {
            this.listViewCursosBaja.setItems(Sesion.getInstancia().getEstudiante().getCarritosObservable());
            this.btnTextBaja.setText("CARRITO");
        }

    }

    @FXML
    public void initialize() {
        this.listViewCursosBaja.setItems(Sesion.getInstancia().getEstudiante().getCursosObservable());
        btnTextBaja.setSelected(true);
    }


}
