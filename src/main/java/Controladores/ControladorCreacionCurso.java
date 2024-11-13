package Controladores;
import Entidades.Horario;
import Entidades.HorarioDisponible;
import Entidades.Materia;
import Entidades.Sala;
import RepositorioBD.CursoRepositorio;
import RepositorioBD.MateriaRepositorio;
import RepositorioBD.SalaRepositorio;
import Servicios.ServicioCurso;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

public class ControladorCreacionCurso {

    MateriaRepositorio materiaRepositorio;
    SalaRepositorio salaRepositorio;
    CursoRepositorio cursoRepositorio;
    ServicioCurso servicioCurso;

    public ControladorCreacionCurso(MateriaRepositorio materiaRepositorio, SalaRepositorio salaRepositorio, CursoRepositorio cursoRepositorio, ServicioCurso servicioCurso) {
        this.materiaRepositorio = materiaRepositorio;
        this.salaRepositorio = salaRepositorio;
        this.cursoRepositorio = cursoRepositorio;
        this.servicioCurso = servicioCurso;
    }

    @FXML
    private Button btnCrearClase;

    @FXML
    private TextField cuposLabel;

    @FXML
    private ChoiceBox<HorarioDisponible> horariosChoice;


    @FXML
    private ChoiceBox<Materia> materiasChoice;

    @FXML
    private ChoiceBox<Sala> salasChoice;

    @FXML
    void crearClasePressed(ActionEvent event) {
        //servicioCurso.crearCurso(materiasChoice.getSelectionModel().getSelectedItem(), Integer.parseInt(cuposLabel), )
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
            List<Sala> salas = salaRepositorio.obtenerSalas();
            cuposLabel.textProperty().addListener((obs, oldValue, newValue) -> {
                try {
                    int cupos = Integer.parseInt(newValue);
                    salasChoice.getItems().setAll(
                            salas.stream()
                                    .filter(s -> s.getCapacidad() >= cupos)
                                    .toList()
                    );
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            });

            salasChoice.setConverter(new StringConverter<>() {

                @Override
                public String toString(Sala sala) {
                    try {
                        String nombre = sala.getUbicacion();
                        System.out.println("Converting Materia: " + nombre);
                        return nombre;
                    } catch (Exception e) {
                        System.out.println("Error in toString conversion: " + e.getMessage());
                        e.printStackTrace();
                        return "";
                    }
                }

                @Override
                public Sala fromString(String s) {
                    return null;
                }
            });

            salasChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {

                try{
                    List<HorarioDisponible> horarios = cursoRepositorio.obtenerHorariosDisponiblesPorSala(Integer.parseInt(salasChoice.getSelectionModel().getSelectedItem().getiD()));
                    horariosChoice.getItems().setAll(horarios);
                }catch (Exception e) {
                    e.printStackTrace();
                }

            });
            salasChoice.setConverter(new StringConverter<>() {

                @Override
                public String toString(Sala sala) {
                    try {
                        String nombre = sala.getUbicacion();
                        System.out.println("Converting Materia: " + nombre);
                        return nombre;
                    } catch (Exception e) {
                        System.out.println("Error in toString conversion: " + e.getMessage());
                        e.printStackTrace();
                        return "";
                    }
                }

                @Override
                public Sala fromString(String s) {
                    return null;
                }
            });
            horariosChoice.setConverter(new StringConverter<>() {

                @Override
                public String toString(HorarioDisponible horario) {
                    try {
                        String nombre = "Hora inicio: " + horario.getHoraInicio() + " Hora fin: " + horario.getHoraFin();
                        System.out.println("Converting horario: " + nombre);
                        return nombre;
                    } catch (Exception e) {
                        System.out.println("Error in toString conversion: " + e.getMessage());
                        e.printStackTrace();
                        return "";
                    }
                }

                @Override
                public HorarioDisponible fromString(String s) {
                    return null;
                }
            });

        } catch (Exception e) {
            System.out.println("Error in initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }
}