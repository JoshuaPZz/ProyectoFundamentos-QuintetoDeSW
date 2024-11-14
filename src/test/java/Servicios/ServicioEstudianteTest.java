package Servicios;

import Entidades.*;
import RepositorioBD.CursoRepositorio;
import RepositorioBD.EstudianteRepositorio;
import RepositorioBD.MateriaRepositorio;
import RepositorioBD.ProfesorRepositorio;
import RepositorioBD.SalaRepositorio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import java.util.*;

public class ServicioEstudianteTest {
    private ServicioEstudiante servicioEstudiante;
    private ServicioCurso servicioCurso;
    private CursoRepositorio cursoRepositorio;
    private ProfesorRepositorio profesorRepositorio;
    private EstudianteRepositorio estudianteRepositorio;
    private MateriaRepositorio materiaRepositorio;
    private SalaRepositorio salaRepositorio;
    private Estudiante estudiante;
    private Curso curso;
    private Materia materia;
    private Horario horario;
    private Sala sala;

    @BeforeEach
    public void setUp() throws SQLException {
        cursoRepositorio = new CursoRepositorio();
        profesorRepositorio = new ProfesorRepositorio();
        estudianteRepositorio = new EstudianteRepositorio();
        materiaRepositorio = new MateriaRepositorio();
        salaRepositorio = new SalaRepositorio();
        servicioCurso = new ServicioCurso(cursoRepositorio, estudianteRepositorio);
        servicioEstudiante = new ServicioEstudiante(servicioCurso, estudianteRepositorio);

        materia = new Materia("Matematicas", "101", "", 3, new ArrayList<>(), new ArrayList<>());
        horario = new Horario("Lunes",
                ServicioCurso.crearHorario(2024, Calendar.JANUARY, 1, 9),
                ServicioCurso.crearHorario(2024, Calendar.JANUARY, 1, 11));
        sala = new Sala("105", "X", 30, "Y");

        salaRepositorio.insertarSala(sala);
        materiaRepositorio.agregarMateria(materia);

        curso = new Curso(materia, 30, horario, sala, 25);
        String nuevoCursoId = cursoRepositorio.crearCurso(curso);
        curso = cursoRepositorio.obtenerCursoPorId(nuevoCursoId);

        estudiante = new Estudiante();
        estudiante.setId(1);
        estudiante.setNombre("Andres");
        estudiante.setCreditosmax(20);
        estudiante.setCursos(new ArrayList<>());
        estudiante.setCarrito(new ArrayList<>());
        estudiante.setCursosVistos(new ArrayList<>());

        // Eliminar los cursos inscritos por ese estudiante
        for(Curso c : estudiante.getCursos()){
            estudianteRepositorio.eliminarInscripcion(estudiante.getId(), c.getiD());
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Borrar todos los datos insertados a la bdd
        if (curso != null && curso.getiD() != null) {
            cursoRepositorio.eliminarCurso(curso.getiD());
        }
        if (materia != null && materia.getiD() != null) {
            materiaRepositorio.eliminarMateria(materia.getiD());
        }
        if (sala != null && sala.getiD() != null) {
            salaRepositorio.eliminarSala(Integer.parseInt(sala.getiD()));
        }
        // Borrar los cursos y el carrito del estudiante
        if (estudiante != null) {
            estudiante.getCursos().clear();
            estudiante.getCarrito().clear();
        }
    }

    @Test
    public void testAgregarCursoAlCarritoExitoso() throws SQLException {
        boolean resultado = servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);
        assertTrue(resultado, "Debería agregar el curso al carrito");
        List<Curso> carrito = servicioEstudiante.obtenerCarrito(estudiante.getId());
        assertEquals(1, carrito.size(), "Debería haber exactamente un curso en el carrito");
        assertEquals(curso.getiD(), carrito.get(0).getiD(), "El curso en el carrito debería ser el mismo que se agregó");
    }

