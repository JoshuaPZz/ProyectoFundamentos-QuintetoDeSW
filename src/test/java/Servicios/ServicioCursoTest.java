package Servicios;

import Entidades.*;
import RepositorioBD.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import java.sql.SQLException;

public class ServicioCursoTest {
    private ServicioCurso servicioCurso;
    private ServicioSala servicioSala;
    private CursoRepositorio cursoRepositorio;
    private ProfesorRepositorio profesorRepositorio;
    private EstudianteRepositorio estudianteRepositorio;
    private MateriaRepositorio materiaRepositorio;
    private SalaRepositorio salaRepositorio;
    private Curso curso;
    private Materia materia;
    private Horario horario;
    private Estudiante estudiante;
    private Sala sala;

    @BeforeEach
    public void setUp() throws SQLException {
        // Initialize services and repositories
        servicioSala = new ServicioSala();
        profesorRepositorio = new ProfesorRepositorio();
        cursoRepositorio = new CursoRepositorio();
        estudianteRepositorio = new EstudianteRepositorio();
        materiaRepositorio = new MateriaRepositorio();
        salaRepositorio = new SalaRepositorio();
        servicioCurso = new ServicioCurso(cursoRepositorio, estudianteRepositorio);

        // Create common test data
        materia = new Materia("Matematicas", "101", "", 3, new ArrayList<>(), new ArrayList<>());
        horario = new Horario("Lunes",
                ServicioCurso.crearHorario(2024, Calendar.JANUARY, 1, 9),
                ServicioCurso.crearHorario(2024, Calendar.JANUARY, 1, 11));
        sala = new Sala("105", "X", 30, "Y");

        // Set up database
        salaRepositorio.insertarSala(sala);
        materiaRepositorio.agregarMateria(materia);

        // Create course with common data
        curso = new Curso(materia, 30, horario, sala, 25);
        String nuevoCursoId = cursoRepositorio.crearCurso(curso);
        curso = cursoRepositorio.obtenerCursoPorId(nuevoCursoId);

        // Set up student
        estudiante = estudianteRepositorio.buscarPorId(1);

        for(Curso c : estudiante.getCursos()){
            estudianteRepositorio.eliminarInscripcion(estudiante.getId(), c.getiD());
        }


    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up test data
        if (curso != null && curso.getiD() != null) {
            cursoRepositorio.eliminarCurso(curso.getiD());
        }
        if (materia != null && materia.getiD() != null) {
            materiaRepositorio.eliminarMateria(materia.getiD());
        }
        if (sala != null && sala.getiD() != null) {
            salaRepositorio.eliminarSala(Integer.parseInt(sala.getiD()));
        }
    }





    @Test
    public void testCrearCursoExitoso() throws SQLException {

        Curso nuevoCurso = servicioCurso.buscarCursoPorID(this.curso.getiD());
        assertNotNull(nuevoCurso, "El curso creado no debería ser null");
        assertEquals(30, nuevoCurso.getCapacidad(), "La capacidad del curso no coincide");
        assertTrue(
                Objects.equals(nuevoCurso.getMateria().getNombre(), materia.getNombre()) &&
                        Objects.equals(nuevoCurso.getMateria().getiD(), materia.getiD()) &&
                        Objects.equals(nuevoCurso.getMateria().getDescripcion(), materia.getDescripcion()));
    }

    @Test
    public void testConsultarCursoExistente() throws SQLException {
        Curso nuevoCurso = servicioCurso.buscarCursoPorID(this.curso.getiD());
        System.out.println("ID del curso creado: " + nuevoCurso.getiD());
        assertNotNull(nuevoCurso, "El ID del curso debería estar asignado después de guardarlo");

        // Consultar el curso usando el ID
        Curso cursoConsultado = servicioCurso.consultarCurso(nuevoCurso);
        assertNotNull(cursoConsultado, "El curso consultado no debería ser null");
        assertEquals(nuevoCurso.getiD(), cursoConsultado.getiD(), "El ID del curso consultado no coincide");
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

        Curso nuevoCurso =  servicioCurso.buscarCursoPorID(this.curso.getiD());
        List<Estudiante> estudiantes = servicioCurso.verEstudiantes(nuevoCurso);

        assertNotNull(estudiantes, "La lista de estudiantes no debería ser null");
        assertTrue(estudiantes.isEmpty(), "La lista de estudiantes debería estar vacía");
    }

