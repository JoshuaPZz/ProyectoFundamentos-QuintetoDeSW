package Servicios;
import Entidades.Curso;
import Entidades.Estudiante;
import Entidades.Materia;

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

    // Agregar un curso dado al carrito del estudiante
    public boolean agregarCursoAlCarrito(Estudiante estudiante, Curso curso) {
        if (estudiante != null && curso != null) {
            List<Curso> cursosInscritos = estudiante.getCursos();
            List<Curso> carrito = estudiante.getCarrito();

            if (cursosInscritos.contains(curso)) {
                System.out.println("El curso ya está inscrito.");
                return false;
            }

            if (carrito.contains(curso)) {
                System.out.println("El curso ya está en el carrito.");
                return false;
            }

            carrito.add(curso);
            System.out.println("Curso agregado al carrito: " + curso.getiD());
            return true;
        }
        return false;
    }

    // Ver las materias que se encuetran actualmente en el carrito
    public List<Materia> verCarrito(Estudiante estudiante) {
        if (estudiante != null && estudiante.getCarrito() != null) {
            List<Materia> materias = new ArrayList<>();
            for (Curso curso : estudiante.getCarrito()) {
                System.out.println(curso.getMateria().getNombre() + " " + curso.getiD()); // Mostrar lista de materias en carrito
                materias.add(curso.getMateria());
            }
            return materias;
        }
        return null;
    }

    // Método para remover a un estudiante de un curso
    public boolean removerCurso(Estudiante estudiante, Curso curso) {
        if (estudiante != null && curso != null && curso.getEstudiantes().contains(estudiante)) {
            curso.getEstudiantes().remove(estudiante);
            estudiante.getCursos().remove(curso);
            return true;
        }
        return false;
    }

    // Metodo para ver el horario ya inscrito del estudiante
    public List<String> verHorario(Estudiante estudiante) {
        if (estudiante != null && estudiante.getCursos() != null) {
            List<String> detallesMaterias = new ArrayList<>();
            for (Curso curso : estudiante.getCursos()) {
                String materiaNombre = curso.getMateria().getNombre();  // Obtenemos el nombre de la materia
                List<Date> horarios = curso.getHorarios();  // Obtenemos la lista de horarios
                StringBuilder detallesCurso = new StringBuilder(materiaNombre + " - Horarios: ");
                for (Date horario : horarios) {
                    detallesCurso.append(horario.toString()).append(" ");  // Agregamos cada horario a la cadena
                }
                detallesMaterias.add(detallesCurso.toString());  // Añadimos la información del curso a la lista
            }
            return detallesMaterias;
        }
        return null;
    }




}
