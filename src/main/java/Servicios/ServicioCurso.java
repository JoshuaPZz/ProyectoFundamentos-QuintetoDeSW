package Servicios;
import Entidades.*;

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
    public Curso crearCurso(String ID, Materia materia, int capacidad, List<Date> horarios, List<Sala> salas, int cupos, List<Profesor> profesores) {
        Curso nuevoCurso = new Curso(ID, materia, capacidad, horarios, salas, cupos);
        nuevoCurso.setProfesores(profesores);
        cursos.add(nuevoCurso);
        return nuevoCurso;
    }


    // Método para consultar un curso
    public String consultarCurso(Curso curso) {
        if (curso != null) {
            return "Curso ID: " + curso.getiD() + ", Materia: " + curso.getMateria().getNombre() +
                    ", Capacidad: " + curso.getCapacidad() + ", Estudiantes: " + curso.getEstudiantes().size();
        }
        return "Curso no encontrado.";
    }

    // Método para ver los estudiantes de un curso
    public List<Estudiante> verEstudiantes(Curso curso) {
        if (curso != null) {
            return curso.getEstudiantes();
        }
        return new ArrayList<>();
    }

    // Método para ver el profesor principal de un curso
    public Profesor verProfesor(Curso curso) {
        if (curso != null && !curso.getProfesores().isEmpty()) {
            return curso.getProfesores().get(0);  // Suponemos que el primer profesor es el principal
        }
        return null;
    }
    // Buscar curso por ID
    public Curso buscarCursoPorID(String idCurso) {
        for (Curso curso : cursos) {
            if (curso.getiD().equalsIgnoreCase(idCurso)) {
                return curso;
            }
        }
        System.out.println("No se encontró un curso con el ID proporcionado: " + idCurso);
        return null;
    }

    // Crear los horarios del curso
    public static Date crearHorario(int year, int month, int day, int hourOfDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);          // Establecer el año
        calendar.set(Calendar.MONTH, month);        // Establecer el mes (0=enero, 1=febrero, ..., 11=diciembre)
        calendar.set(Calendar.DAY_OF_MONTH, day);   // Establecer el día del mes
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay); // Establecer la hora (formato 24 horas)// Establecer los minutos
        calendar.set(Calendar.SECOND, 0);           // Establecer los segundos en 0
        calendar.set(Calendar.MILLISECOND, 0);      // Establecer los milisegundos en 0
        return calendar.getTime();
    }

    // Verificar cruce de horarios entre los cursos
    public boolean hayCruceHorarios(Curso nuevoCurso, List<Curso> cursosInscritos) {
        for (Curso cursoInscrito : cursosInscritos) {
            for (Date horarioNuevo : nuevoCurso.getHorarios()) {
                for (Date horarioInscrito : cursoInscrito.getHorarios()) {
                    if (horarioNuevo.equals(horarioInscrito)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
