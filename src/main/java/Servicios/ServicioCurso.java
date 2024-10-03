package Servicios;

import Entidades.Curso;
import Entidades.Estudiante;
import Entidades.Materia;
import Entidades.Profesor;
import RepositorioBD.CursoRepositorio;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ServicioCurso {

    // Método para consultar un curso
    public String consultarCurso(Curso curso) {
        if (curso != null) {
            return "Curso ID: " + curso.getiD() + ", Materia: " + curso.getMateria().getNombre() +
                    ", Capacidad: " + curso.getCapacidad() + ", Estudiantes: " + curso.getEstudiantes().size();
        }
        return "Curso no encontrado.";
    }

    // Ver la lista de estudiantes de un curso usando RepositorioCurso
    public List<Estudiante> verEstudiantes(Curso curso, CursoRepositorio repositorioCurso) {
        if (curso != null) {
            try {
                return repositorioCurso.obtenerEstudiantes(curso.getiD());
            } catch (SQLException e) {
                System.out.println("Error al obtener los estudiantes del curso: " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    // Ver el profesor principal de un curso usando RepositorioCurso
    public Profesor verProfesor(Curso curso, CursoRepositorio repositorioCurso) {
        if (curso != null) {
            try {
                List<Profesor> profesores = repositorioCurso.obtenerProfesores(curso.getiD());
                if (!profesores.isEmpty()) {
                    return profesores.get(0);  // Suponemos que el primer profesor es el principal
                }
            } catch (SQLException e) {
                System.out.println("Error al obtener profesores del curso: " + e.getMessage());
            }
        }
        return null;
    }
    // Buscar un curso por su ID usando RepositorioCurso
    public Curso buscarCursoPorID(String idCurso, CursoRepositorio repositorioCurso) {
        try {
            return repositorioCurso.obtenerCursoPorId(idCurso);
        } catch (SQLException e) {
            System.out.println("Error al buscar el curso con ID " + idCurso + ": " + e.getMessage());
            return null;
        }
    }



    // Crear los horarios del curso
    public static Date crearHorario(int year, int month, int day, int hourOfDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);          // Establecer el año
        calendar.set(Calendar.MONTH, month);        // Establecer el mes (0=enero, 1=febrero, ..., 11=diciembre)
        calendar.set(Calendar.DAY_OF_MONTH, day);   // Establecer el día del mes
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay); // Establecer la hora (formato 24 horas)
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

    // Verificación de cumplimiento de requisitos pre y co
    public boolean cumpleRequisitos(Estudiante estudiante, Curso curso) {
        List<Materia> materiasVistas = new ArrayList<>();

        // Obtener las materias ya vistas (de cursos completados)
        for (Curso cursoVisto : estudiante.getCursosVistos()) {
            materiasVistas.add(cursoVisto.getMateria());
        }

        // Verificar los pre-requisitos de la materia asociada al curso
        List<Materia> preRequisitos = curso.getMateria().getPrerequisitos();
        for (Materia preRequisito : preRequisitos) {
            if (!materiasVistas.contains(preRequisito)) {
                System.out.println("Falta el pre-requisito: " + preRequisito.getNombre());
                return false;
            }
        }

        // Verificar los co-requisitos de la materia asociada al curso
        List<Materia> coRequisitos = curso.getMateria().getCorequisitos();
        for (Materia coRequisito : coRequisitos) {
            boolean coRequisitoCumplido = false;
            // Revisar si el estudiante ya está inscrito en algún curso que tenga esta materia como co-requisito
            for (Curso cursoInscrito : estudiante.getCursos()) {
                if (cursoInscrito.getMateria().equals(coRequisito)) {
                    coRequisitoCumplido = true;
                    break;
                }
            }

            // Si no está inscrito en la materia, verificar si ya la ha visto
            if (!coRequisitoCumplido) {
                for (Curso cursoVisto : estudiante.getCursosVistos()) {
                    if (cursoVisto.getMateria().equals(coRequisito)) {
                        coRequisitoCumplido = true;
                        break;
                    }
                }
            }

            // Si no se cumple el co-requisito, mostrar el mensaje y detener la inscripción
            if (!coRequisitoCumplido) {
                System.out.println("Falta el co-requisito: " + coRequisito.getNombre());
                return false;
            }
        }

        // Si se cumplieron todos los requisitos, se puede inscribir al curso
        return true;
    }
}
