package Servicios;

import Entidades.*;
import RepositorioBD.CursoRepositorio;
import RepositorioBD.EstudianteRepositorio;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import java.util.*;

public class ServicioEstudianteTest {
    private ServicioEstudiante servicioEstudiante;
    private ServicioCurso servicioCurso;
    private EstudianteRepositorio estudianteRepositorio;
    private CursoRepositorio cursoRepositorio;
    private Estudiante estudiante;
    private Curso curso;

    @Before
    public void setUp() throws SQLException {
        cursoRepositorio = new CursoRepositorio();
        estudianteRepositorio = new EstudianteRepositorio();
        servicioCurso = new ServicioCurso(cursoRepositorio, estudianteRepositorio);
        servicioEstudiante = new ServicioEstudiante(servicioCurso, estudianteRepositorio);

        // Configurar estudiante de prueba
        estudiante = new Estudiante();
        estudiante.setId(1);
        estudiante.setNombre("Test Student");
        estudiante.setCreditosmax(20);
        estudiante.setCursos(new ArrayList<>());
        estudiante.setCarrito(new ArrayList<>());
        estudiante.setCursosVistos(new ArrayList<>());

        // Configurar curso de prueba
        Materia materia = new Materia();
        materia.setiD("MAT101");
        materia.setCreditos(3);
        materia.setNombre("Matemáticas");

        List<Horario> horarios = new ArrayList<>();
        String dia = "Lunes"; // Definir el día de la semana
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

        // Guardar en la base de datos
        cursoRepositorio.crearCurso(curso);
    }

    @Test
    public void testAgregarCursoAlCarritoExitoso() throws SQLException {
        boolean resultado = servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);

        assertTrue(resultado, "Debería agregar el curso al carrito");
        List<Curso> carrito = servicioEstudiante.obtenerCarrito(estudiante.getId());
        assertTrue(carrito.contains(curso), "El curso debería estar en el carrito");
    }

    @Test
    public void testAgregarCursoAlCarritoDuplicado() throws SQLException {
        servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);
        boolean resultado = servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);

        assertFalse(resultado, "No debería agregar un curso duplicado al carrito");
    }

    @Test
    public void testAgregarCursoAlCarritoCursoYaInscrito() throws SQLException {
        estudianteRepositorio.inscribirCurso(estudiante.getId(), curso.getiD());
        boolean resultado = servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);

        assertFalse(resultado, "No debería agregar un curso ya inscrito al carrito");
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
        assertEquals(carrito.size(), 1, "Debería haber un curso en el carrito");
    }
}