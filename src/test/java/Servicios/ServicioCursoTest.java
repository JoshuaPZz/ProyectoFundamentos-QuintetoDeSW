package Servicios;

import Entidades.*;
import RepositorioBD.CursoRepositorio;
import RepositorioBD.EstudianteRepositorio;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.Before;
import java.sql.SQLException;
import static org.junit.Assert.*;

public class ServicioCursoTest {
    private ServicioCurso servicioCurso;
    private CursoRepositorio cursoRepositorio;
    private EstudianteRepositorio estudianteRepositorio;
    private Curso curso;
    private Estudiante estudiante;

    @Before
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

        assertNotNull("El curso creado no debería ser null", nuevoCurso);
        assertEquals("La capacidad del curso no coincide", 25, nuevoCurso.getCapacidad());
        assertEquals("La materia del curso no coincide", materia, nuevoCurso.getMateria());
    }

    @Test
    public void testConsultarCursoExistente() throws SQLException {
        cursoRepositorio.crearCurso(curso);
        Curso cursoConsultado = servicioCurso.consultarCurso(curso);

        assertNotNull("El curso consultado no debería ser null", cursoConsultado);
        assertEquals(Optional.of("El ID del curso consultado no coincide"), curso.getiD(), cursoConsultado.getiD());
    }

    @Test
    public void testConsultarCursoInexistente() {
        Curso cursoInexistente = new Curso();
        cursoInexistente.setiD("NOID");

        Curso resultado = servicioCurso.consultarCurso(cursoInexistente);
        assertNull("El resultado debería ser null para un curso inexistente", resultado);
    }

    @Test
    public void testVerEstudiantesCursoVacio() throws SQLException {
        cursoRepositorio.crearCurso(curso);
        List<Estudiante> estudiantes = servicioCurso.verEstudiantes(curso);

        assertNotNull("La lista de estudiantes no debería ser null", estudiantes);
        assertTrue("La lista de estudiantes debería estar vacía", estudiantes.isEmpty());
    }

    @Test
    public void testVerEstudiantesCursoConEstudiantes() throws SQLException {
        cursoRepositorio.crearCurso(curso);
        estudianteRepositorio.inscribirCurso(estudiante.getId(), curso.getiD());

        List<Estudiante> estudiantes = servicioCurso.verEstudiantes(curso);

        assertNotNull("La lista de estudiantes no debería ser null", estudiantes);
        assertFalse("La lista de estudiantes no debería estar vacía", estudiantes.isEmpty());
        assertEquals("Debería haber un estudiante en la lista", 1, estudiantes.size());
    }

    @Test
    public void testVerProfesorExistente() throws SQLException {
        cursoRepositorio.crearCurso(curso);
        Profesor profesor = servicioCurso.verProfesor(curso);

        assertNotNull("El profesor no debería ser null", profesor);
        assertEquals("El ID del profesor no coincide", "P001", profesor.getId());
    }

    @Test
    public void testVerProfesorCursoInexistente() {
        Curso cursoSinProfesor = new Curso();
        cursoSinProfesor.setiD("NOPROF");

        Profesor profesor = servicioCurso.verProfesor(cursoSinProfesor);
        assertNull("El profesor debería ser null para un curso inexistente", profesor);
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
        assertTrue("Debería detectar el cruce de horarios", hayCruce);
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
        assertFalse("No debería haber cruce de horarios", hayCruce);
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
        assertTrue("El estudiante debería cumplir los prerequisitos", cumpleRequisitos);
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
        assertFalse("El estudiante no debería cumplir los prerequisitos", cumpleRequisitos);
    }

    @Test
    public void testAsignarSalaACursoExitoso() throws SQLException {
        cursoRepositorio.crearCurso(curso);
        Sala nuevaSala = new Sala();
        nuevaSala.setiD("102");
        nuevaSala.setCapacidad(35);

        boolean resultado = servicioCurso.asignarSalaACurso(curso.getiD(), nuevaSala);
        assertTrue("Debería asignar la sala exitosamente", resultado);
    }

    @Test
    public void testAsignarSalaACursoRepetida() throws SQLException {
        cursoRepositorio.crearCurso(curso);
        Sala salaExistente = curso.getSalas().get(0);

        boolean resultado = servicioCurso.asignarSalaACurso(curso.getiD(), salaExistente);
        assertFalse("No debería asignar una sala que ya está asignada", resultado);
    }
}