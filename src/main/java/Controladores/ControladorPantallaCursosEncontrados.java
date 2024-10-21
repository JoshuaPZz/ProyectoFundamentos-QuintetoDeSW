    package Controladores;

    import Entidades.Curso;
    import Entidades.Materia;
    import RepositorioBD.CursoRepositorio;
    import RepositorioBD.MateriaRepositorio;
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

    public class ControladorPantallaCursosEncontrados {
        MateriaRepositorio repositorio = new MateriaRepositorio(); //Se debe hacer la inyeccion desde principal
        CursoRepositorio repositorio2 = new CursoRepositorio();

        @FXML
        private Button botonAgregar;

        @FXML
        private Button botonFinalizar;


        @FXML
        private Label labelDisponibles;
        @FXML
        private TextField textIdAgregar;


        public void setLabelInfo(String disponibles) {
            labelDisponibles.setText(disponibles);
        }
        public Label getLabelInfo(){
            return labelDisponibles;
        }

        @FXML
        public void agregarButtonPressed(ActionEvent actionEvent) throws SQLException {

            Stage mainStage = SceneManager.getInstance().getPrimaryStage();

            Scene inscripcionScene = mainStage.getScene();
            ControladorPantallaInscripcion controller = (ControladorPantallaInscripcion) inscripcionScene.getUserData();

            if (controller != null) {
                //ServicioEstudiante estudiante = new ServicioEstudiante();
                CursoRepositorio repositorio = new CursoRepositorio();
                try{
                    Materia materia = repositorio2.obtenerMateriaporCursoID(textIdAgregar.getText());
                    if (materia != null) {
                        String infoMateria = "Curso: " + textIdAgregar.getText() + " Materia: " + materia.getNombre();
                        controller.setLabelCarrito(infoMateria);
                    }else{
                        controller.setLabelCarrito("Materia no encontrada");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                    controller.setLabelCarrito("Error al encontrar materia");
                }
               // Curso curso = repositorio.obtenerCursoPorId(id);
                //estudiante.agregarCursoAlCarrito(estudiante, )
            }
        }
        @FXML
        public void finalizarButtonPressed(ActionEvent actionEvent) {

        }

    }
