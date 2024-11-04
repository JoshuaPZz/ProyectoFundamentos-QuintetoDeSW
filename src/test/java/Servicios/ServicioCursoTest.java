package Servicios;

import Entidades.*;
import RepositorioBD.CursoRepositorio;
import RepositorioBD.EstudianteRepositorio;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class ServicioCursoTest {
    private ServicioCurso servicioCurso;
    private CursoRepositorio cursoRepositorio;
    private EstudianteRepositorio estudianteRepositorio;
    private Curso curso;
    private Estudiante estudiante;

    @BeforeEach
    public void setUp() throws SQLException {
        cursoRepositorio = new CursoRepositorio();
        estudianteRepositorio = new EstudianteRepositorio();
        servicioCurso = new ServicioCurso(cursoRepositorio, estudianteRepositorio);

        // Configurar datos de prueba
        Materia materia = new Materia();
        materia.setiD("MAT101");
        materia.setCreditos(3);

        List<Horario> horarios = new ArrayList<>();
        String dia = "Lunes"; // Definir el día de la semana
        Date horaInicio = ServicioCurso.crearHorario(2024, Calendar.JANUARY, 1, 9);
        Date horaFin = ServicioCurso.crearHorario(2024, Calendar.JANUARY, 1, 11);
        horarios.add(new Horario(dia, horaInicio, horaFin));

        List<Sala> salas = new ArrayList<>();
        Sala sala = new Sala();
        sala.setiD("101");
        sala.setCapacidad(30);
        salas.add(sala);

        List<Profesor> profesores = new ArrayList<>();
        Profesor profesor = new Profesor();
        profesor.setId(1);
        profesor.setNombre("Juan Perez");
        profesores.add(profesor);

        curso = new Curso();
        curso.setMateria(materia);
        curso.setCapacidad(30);
        curso.setHorarios(horarios);
        curso.setSalas(salas);
        curso.setProfesores(profesores);
        curso.setEstudiantes(new ArrayList<>());
        curso.setiD("CURSO101");

        estudiante = new Estudiante();
        estudiante.setId(1);
        estudiante.setNombre("Test Student");
        estudiante.setCursos(new ArrayList<>());
        estudiante.setCursosVistos(new ArrayList<>());

        cursoRepositorio.crearCurso(curso);
    }

    @Test
    public void testCrearCursoExitoso() {
        Materia materia = new Materia();
        materia.setiD("MAT102");
        List<Horario> horarios = new ArrayList<>();
        String dia = "Lunes"; // Definir el día de la semana
        Date horaInicio = ServicioCurso.crearHorario(2024, Calendar.JANUARY, 1, 9);
        Date horaFin = ServicioCurso.crearHorario(2024, Calendar.JANUARY, 1, 11);
        horarios.add(new Horario(dia, horaInicio, horaFin));
        List<Sala> salas = new ArrayList<>();
        List<Profesor> profesores = new ArrayList<>();

        Curso nuevoCurso = servicioCurso.crearCurso(materia, 25, horarios, salas, 25, profesores);

        assertNotNull(nuevoCurso, "El curso creado no debería ser null");
        assertEquals(25, nuevoCurso.getCapacidad(), "La capacidad del curso no coincide");
        assertEquals(materia, nuevoCurso.getMateria(), "La materia del curso no coincide");
    }

    @Test
    public void testConsultarCursoExistente() throws SQLException {
        cursoRepositorio.crearCurso(curso);
        Curso cursoConsultado = servicioCurso.consultarCurso(curso);

        assertNotNull(cursoConsultado, "El curso consultado no debería ser null");
        assertEquals(Optional.of("El ID del curso consultado no coincide"), curso.getiD(), cursoConsultado.getiD());
    }

    @Test
    public void testConsultarCursoInexistente() {
        Curso cursoInexistente = new Curso();
        cursoInexistente.setiD("NOID");

        Curso resultado = servicioCurso.consultarCurso(cursoInexistente);
        assertNull(resultado, "El resultado debería ser null para un curso inexistente");
    }

    @Test
    public void testVerEstudiantesCursoVacio() throws SQLException {
        cursoRepositorio.crearCurso(curso);
        List<Estudiante> estudiantes = servicioCurso.verEstudiantes(curso);

        assertNotNull(estudiantes, "La lista de estudiantes no debería ser null");
        assertTrue(estudiantes.isEmpty(), "La lista de estudiantes debería estar vacía");
    }

    @Test
    public void testVerEstudiantesCursoConEstudiantes() throws SQLException {
        cursoRepositorio.crearCurso(curso);
        estudianteRepositorio.inscribirCurso(estudiante.getId(), curso.getiD());

        List<Estudiante> estudiantes = servicioCurso.verEstudiantes(curso);

        assertNotNull(estudiantes, "La lista de estudiantes no debería ser null");
        assertFalse(estudiantes.isEmpty(), "La lista de estudiantes no debería estar vacía");
        assertEquals(1, estudiantes.size(), "Debería haber un estudiante en la lista");
    }

    @Test
    public void testVerProfesorExistente() throws SQLException {
        cursoRepositorio.crearCurso(curso);
        Profesor profesor = servicioCurso.verProfesor(curso);

        assertNotNull(profesor, "El profesor no debería ser null");
        assertEquals("P001", profesor.getId(), "El ID del profesor no coincide");
    }

    @Test
    public void testVerProfesorCursoInexistente() {
        Curso cursoSinProfesor = new Curso();
        cursoSinProfesor.setiD("NOPROF");

        Profesor profesor = servicioCurso.verProfesor(cursoSinProfesor);
        assertNull(profesor, "El profesor debería ser null para un curso inexistente");
    }

    @Test
    public void testHayCruceHorariosConCruce() throws SQLException {
        cursoRepositorio.crearCurso(curso);
        estudianteRepositorio.inscribirCurso(estudiante.getId(), curso.getiD());

        // Crear otro curso con el mismo horario
        Curso curso2 = new Curso();
        Materia materia2 = new Materia();
        materia2.setiD("MAT103");
        curso2.setMateria(materia2);
        curso2.setHorarios(curso.getHorarios());
        curso2.setiD("CURSO102");
        cursoRepositorio.crearCurso(curso2);

        boolean hayCruce = servicioCurso.hayCruceHorarios(curso2, estudiante);
        assertTrue(hayCruce, "Debería detectar el cruce de horarios");
    }

    @Test
    public void testHayCruceHorariosSinCruce() throws SQLException {
        cursoRepositorio.crearCurso(curso);
        estudianteRepositorio.inscribirCurso(estudiante.getId(), curso.getiD());

        // Crear otro curso con horario diferente
        Curso curso2 = new Curso();
        Materia materia2 = new Materia();
        materia2.setiD("MAT103");
        List<Horario> horarios = new ArrayList<>();
        String dia = "Lunes"; // Definir el día de la semana
        Date horaInicio = ServicioCurso.crearHorario(2024, Calendar.JANUARY, 1, 9);
        Date horaFin = ServicioCurso.crearHorario(2024, Calendar.JANUARY, 1, 11);
        horarios.add(new Horario(dia, horaInicio, horaFin));
        curso2.setHorarios(horarios);
        curso2.setMateria(materia2);
        curso2.setiD("CURSO102");
        cursoRepositorio.crearCurso(curso2);

        boolean hayCruce = servicioCurso.hayCruceHorarios(curso2, estudiante);
        assertFalse(hayCruce, "No debería haber cruce de horarios");
    }

    @Test
    public void testCumpleRequisitosConPreRequisitos() throws SQLException {
        // Configurar materia con prerrequisitos
        Materia prerequisito = new Materia();
        prerequisito.setiD("PRE101");

        List<Materia> prerequisitos = new ArrayList<>();
        prerequisitos.add(prerequisito);

        curso.getMateria().setPrerequisitos(prerequisitos);

        // Agregar el prerrequisito a las materias vistas del estudiante
        estudianteRepositorio.obtenerMateriasVistasPorEstudiante(estudiante.getId());

        boolean cumpleRequisitos = servicioCurso.cumpleRequisitos(estudiante, curso);
        assertTrue(cumpleRequisitos, "El estudiante debería cumplir los prerequisitos");
    }

    @Test
    public void testCumpleRequisitosSinPreRequisitos() throws SQLException {
        // Configurar materia con prerrequisitos
        Materia prerequisito = new Materia();
        prerequisito.setiD("PRE101");

        List<Materia> prerequisitos = new ArrayList<>();
        prerequisitos.add(prerequisito);

        curso.getMateria().setPrerequisitos(prerequisitos);

        boolean cumpleRequisitos = servicioCurso.cumpleRequisitos(estudiante, curso);
        assertFalse(cumpleRequisitos, "El estudiante no debería cumplir los prerequisitos");
    }

    @Test
    public void testAsignarSalaACursoExitoso() throws SQLException {
        cursoRepositorio.crearCurso(curso);
        Sala nuevaSala = new Sala();
        nuevaSala.setiD("102");
        nuevaSala.setCapacidad(35);

        boolean resultado = servicioCurso.asignarSalaACurso(curso.getiD(), nuevaSala);
        assertTrue(resultado, "Debería asignar la sala exitosamente");
    }

    @Test
    public void testAsignarSalaACursoRepetida() throws SQLException {
        cursoRepositorio.crearCurso(curso);
        Sala salaExistente = curso.getSalas().get(0);

        boolean resultado = servicioCurso.asignarSalaACurso(curso.getiD(), salaExistente);
        assertFalse(resultado, "No debería asignar una sala que ya está asignada");
    }
}