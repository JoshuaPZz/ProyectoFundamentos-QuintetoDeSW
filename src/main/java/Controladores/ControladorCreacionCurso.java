package Controladores;
import Entidades.Materia;
import RepositorioBD.MateriaRepositorio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class ControladorCreacionCurso {

    MateriaRepositorio materiaRepositorio;

    public ControladorCreacionCurso(MateriaRepositorio materiaRepositorio) {
        this.materiaRepositorio = materiaRepositorio;
    }

    @FXML
    private Button btnCrearClase;

    @FXML
    private TextField cuposLabel;

    @FXML
    private TextField finLabel;

    @FXML
    private TextField inicioLabel;

    @FXML
    private ChoiceBox<Materia> materiasChoice;

    @FXML
    void crearClasePressed(ActionEvent event) {

    }

    @FXML
    public void initialize() {
        try {
            this.materiasChoice.getItems().setAll(materiaRepositorio.obtenerMaterias());

            materiasChoice.setConverter(new StringConverter<Materia>() {
                @Override
                public String toString(Materia object) {
                    try {
                        String nombre = object.getNombre();
                        System.out.println("Converting Materia: " + nombre);
                        return nombre;
                    } catch (Exception e) {
                        System.out.println("Error in toString conversion: " + e.getMessage());
                        e.printStackTrace();
                        return "Error";
                    }
                }

                @Override
                public Materia fromString(String s) {
                    return null;
                }
            });

            // Select first item by default (optional)
            if (!materiasChoice.getItems().isEmpty()) {
                materiasChoice.setValue(materiasChoice.getItems().get(0));
            }

        } catch (Exception e) {
            System.out.println("Error in initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }
}