package Servicios;
import Entidades.Curso;
import Entidades.Materia;
import Entidades.Sala;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
public class ServicioCurso {
    private List<Curso> cursos;

    // Constructor
    public ServicioCurso() {
        this.cursos = new ArrayList<>();
    }
    public Curso crearCurso(String ID, Materia materia, int capacidad, List<Date> horarios, List<Sala> salas, int cupos) {
        Curso nuevoCurso = new Curso(ID, materia, capacidad, horarios, salas, cupos);
        cursos.add(nuevoCurso); // Añadimos el curso a la lista
        return nuevoCurso;
    }


}
