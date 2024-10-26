package Servicios;

import Entidades.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class ServicioCursoTest {

    @Test
    public void testHayCruceHorarios_SinCruce() {
        Curso curso = new Curso();
        curso.setiD("curso1");
        Estudiante estudiante = new Estudiante();
        estudiante.setId(1);

        ServicioCurso servicioCurso = new ServicioCurso();
        boolean resultado = servicioCurso.hayCruceHorarios(curso, estudiante);

        assertFalse(resultado, "No debe haber cruce de horarios para el curso1 y el estudiante 1");
    }

    @Test
    public void testHayCruceHorarios_ConCruce() {
        Curso curso = new Curso();
        curso.setiD("curso2");
        Estudiante estudiante = new Estudiante();
        estudiante.setId(2);

        ServicioCurso servicioCurso = new ServicioCurso();
        boolean resultado = servicioCurso.hayCruceHorarios(curso, estudiante);

        assertTrue(resultado, "Debe haber cruce de horarios para el curso2 y el estudiante 2");
    }

    @Test
    public void testHayCruceHorarios_CursoNull() {
        Curso curso = null;
        Estudiante estudiante = new Estudiante();
        estudiante.setId(3);

        ServicioCurso servicioCurso = new ServicioCurso();
        boolean resultado = servicioCurso.hayCruceHorarios(curso, estudiante);

        assertFalse(resultado, "Debe devolver false si el curso es null");
    }

    @Test
    public void testCrearCurso() {
        ServicioCurso servicioCurso = new ServicioCurso();

        Materia materia = new Materia();
        materia.setiD("MAT101");

        List<Date> horarios = new ArrayList<>();
        horarios.add(ServicioCurso.crearHorario(2024, 3, 1, 10));

        List<Sala> salas = new ArrayList<>();
        Sala sala = new Sala();
        sala.setiD("SALA1");
        salas.add(sala);

        List<Profesor> profesores = new ArrayList<>();
        Profesor profesor = new Profesor();
        profesor.setId(1);
        profesores.add(profesor);

        Curso curso = servicioCurso.crearCurso("CURSO1", materia, 30, horarios, salas, 25, profesores);

        assertNotNull(curso, "El curso no debería ser null");
        assertEquals("CURSO1", curso.getiD(), "El ID del curso debería coincidir");
        assertEquals(materia, curso.getMateria(), "La materia debería coincidir");
        assertEquals(30, curso.getCapacidad(), "La capacidad debería coincidir");
        assertEquals(horarios, curso.getHorarios(), "Los horarios deberían coincidir");
        assertEquals(salas, curso.getSalas(), "Las salas deberían coincidir");
        assertEquals(profesores, curso.getProfesores(), "Los profesores deberían coincidir");
    }

    @Test
    public void testConsultarCurso_CursoExistente() {
        ServicioCurso servicioCurso = new ServicioCurso();
        Curso curso = new Curso();
        curso.setiD("CURSO1");

        Curso resultado = servicioCurso.consultarCurso(curso);

        assertNull(resultado, "Debe retornar null ya que no hay conexión a BD");
    }

    @Test
    public void testConsultarCurso_CursoNull() {
        ServicioCurso servicioCurso = new ServicioCurso();
        Curso resultado = servicioCurso.consultarCurso(null);

        assertNull(resultado, "Debe retornar null cuando el curso es null");
    }

    @Test
    public void testVerEstudiantes_CursoExistente() {
        ServicioCurso servicioCurso = new ServicioCurso();
        Curso curso = new Curso();
        curso.setiD("CURSO1");

        List<Estudiante> estudiantes = servicioCurso.verEstudiantes(curso);

        assertNotNull(estudiantes, "La lista no debe ser null");
        assertTrue(estudiantes.isEmpty(), "La lista debe estar vacía ya que no hay conexión a BD");
    }

    @Test
    public void testVerEstudiantes_CursoNull() {
        ServicioCurso servicioCurso = new ServicioCurso();
        List<Estudiante> estudiantes = servicioCurso.verEstudiantes(null);

        assertNotNull(estudiantes, "La lista no debe ser null");
        assertTrue(estudiantes.isEmpty(), "La lista debe estar vacía para un curso null");
    }

    @Test
    public void testCrearHorario() {
        Date horario = ServicioCurso.crearHorario(2024, 3, 15, 14);

        Calendar cal = Calendar.getInstance();
        cal.setTime(horario);

        assertEquals(2024, cal.get(Calendar.YEAR), "El año debe ser 2024");
        assertEquals(3, cal.get(Calendar.MONTH), "El mes debe ser marzo (3)");
        assertEquals(15, cal.get(Calendar.DAY_OF_MONTH), "El día debe ser 15");
        assertEquals(14, cal.get(Calendar.HOUR_OF_DAY), "La hora debe ser 14");
        assertEquals(0, cal.get(Calendar.MINUTE), "Los minutos deben ser 0");
        assertEquals(0, cal.get(Calendar.SECOND), "Los segundos deben ser 0");
    }

    @Test
    public void testCumpleRequisitos_SinRequisitos() {
        ServicioCurso servicioCurso = new ServicioCurso();

        Estudiante estudiante = new Estudiante();
        estudiante.setId(1);

        Curso curso = new Curso();
        Materia materia = new Materia();
        materia.setPrerequisitos(new ArrayList<>());
        materia.setCorequisitos(new ArrayList<>());
        curso.setMateria(materia);

        boolean resultado = servicioCurso.cumpleRequisitos(estudiante, curso);

        assertFalse(resultado, "Debe retornar false ya que no hay conexión a BD");
    }

    @Test
    public void testBuscarCursoPorID() {
        ServicioCurso servicioCurso = new ServicioCurso();
        Curso curso = servicioCurso.buscarCursoPorID("CURSO1");

        assertNull(curso, "Debe retornar null ya que no hay conexión a BD");
    }


    @Test
    public void testAsignarSalaACurso_Exito() {
        ServicioCurso servicioCurso = new ServicioCurso();

        // Crear un curso
        Curso curso = new Curso();
        curso.setiD("CURSO1");
        List<Sala> salas = new ArrayList<>();
        curso.setSalas(salas); // Inicializa la lista de salas

        // Crear una sala
        Sala sala = new Sala();
        sala.setiD("SALA1");

        // Asignar la sala al curso
        boolean resultado = servicioCurso.asignarSalaACurso("CURSO1", sala);

        assertTrue(resultado, "Debería asignar la sala correctamente al curso");
        assertTrue(curso.getSalas().contains(sala), "La sala debería estar asignada al curso");
    }

    @Test
    public void testAsignarSalaACurso_SalaYaAsignada() {
        ServicioCurso servicioCurso = new ServicioCurso();

        // Crear un curso
        Curso curso = new Curso();
        curso.setiD("CURSO1");
        List<Sala> salas = new ArrayList<>();
        curso.setSalas(salas); // Inicializa la lista de salas

        // Crear una sala
        Sala sala = new Sala();
        sala.setiD("SALA1");

        // Asignar la sala al curso
        servicioCurso.asignarSalaACurso("CURSO1", sala); // Primera asignación

        // Intentar asignar la misma sala nuevamente
        boolean resultado = servicioCurso.asignarSalaACurso("CURSO1", sala);

        assertFalse(resultado, "No debería permitir asignar la misma sala al curso nuevamente");
        assertEquals(1, curso.getSalas().size(), "Debería haber solo una sala asignada al curso");
    }

    @Test
    public void testAsignarSalaACurso_CursoNoExistente() {
        ServicioCurso servicioCurso = new ServicioCurso();

        // Crear una sala
        Sala sala = new Sala();
        sala.setiD("SALA1");

        // Intentar asignar la sala a un curso que no existe
        boolean resultado = servicioCurso.asignarSalaACurso("CURSO_INEXISTENTE", sala);

        assertFalse(resultado, "Debería devolver false ya que el curso no existe");
    }

    @Test
    public void testAsignarSalaACurso_CursoNull() {
        ServicioCurso servicioCurso = new ServicioCurso();

        // Crear una sala
        Sala sala = new Sala();
        sala.setiD("SALA1");

        // Intentar asignar la sala a un curso nulo
        boolean resultado = servicioCurso.asignarSalaACurso(null, sala);

        assertFalse(resultado, "Debería devolver false si el curso es null");
    }

}