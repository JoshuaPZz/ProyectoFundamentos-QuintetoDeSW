package Servicios;

import Entidades.*;
import RepositorioBD.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import java.sql.SQLException;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class ServicioCursoTest {
    private ServicioCurso servicioCurso;
    private CursoRepositorio cursoRepositorio;
    private ProfesorRepositorio profesorRepositorio;
    private EstudianteRepositorio estudianteRepositorio;
    private MateriaRepositorio materiaRepositorio;
    private SalaRepositorio salaRepositorio;
    private Curso curso;
    private Estudiante estudiante;

    @BeforeEach
    public void setUp() throws SQLException {
        profesorRepositorio = new ProfesorRepositorio();
        cursoRepositorio = new CursoRepositorio();
        estudianteRepositorio = new EstudianteRepositorio();
        materiaRepositorio = new MateriaRepositorio();
        salaRepositorio = new SalaRepositorio();
        servicioCurso = new ServicioCurso(cursoRepositorio, estudianteRepositorio);

        // Configurar datos de prueba
        Materia materia = new Materia();
        materia.setiD("101");
        materia.setCreditos(3);
        materia.setNombre("Matemáticas");
        materiaRepositorio.agregarMateria(materia);

        List<Horario> horarios = new ArrayList<>();
        String dia = "Lunes"; // Definir el día de la semana
        Date horaInicio = ServicioCurso.crearHorario(2024, Calendar.JANUARY, 1, 9);
        Date horaFin = ServicioCurso.crearHorario(2024, Calendar.JANUARY, 1, 11);
        horarios.add(new Horario(dia, horaInicio, horaFin));

        Sala sala = new Sala();
        sala.setiD("101");
        sala.setUbicacion("Edificio Basicas");
        sala.setCapacidad(30);
        sala.setTipo("Laboratorio");
        salaRepositorio.insertarSala(sala);

        Sala salaInsertada = salaRepositorio.obtenerSalaPorId(sala.getiD());
        assertNotNull(salaInsertada, "La sala no fue insertada correctamente");

        List<Sala> salas = new ArrayList<>();
        salas.add(salaInsertada);

        List<Profesor> profesores = new ArrayList<>();
        Profesor profesor = new Profesor();
        profesor.setId(1);
        profesor.setNombre("Juan Perez");
        profesores.add(profesor);


        curso = new Curso();
        curso.setMateria(materia);
        curso.setCapacidad(30);
        curso.setHorarios(horarios);
        curso.setProfesores(profesores);
        curso.setEstudiantes(new ArrayList<>());
        curso.setiD("CURSO101");
        curso.setSalas(salas);

        estudiante = new Estudiante();
        estudiante.setId(1);
        estudiante.setNombre("Test Student");
        estudiante.setCursos(new ArrayList<>());
        estudiante.setCursosVistos(new ArrayList<>());
/*
        for(Horario horario : curso.getHorarios()) {
            System.out.println(horario.getDia());
            System.out.println(horario.getHoraInicio());
            System.out.println(horario.getHoraFin());
        }

 */

        cursoRepositorio.crearCurso(curso);
    }

    public void tearDown() throws SQLException {
        cursoRepositorio.eliminarCurso(curso.getiD());
    }

    @Test
    public void testCrearCursoExitoso() {
        Materia materia = new Materia();
        materia.setiD("102");
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
        System.out.println("ID del curso creado: " + curso.getiD());
        assertNotNull(curso, "El ID del curso debería estar asignado después de guardarlo");

        // Consultar el curso usando el ID
        Curso cursoConsultado = servicioCurso.consultarCurso(curso);

        assertNotNull(cursoConsultado, "El curso consultado no debería ser null");
        assertEquals(curso.getiD(), cursoConsultado.getiD(), "El ID del curso consultado no coincide");
    }


    @Test
    public void testConsultarCursoInexistente() {
        Curso cursoInexistente = new Curso();
        cursoInexistente.setiD("0");

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
        materia2.setiD("103");
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
        materia2.setiD("103");
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