    @Test
    public void testVerEstudiantesCursoConEstudiantes() throws SQLException {
        Curso nuevoCurso = cursoRepositorio.obtenerCursoPorId(this.curso.getiD());
        estudianteRepositorio.inscribirCurso(estudiante.getId(), nuevoCurso.getiD());
        List<Estudiante> estudiantes = servicioCurso.verEstudiantes(nuevoCurso);
        assertNotNull(estudiantes, "La lista de estudiantes no debería ser null");
        assertFalse(estudiantes.isEmpty(), "La lista de estudiantes no debería estar vacía");
        assertEquals(1, estudiantes.size(), "Debería haber un estudiante en la lista");
        estudianteRepositorio.eliminarInscripcion(estudiante.getId(), nuevoCurso.getiD());
    }

    @Test
    public void testVerProfesorExistente() throws SQLException {

        //Crear una copia del curso default de pruebas
        Curso curso = new Curso(materia, 30, horario, sala, 25);
        List<Profesor> profesores = new ArrayList<>();
        Profesor profesor = new Profesor();
        profesor.setId(1);
        profesor.setNombre("Juan Perez");
        profesores.add(profesor);
        //Agregar el profesor al curso copia
        curso.setProfesores(profesores);

        //Crear nuevo curso en BD con profesor
        String cursoId = cursoRepositorio.crearCurso(curso);

        //Traer el nuevo curso de BD con su id
        Curso nuevoCurso = cursoRepositorio.obtenerCursoPorId(cursoId);
        assertNotNull(servicioCurso.verProfesor(nuevoCurso), "El profesor no debería ser null");
        assertEquals(1, servicioCurso.verProfesor(nuevoCurso).getId(), "El ID del profesor no coincide");

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
        //Crear un nuevo curso igual con diferente ID
        Curso curso = new Curso(materia, 30, horario, sala, 25);
        String nuevoCurso = cursoRepositorio.crearCurso(curso);
        Curso curso2 = cursoRepositorio.obtenerCursoPorId(nuevoCurso);

        //inscribir curso de prueba al estudiante
        estudianteRepositorio.inscribirCurso(estudiante.getId(), this.curso.getiD());

        //AssertTrue
        boolean hayCruce = servicioCurso.hayCruceHorarios(curso2, estudiante);
        assertTrue(hayCruce, "Debería detectar el cruce de horarios");

    }

    @Test
    public void testHayCruceHorariosSinCruce() throws SQLException {

        estudianteRepositorio.inscribirCurso(estudiante.getId(), curso.getiD());
        List<Horario> horarios = new ArrayList<>();
        String dia = "Lunes"; // Definir el día de la semana
        Date horaInicio = ServicioCurso.crearHorario(2024, Calendar.JANUARY, 1, 9);
        Date horaFin = ServicioCurso.crearHorario(2024, Calendar.JANUARY, 1, 11);
        horarios.add(new Horario(dia, horaInicio, horaFin));

        //Crear un nuevo curso igual con diferente ID
        Curso curso2 = new Curso(materia, 30, horario, sala, 25);

        //Poner diferentes horarios en el curso
        curso2.setHorarios(horarios);

        //AssertFalse
        boolean hayCruce = servicioCurso.hayCruceHorarios(curso2, estudiante);
        assertFalse(hayCruce, "No debería haber cruce de horarios");
        //limpiar base e datos

    }


    @Test
    public void testCumpleRequisitosConPreRequisitos() throws SQLException {
        // Configurar materia con prerrequisitos
        Curso curso = new Curso(materia, 30, horario, sala, 25);
        Materia prerequisito = new Materia();
        prerequisito.setiD("PRE101");
        curso.getMateria().getPrerequisitos().add(prerequisito);
        String cursoId = cursoRepositorio.crearCurso(curso);
        curso = servicioCurso.buscarCursoPorID(cursoId);
        // Agregar el prerrequisito a las materias vistas del estudiante
        estudianteRepositorio.obtenerMateriasVistasPorEstudiante(estudiante.getId()).add(prerequisito);
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
        Curso nuevoCurso = servicioCurso.buscarCursoPorID(this.curso.getiD());
        Sala nuevaSala = new Sala();
        nuevaSala.setiD("102");
        nuevaSala.setCapacidad(35);
        boolean resultado = servicioCurso.asignarSalaACurso(nuevoCurso, nuevaSala);
        assertTrue(resultado, "Debería asignar la sala exitosamente");

    }

    @Test
    public void testAsignarSalaACursoRepetida() throws SQLException {
        //Obtener las salas del curso global de prueba
        Sala salaExistente = curso.getSalas().get(0);

        //Asignar la sala
        boolean resultado = servicioCurso.asignarSalaACurso(curso, salaExistente);
        assertFalse(resultado, "No debería asignar una sala que ya está asignada");
    }
}