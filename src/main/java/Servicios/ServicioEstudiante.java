package Servicios;
import Entidades.Curso;
import Entidades.Estudiante;
import Entidades.Materia;
import RepositorioBD.EstudianteRepositorio;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServicioEstudiante {

    private ServicioCurso servicioCurso;
    private EstudianteRepositorio estudianteRepositorio;
    public ServicioEstudiante(ServicioCurso servicioCurso, EstudianteRepositorio estudianteRepositorio) {
        this.servicioCurso = servicioCurso;
        this.estudianteRepositorio = estudianteRepositorio;

    }

    //Metodo para agregar un curso al carrito del estudiante
    public boolean agregarCursoAlCarrito(Estudiante estudiante, Curso curso) throws SQLException {
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
            estudianteRepositorio.agregarAlCarrito(estudiante.getId(), Integer.parseInt(curso.getiD()));
            carrito.add(curso);
            return true;
        }
        return false;
    }

    public List<Curso> obtenerCarrito(int estudianteId) throws SQLException {
        return estudianteRepositorio.obtenerCarrito(estudianteId);
    }

    public void agregarAlCarrito(int estudianteId, int cursoId) throws SQLException {
        estudianteRepositorio.agregarAlCarrito(estudianteId, cursoId);
    }

    //Metodo para ver las materias que se encuentran actualmente en el carrito
    /*
    public ArrayList<Curso> verCarrito(Estudiante estudiante) {
        if (estudiante != null && estudiante.getCarrito() != null) {
            ArrayList<Curso> cursos = new ArrayList<>();
            for (Curso curso : estudiante.getCarrito()) {
                cursos.add(curso);
            }
            return cursos;
        }
        return null;
    }

     */
    public ArrayList<String> verCarritoToString(Estudiante estudiante) {
        if (estudiante != null && estudiante.getCarrito() != null) {
            ArrayList<String> materias = new ArrayList<>();
            for (Curso curso : estudiante.getCarrito()) {
                materias.add(curso.toString());
            }
            return materias;
        }
        return null;
    }

    //Método para remover a un estudiante de un curso
    public boolean removerCurso(Estudiante estudiante, Curso curso) throws SQLException {
        if (estudiante != null && curso != null && curso.getEstudiantes().contains(estudiante)) {
        estudianteRepositorio.eliminarInscripcion(estudiante.getId(), curso.getiD());
            curso.getEstudiantes().remove(estudiante);
            estudiante.getCursos().remove(curso);
            return true;
        }
        return false;
    }

    //Metodo para obtener los cursos de un estudiante
    public List<Curso> obtenerCursosEstudiante(Estudiante estudiante) {
        if (estudiante != null) {
            try {
                return estudianteRepositorio.obtenerCursosPorEstudiante(estudiante.getId());
            } catch (SQLException e) {
                System.out.println("Error al obtener los cursos del estudiante: " + e.getMessage());
            }
        }
        return Collections.emptyList();
    }

    //Metodo para inscribir un curso
    public boolean inscribirCurso(Estudiante estudiante, Curso cursoAInscribir) {
        if (estudiante != null && estudiante.getCarrito() != null) {
            List<Curso> carrito = estudiante.getCarrito();
            int totalCreditos = 0;

            for (Curso cursoInscrito : estudiante.getCursos()) {
                totalCreditos += cursoInscrito.getMateria().getCreditos();
            }

            //Verificar si el curso está en el carrito
            if (!carrito.contains(cursoAInscribir)) {
                System.out.println("El curso no está en el carrito.");
                return false;
            }

            //Verificar si el curso ya fue visto por el estudiante
            for (Curso cursoVisto : estudiante.getCursosVistos()) {
                if (cursoVisto.getMateria().getiD().equals(cursoAInscribir.getMateria().getiD())) {
                    System.out.println("El estudiante ya ha cursado la materia " + cursoAInscribir.getMateria().getNombre() + " anteriormente.");
                    return false;
                }
            }

            //Verificar que no exceda los créditos máximos permitidos
            int creditosCurso = cursoAInscribir.getMateria().getCreditos();
            if (totalCreditos + creditosCurso > estudiante.getCreditosmax()) {
                System.out.println("El curso " + cursoAInscribir.getiD() + " excede el límite de créditos permitidos.");
                return false;
            }
            //Verificar pre-requisitos y co-requisitos
            if (!servicioCurso.cumpleRequisitos(estudiante, cursoAInscribir)) {
                System.out.println("No cumple con los pre-requisitos o co-requisitos del curso: " + cursoAInscribir.getiD());
                return false;
            }


            //Verificar que no haya cruce de horarios


            if (servicioCurso.hayCruceHorarios(cursoAInscribir, estudiante)) {
                System.out.println("El curso " + cursoAInscribir.getiD() + " tiene cruce de horarios.");
                return false;
            }

            //Verificar la capacidad del curso
            if (cursoAInscribir.getEstudiantes().size() < cursoAInscribir.getCapacidad()) {
                try {
                    estudianteRepositorio.inscribirCurso(estudiante.getId(), cursoAInscribir.getiD());
                    cursoAInscribir.getEstudiantes().add(estudiante);
                    estudiante.getCursos().add(cursoAInscribir);
                    carrito.remove(cursoAInscribir);

                    System.out.println("Inscripción exitosa del curso: " + cursoAInscribir.getiD());
                    return true;
                } catch (Exception e) {
                    System.out.println("Error al inscribir el curso en la base de datos: " + e.getMessage());
                    return false;
                }
            } else {
                System.out.println("No hay cupo en el curso: " + cursoAInscribir.getiD());
                return false;
            }
        }
        return false;
    }
}
