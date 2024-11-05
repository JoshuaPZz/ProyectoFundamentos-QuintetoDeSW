package Controladores;

import Entidades.Curso;
import Entidades.Sesion;
import Servicios.ServicioCurso;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class ControladorPantallaVerInformacion {

    ServicioCurso servicioCurso;

    public ControladorPantallaVerInformacion(ServicioCurso servicioCurso) {
        this.servicioCurso = servicioCurso;
    }


    @FXML
    private Button botonVerInformacion;

    @FXML
    private ListView<Curso> listViewCursos;

    @FXML
    void botonVerInfoPressed(ActionEvent event) {

        try {
            Stage stageInfo = SceneManager.getInstance().openNewWindow("/Pantallas/pantallaInformacionCompleta.fxml", "/CssStyle/LoginStyle.css", "Revisar informacion", true);
            stageInfo.show();
            stageInfo.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ControladorPantallaInformacionCompleta controladorPantallaInformacionCompleta = (ControladorPantallaInformacionCompleta) SceneManager.getInstance().getControllers().get("/Pantallas/pantallaInformacionCompleta.fxml");

        controladorPantallaInformacionCompleta.getLabelInformacionCompleta().setText(servicioCurso.obtenerInformacionCompleta(this.listViewCursos.getSelectionModel().getSelectedItem().getiD()));
    }



    @FXML
    public void initialize() {

        listViewCursos.setItems(Sesion.getInstancia().getEstudiante().getCursosObservable());

    }
}
