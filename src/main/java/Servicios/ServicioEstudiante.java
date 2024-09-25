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

    // Metodo para inscribir ya una materia del carrito al horario
    public boolean inscribirCurso(Estudiante estudiante, Curso cursoAInscribir) {
        if (estudiante != null && estudiante.getCarrito() != null) {
            List<Curso> carrito = estudiante.getCarrito();
            int totalCreditos = 0;

            // Calcular los créditos de los cursos ya inscritos
            for (Curso cursoInscrito : estudiante.getCursos()) {
                totalCreditos += cursoInscrito.getMateria().getCreditos();
            }

            // Verificar si el curso está en el carrito
            if (!carrito.contains(cursoAInscribir)) {
                System.out.println("El curso no está en el carrito.");
                return false;
            }

            // Verificar si el curso ya fue visto por el estudiante
            for (Curso cursoVisto : estudiante.getCursosVistos()) {
                if (cursoVisto.getMateria().getiD().equals(cursoAInscribir.getMateria().getiD())) {
                    System.out.println("El estudiante ya ha cursado la materia " + cursoAInscribir.getMateria().getNombre() + " anteriormente.");
                    return false;
                }
            }

            // Verificar que no exceda los créditos máximos permitidos
            int creditosCurso = cursoAInscribir.getMateria().getCreditos();
            if (totalCreditos + creditosCurso > estudiante.getCreditosmax()) {
                System.out.println("El curso " + cursoAInscribir.getiD() + " excede el límite de créditos permitidos.");
                return false;
            }

            if (!servicioCurso.cumpleRequisitos(estudiante, cursoAInscribir)) {
                System.out.println("No cumple con los pre-requisitos o co-requisitos del curso: " + cursoAInscribir.getiD());
                return false;
            } // Verificar pre-requisitos y co-requisitos

            // Verificar que no haya cruce de horarios
            if (servicioCurso.hayCruceHorarios(cursoAInscribir, estudiante.getCursos())) {
                System.out.println("El curso " + cursoAInscribir.getiD() + " tiene cruce de horarios.");
                return false;
            }

            // Verificar la capacidad del curso
            if (cursoAInscribir.getEstudiantes().size() < cursoAInscribir.getCapacidad()) {
                // Inscribir al estudiante en el curso
                cursoAInscribir.getEstudiantes().add(estudiante);
                estudiante.getCursos().add(cursoAInscribir);
                carrito.remove(cursoAInscribir); // Remover el curso del carrito
                System.out.println("Inscripción exitosa del curso: " + cursoAInscribir.getiD());
                return true;
            } else {
                System.out.println("No hay cupo en el curso: " + cursoAInscribir.getiD());
                return false;
            }
        }
        return false;
    }


}
