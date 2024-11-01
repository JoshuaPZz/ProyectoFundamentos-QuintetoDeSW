package Servicios;

import Entidades.*;
import RepositorioBD.CursoRepositorio;
import RepositorioBD.EstudianteRepositorio;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ServicioCurso {
    private final CursoRepositorio cursoRepositorio;
    private final EstudianteRepositorio estudianteRepositorio;

    public ServicioCurso(CursoRepositorio cursoRepositorio, EstudianteRepositorio estudianteRepositorio) {
        this.cursoRepositorio = cursoRepositorio;
        this.estudianteRepositorio = estudianteRepositorio;
    }

    // Método para crear un curso
    public Curso crearCurso(Materia materia, int capacidad, List<Horario> horarios, List<Sala> salas, int cupos, List<Profesor> profesores) {
        Curso nuevoCurso = new Curso(materia, capacidad, horarios, salas, cupos);
        nuevoCurso.setProfesores(profesores); // Asigna la lista de profesores al nuevo curso
        // Llamada al repositorio para almacenar el curso en la base de datos
        try {
            cursoRepositorio.crearCurso(nuevoCurso);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al guardar el curso en la base de datos.");
        }
        return nuevoCurso;
    }


    //Método para consultar un curso
    public Curso consultarCurso(Curso curso) {
        if (curso != null) {
            String id = curso.getiD();
            try {
                return cursoRepositorio.obtenerCursoPorId(id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;

    }

    //Ver la lista de estudiantes de un curso usando RepositorioCurso
    public List<Estudiante> verEstudiantes(Curso curso) {
        if (curso != null) {
            try {
                return cursoRepositorio.obtenerEstudiantes(curso.getiD());
            } catch (SQLException e) {
                System.out.println("Error al obtener los estudiantes del curso: " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    //Ver el profesor principal de un curso usando RepositorioCurso
    public Profesor verProfesor(Curso curso) {
        if (curso != null) {
            try {
                List<Profesor> profesores = cursoRepositorio.obtenerProfesores(curso.getiD());
                if (!profesores.isEmpty()) {
                    return profesores.get(0);
                }
            } catch (SQLException e) {
                System.out.println("Error al obtener profesores del curso: " + e.getMessage());
            }
        }
        return null;
    }

    //Buscar un curso por su ID usando RepositorioCurso
    public Curso buscarCursoPorID(String idCurso) {
        try {
            return cursoRepositorio.obtenerCursoPorId(idCurso);
        } catch (SQLException e) {
            System.out.println("Error al buscar el curso con ID " + idCurso + ": " + e.getMessage());
            return null;
        }
    }


    //Crear los horarios del curso
    public static Date crearHorario(int year, int month, int day, int hourOfDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2024);          // Establecer el año
        calendar.set(Calendar.MONTH, month);        // Establecer el mes (0=enero, 1=febrero, ..., 11=diciembre)
        calendar.set(Calendar.DAY_OF_MONTH, day);   // Establecer el día del mes
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay); // Establecer la hora (formato 24 horas)
        calendar.set(Calendar.SECOND, 0);           // Establecer los segundos en 0
        calendar.set(Calendar.MILLISECOND, 0);      // Establecer los milisegundos en 0
        return calendar.getTime();
    }

    //Verificar cruce de horarios entre los cursos
    public boolean hayCruceHorarios(Curso curso, Estudiante estudiante) {
        if (curso != null) {
            String id = curso.getiD();
            int idEstudiante = estudiante.getId();
            try {
                boolean cruce = cursoRepositorio.hayCruceHorarios(id, idEstudiante);
                if (cruce == true) {
                    return true;
                }
            } catch (SQLException e) {
                System.out.println("Error al hacer la busqueda" + e.getMessage());
                return false;
            }
        }
        return false;
    }

    //Verificación de cumplimiento de requisitos pre y co
    public boolean cumpleRequisitos(Estudiante estudiante, Curso curso) {
        try {
            List<Materia> materiasVistas = estudianteRepositorio.obtenerMateriasVistasPorEstudiante(estudiante.getId());
            List<Materia> preRequisitos = curso.getMateria().getPrerequisitos();
            for (Materia preRequisito : preRequisitos) {
                if (!materiasVistas.contains(preRequisito)) {
                    System.out.println("Falta el pre-requisito: " + preRequisito.getNombre());
                    return false;
                }
            }

            List<Materia> coRequisitos = curso.getMateria().getCorequisitos();
            for (Materia coRequisito : coRequisitos) {
                boolean coRequisitoCumplido = false;
                List<Curso> cursosInscritos = estudianteRepositorio.obtenerCursosInscritos(estudiante.getId());
                for (Curso cursoInscrito : cursosInscritos) {
                    if (cursoInscrito.getMateria().equals(coRequisito)) {
                        coRequisitoCumplido = true;
                        break;
                    }
                }

                // Si no está inscrito en la materia, verificar si ya la ha visto
                if (!coRequisitoCumplido) {
                    for (Materia materiaVista : materiasVistas) {
                        if (materiaVista.equals(coRequisito)) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> obtenerCursosPorMateria(String materiaId) throws SQLException {
        return cursoRepositorio.obtenerCursosPorMateria(materiaId);
    }
}