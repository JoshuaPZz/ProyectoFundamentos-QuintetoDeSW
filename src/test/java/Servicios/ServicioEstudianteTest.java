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
        materiaRepositorio = new MateriaRepositorio();
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

        // Configurar y GUARDAR la materia primero
        materia = new Materia();
        materia.setiD("101");
        materia.setCreditos(3);
        materia.setNombre("Matemáticas");
        // Añade aquí la línea para guardar la materia en la base de datos
        materiaRepositorio.agregarMateria(materia); // Necesitarás crear esta clase/método

        // Luego configura el curso
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

        // Ahora sí, guarda el curso
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
