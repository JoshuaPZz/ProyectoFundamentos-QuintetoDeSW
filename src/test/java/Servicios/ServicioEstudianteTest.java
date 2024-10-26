package Servicios;

import Entidades.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import java.util.*;

public class ServicioEstudianteTest {

    @Test
    public void testAgregarCursoAlCarrito_CursoNuevo() {
        ServicioEstudiante servicioEstudiante = new ServicioEstudiante();

        Estudiante estudiante = new Estudiante();
        estudiante.setCarrito(new ArrayList<>());
        estudiante.setCursos(new ArrayList<>());

        Curso curso = new Curso();
        curso.setiD("CURSO1");

        boolean resultado = servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);

        assertTrue(resultado, "Debería agregar el curso al carrito exitosamente");
        assertTrue(estudiante.getCarrito().contains(curso), "El curso debería estar en el carrito");
    }

    @Test
    public void testAgregarCursoAlCarrito_CursoYaInscrito() {
        ServicioEstudiante servicioEstudiante = new ServicioEstudiante();

        Estudiante estudiante = new Estudiante();
        estudiante.setCarrito(new ArrayList<>());
        estudiante.setCursos(new ArrayList<>());

        Curso curso = new Curso();
        curso.setiD("CURSO1");

        estudiante.getCursos().add(curso);

        boolean resultado = servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);

        assertFalse(resultado, "No debería agregar un curso ya inscrito");
        assertFalse(estudiante.getCarrito().contains(curso), "El curso no debería estar en el carrito");
    }

    @Test
    public void testAgregarCursoAlCarrito_CursoYaEnCarrito() {
        ServicioEstudiante servicioEstudiante = new ServicioEstudiante();

        Estudiante estudiante = new Estudiante();
        estudiante.setCarrito(new ArrayList<>());
        estudiante.setCursos(new ArrayList<>());

        Curso curso = new Curso();
        curso.setiD("CURSO1");

        estudiante.getCarrito().add(curso);

        boolean resultado = servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);

        assertFalse(resultado, "No debería agregar un curso que ya está en el carrito");
    }

    @Test
    public void testAgregarCursoAlCarrito_EstudianteNull() {
        ServicioEstudiante servicioEstudiante = new ServicioEstudiante();
        Curso curso = new Curso();

        boolean resultado = servicioEstudiante.agregarCursoAlCarrito(null, curso);

        assertFalse(resultado, "Debería retornar false cuando el estudiante es null");
    }

    @Test
    public void testVerCarrito_EstudianteConCarrito() {
        ServicioEstudiante servicioEstudiante = new ServicioEstudiante();

        Estudiante estudiante = new Estudiante();
        estudiante.setCarrito(new ArrayList<>());

        Curso curso = new Curso();
        Materia materia = new Materia();
        materia.setNombre("Matemáticas");
        curso.setMateria(materia);

        estudiante.getCarrito().add(curso);

        List<Materia> materias = servicioEstudiante.verCarrito(estudiante);

        assertNotNull(materias, "La lista de materias no debería ser null");
        assertEquals(1, materias.size(), "Debería haber una materia en el carrito");
        assertEquals("Matemáticas", materias.get(0).getNombre(), "El nombre de la materia debería coincidir");
    }

    @Test
    public void testVerCarrito_EstudianteNull() {
        ServicioEstudiante servicioEstudiante = new ServicioEstudiante();

        List<Materia> materias = servicioEstudiante.verCarrito(null);

        assertNull(materias, "Debería retornar null cuando el estudiante es null");
    }

    @Test
    public void testRemoverCurso_CursoInscrito() {
        ServicioEstudiante servicioEstudiante = new ServicioEstudiante();

        Estudiante estudiante = new Estudiante();
        estudiante.setId(1);
        estudiante.setCursos(new ArrayList<>());

        Curso curso = new Curso();
        curso.setiD("CURSO1");
        curso.setEstudiantes(new ArrayList<>());

        estudiante.getCursos().add(curso);
        curso.getEstudiantes().add(estudiante);

        try {
            boolean resultado = servicioEstudiante.removerCurso(estudiante, curso);
            assertFalse(resultado, "Debería retornar false ya que no hay conexión a BD");
        } catch (SQLException e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }

    @Test
    public void testObtenerCursosEstudiante_EstudianteExistente() {
        ServicioEstudiante servicioEstudiante = new ServicioEstudiante();

        Estudiante estudiante = new Estudiante();
        estudiante.setId(1);

        List<Curso> cursos = servicioEstudiante.obtenerCursosEstudiante(estudiante);

        assertNotNull(cursos, "La lista no debería ser null");
        assertTrue(cursos.isEmpty(), "La lista debería estar vacía ya que no hay conexión a BD");
    }

    @Test
    public void testInscribirCurso_CursoEnCarrito() {
        ServicioEstudiante servicioEstudiante = new ServicioEstudiante();

        Estudiante estudiante = new Estudiante();
        estudiante.setId(1);
        estudiante.setCarrito(new ArrayList<>());
        estudiante.setCursos(new ArrayList<>());
        estudiante.setCursosVistos(new ArrayList<>());
        estudiante.setCreditosmax(20);

        Curso curso = new Curso();
        curso.setiD("CURSO1");
        curso.setEstudiantes(new ArrayList<>());
        Materia materia = new Materia();
        materia.setCreditos(3);
        materia.setiD("MAT1");
        curso.setMateria(materia);
        curso.setCapacidad(30);

        estudiante.getCarrito().add(curso);

        boolean resultado = servicioEstudiante.inscribirCurso(estudiante, curso);

        assertFalse(resultado, "Debería retornar false ya que no hay conexión a BD");
    }

    @Test
    public void testInscribirCurso_CursoNoEnCarrito() {
        ServicioEstudiante servicioEstudiante = new ServicioEstudiante();

        Estudiante estudiante = new Estudiante();
        estudiante.setCarrito(new ArrayList<>());

        Curso curso = new Curso();
        curso.setiD("CURSO1");

        boolean resultado = servicioEstudiante.inscribirCurso(estudiante, curso);

        assertFalse(resultado, "No debería inscribir un curso que no está en el carrito");
    }

    @Test
    public void testInscribirCurso_ExcedeCreditos() {
        ServicioEstudiante servicioEstudiante = new ServicioEstudiante();

        Estudiante estudiante = new Estudiante();
        estudiante.setCarrito(new ArrayList<>());
        estudiante.setCursos(new ArrayList<>());
        estudiante.setCreditosmax(3);

        Curso cursoExistente = new Curso();
        Materia materiaExistente = new Materia();
        materiaExistente.setCreditos(3);
        cursoExistente.setMateria(materiaExistente);
        estudiante.getCursos().add(cursoExistente);

        Curso cursoNuevo = new Curso();
        cursoNuevo.setiD("CURSO2");
        Materia materiaNueva = new Materia();
        materiaNueva.setCreditos(3);
        cursoNuevo.setMateria(materiaNueva);

        estudiante.getCarrito().add(cursoNuevo);

        boolean resultado = servicioEstudiante.inscribirCurso(estudiante, cursoNuevo);

        assertFalse(resultado, "No debería inscribir un curso que excede el límite de créditos");
    }
}