    @Test
    public void testAgregarCursoAlCarritoDuplicado() throws SQLException {
        servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);
        boolean resultado = servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);
        assertFalse(resultado, "No debería agregar un curso duplicado al carrito");
        List<Curso> carrito = servicioEstudiante.obtenerCarrito(estudiante.getId());
        assertEquals(1, carrito.size(), "Debería mantenerse un solo curso en el carrito");
    }

    @Test
    public void testObtenerCarritoVacio() throws SQLException {
        List<Curso> carrito = servicioEstudiante.obtenerCarrito(estudiante.getId());
        assertNotNull(carrito, "El carrito no debería ser null");
        assertTrue(carrito.isEmpty(), "El carrito debería estar vacío inicialmente");
    }

    @Test
    public void testObtenerCarritoConCursos() throws SQLException {
        servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);
        List<Curso> carrito = servicioEstudiante.obtenerCarrito(estudiante.getId());
        assertNotNull(carrito, "El carrito no debería ser null");
        assertFalse(carrito.isEmpty(), "El carrito no debería estar vacío");
        assertEquals(1, carrito.size(), "Debería haber exactamente un curso en el carrito");
        assertEquals(curso.getiD(), carrito.get(0).getiD(), "El ID del curso en el carrito debería coincidir");
    }

    @Test
    public void testInscribirCursoExitoso() throws SQLException {
        servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);
        boolean resultado = servicioEstudiante.inscribirCurso(estudiante, curso);
        assertTrue(resultado, "Debería inscribir el curso exitosamente");
        assertFalse(estudiante.getCarrito().contains(curso), "El curso no debería estar en el carrito después de la inscripción");
        assertTrue(estudiante.getCursos().contains(curso), "El curso debería estar en los cursos inscritos");
        List<Estudiante> estudiantesEnCurso = servicioCurso.verEstudiantes(curso);
        assertTrue(estudiantesEnCurso.stream().anyMatch(e -> e.getId() == estudiante.getId()),
                "El estudiante debería aparecer en la lista de estudiantes del curso");
    }

    @Test
    public void testInscribirCursoExcedeCreditos() throws SQLException {
        estudiante.setCreditosmax(2);
        servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);
        boolean resultado = servicioEstudiante.inscribirCurso(estudiante, curso);
        assertFalse(resultado, "No debería inscribir el curso si excede los créditos máximos");
        assertTrue(estudiante.getCarrito().contains(curso), "El curso debería permanecer en el carrito");
        assertFalse(estudiante.getCursos().contains(curso), "El curso no debería estar en los cursos inscritos");
    }

    @Test
    public void testInscribirCursoCupoLleno() throws SQLException {
        curso.setCapacidad(0);
        servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);
        boolean resultado = servicioEstudiante.inscribirCurso(estudiante, curso);
        assertFalse(resultado, "No debería inscribir el curso si no hay cupo disponible");
        assertTrue(estudiante.getCarrito().contains(curso), "El curso debería permanecer en el carrito");
        assertFalse(estudiante.getCursos().contains(curso), "El curso no debería estar en los cursos inscritos");
    }

    @Test
    public void testInscribirCursoConCruceHorario() throws SQLException {
        // Crear un curso con horario conflictivo
        Curso cursoConflicto = new Curso(materia, 30, horario, sala, 25);
        String cursoConflictoId = cursoRepositorio.crearCurso(cursoConflicto);
        cursoConflicto = cursoRepositorio.obtenerCursoPorId(cursoConflictoId);

        // Inscribir primer curso
        servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);
        servicioEstudiante.inscribirCurso(estudiante, curso);

        // Intentar inscribir curso con conflicto
        servicioEstudiante.agregarCursoAlCarrito(estudiante, cursoConflicto);
        boolean resultado = servicioEstudiante.inscribirCurso(estudiante, cursoConflicto);

        assertFalse(resultado, "No debería inscribir un curso con cruce de horario");
        assertFalse(estudiante.getCursos().contains(cursoConflicto),
                "El curso conflictivo no debería estar en los cursos inscritos");

        // Limpieza
        cursoRepositorio.eliminarCurso(cursoConflictoId);
    }

    @Test
    public void testRemoverCursoExitoso() throws SQLException {
        servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);
        servicioEstudiante.inscribirCurso(estudiante, curso);
        boolean resultado = servicioEstudiante.removerCurso(estudiante, curso);
        assertTrue(resultado, "Debería poder remover el curso exitosamente");
        assertFalse(estudiante.getCursos().contains(curso),
                "El curso debería ser removido de los cursos inscritos");
        List<Estudiante> estudiantesEnCurso = servicioCurso.verEstudiantes(curso);
        assertFalse(estudiantesEnCurso.stream().anyMatch(e -> e.getId() == estudiante.getId()),
                "El estudiante no debería aparecer en la lista de estudiantes del curso");
    }

    @Test
    public void testRemoverCursoNoInscrito() throws SQLException {
        boolean resultado = servicioEstudiante.removerCurso(estudiante, curso);
        assertFalse(resultado, "No debería remover un curso que no está inscrito");
    }
}
