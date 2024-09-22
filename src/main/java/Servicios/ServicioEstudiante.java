package Servicios;
import Entidades.Estudiante;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class ServicioEstudiante {
    private ServicioCurso servicioCurso;
    private List<Estudiante> estudiantes;

    public ServicioEstudiante(ServicioCurso servicioCurso) {
        this.servicioCurso = servicioCurso;
        this.estudiantes = new ArrayList<>();
    }
}
