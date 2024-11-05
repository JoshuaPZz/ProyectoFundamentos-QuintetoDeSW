package Servicios;

import Entidades.*;
import RepositorioBD.CursoRepositorio;
import RepositorioBD.EstudianteRepositorio;
import RepositorioBD.MateriaRepositorio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import java.util.*;

public class ServicioEstudianteTest {
    private ServicioEstudiante servicioEstudiante;
    private ServicioCurso servicioCurso;
    private EstudianteRepositorio estudianteRepositorio;
    private MateriaRepositorio materiaRepositorio;
    private CursoRepositorio cursoRepositorio;
    private Estudiante estudiante;
    private Curso curso;
    private Materia materia;

    @BeforeEach
    public void setUp() throws SQLException {
        cursoRepositorio = new CursoRepositorio();
        estudianteRepositorio = new EstudianteRepositorio();
        servicioCurso = new ServicioCurso(cursoRepositorio, estudianteRepositorio);
        servicioEstudiante = new ServicioEstudiante(servicioCurso, estudianteRepositorio);
        materiaRepositorio = new MateriaRepositorio();

        estudiante = new Estudiante();
        estudiante.setId(1);
        estudiante.setNombre("Test Student");
        estudiante.setCreditosmax(20);
        estudiante.setCursos(new ArrayList<>());
        estudiante.setCarrito(new ArrayList<>());
        estudiante.setCursosVistos(new ArrayList<>());


        materia = new Materia();
        materia.setiD("101");
        materia.setCreditos(3);
        materia.setNombre("Matemáticas");

        materiaRepositorio.agregarMateria(materia);


        List<Horario> horarios = new ArrayList<>();
        String dia = "Lunes";
        Date horaInicio = ServicioCurso.crearHorario(2024, Calendar.JANUARY, 1, 9);
        Date horaFin = ServicioCurso.crearHorario(2024, Calendar.JANUARY, 1, 11);
        horarios.add(new Horario(dia, horaInicio, horaFin));

        curso = new Curso();
        curso.setMateria(materia);
        curso.setCapacidad(30);
        curso.setHorarios(horarios);
        curso.setSalas(new ArrayList<>());
        curso.setProfesores(new ArrayList<>());
        curso.setEstudiantes(new ArrayList<>());
        curso.setiD("CURSO101");

        cursoRepositorio.crearCurso(curso);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Limpia los datos de prueba
        cursoRepositorio.eliminarCurso(curso.getiD());
        materiaRepositorio.eliminarMateria(materia.getiD());
    }
    @Test
    public void testAgregarCursoAlCarritoExitoso() throws SQLException {
        boolean resultado = servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);
        assertTrue(resultado, "Debería agregar el curso al carrito");
    }

    @Test
    public void testAgregarCursoAlCarritoDuplicado() throws SQLException {
        servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);
        boolean resultado = servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);
        assertFalse(resultado, "No debería agregar un curso duplicado al carrito");
    }

    @Test
    public void testObtenerCarritoVacio() throws SQLException {
        List<Curso> carrito = servicioEstudiante.obtenerCarrito(estudiante.getId());
        assertNotNull(carrito, "El carrito no debería ser null");
        assertTrue(carrito.isEmpty(), "El carrito debería estar vacío");
    }

    @Test
    public void testObtenerCarritoConCursos() throws SQLException {
        servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);
        List<Curso> carrito = servicioEstudiante.obtenerCarrito(estudiante.getId());
        assertNotNull(carrito, "El carrito no debería ser null");
        assertFalse(carrito.isEmpty(), "El carrito no debería estar vacío");
        assertEquals(1, carrito.size(), "Debería haber un curso en el carrito");
    }

    @Test
    public void testInscribirCursoExitoso() throws SQLException {
        servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);
        boolean resultado = servicioEstudiante.inscribirCurso(estudiante, curso);
        assertTrue(resultado, "Debería inscribir el curso exitosamente");
        assertFalse(estudiante.getCarrito().contains(curso), "El curso no debería estar en el carrito después de la inscripción");
        assertTrue(estudiante.getCursos().contains(curso), "El curso debería estar en los cursos inscritos del estudiante");
    }

    @Test
    public void testInscribirCursoExcedeCreditos() throws SQLException {
        estudiante.setCreditosmax(2); // Limitar créditos para provocar fallo
        servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);
        boolean resultado = servicioEstudiante.inscribirCurso(estudiante, curso);
        assertFalse(resultado, "No debería inscribir el curso si excede los créditos máximos");
    }

    @Test
    public void testInscribirCursoCupoLleno() throws SQLException {
        curso.setCapacidad(0); // Sin cupo disponible
        servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);
        boolean resultado = servicioEstudiante.inscribirCurso(estudiante, curso);
        assertFalse(resultado, "No debería inscribir el curso si no hay cupo disponible");
    }

    @Test
    public void testInscribirCursoConCruceDeHorario() throws SQLException {
        Curso cursoConflicto = new Curso();
        Materia materiaConflicto = new Materia();
        materiaConflicto.setiD("102");
        materiaConflicto.setCreditos(3);
        materiaConflicto.setNombre("Física");

        cursoConflicto.setMateria(materiaConflicto);
        cursoConflicto.setHorarios(curso.getHorarios()); // Mismo horario para provocar conflicto
        cursoConflicto.setCapacidad(30);
        cursoConflicto.setEstudiantes(new ArrayList<>());
        cursoConflicto.setiD("CURSO102");

        cursoRepositorio.crearCurso(cursoConflicto);

        servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);
        servicioEstudiante.inscribirCurso(estudiante, curso); // Inscribir el primer curso

        servicioEstudiante.agregarCursoAlCarrito(estudiante, cursoConflicto); // Intentar inscribir el segundo curso
        boolean resultado = servicioEstudiante.inscribirCurso(estudiante, cursoConflicto);

        assertFalse(resultado, "No debería inscribir un curso si hay cruce de horarios");
    }

    @Test
    public void testRemoverCursoExitoso() throws SQLException {
        servicioEstudiante.agregarCursoAlCarrito(estudiante,curso);
        servicioEstudiante.inscribirCurso(estudiante, curso);
        boolean resultado = servicioEstudiante.removerCurso(estudiante, curso);
        assertTrue(resultado, "Debería poder remover el curso exitosamente");
        assertFalse(estudiante.getCursos().contains(curso), "El curso debería ser removido de los cursos inscritos del estudiante");
    }

    @Test
    public void testRemoverCursoNoInscrito() throws SQLException {
        boolean resultado = servicioEstudiante.removerCurso(estudiante, curso);
        assertFalse(resultado, "No debería remover un curso que no está inscrito");
    }
}
