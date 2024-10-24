import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServicioCursoTest {

    @Test
    public void testHayCruceHorarios_SinCruce() {
        // Given: create a Curso and Estudiante
        Curso curso = new Curso();
        curso.setiD("curso1");
        Estudiante estudiante = new Estudiante();
        estudiante.setId(1);

        // When: instantiate the service and call the method
        ServicioCurso servicioCurso = new ServicioCurso();
        boolean resultado = servicioCurso.hayCruceHorarios(curso, estudiante);

        // Then: assert that there is no schedule conflict
        assertFalse(resultado, "No debe haber cruce de horarios para el curso1 y el estudiante 1");
    }

    @Test
    public void testHayCruceHorarios_ConCruce() {
        // Given: create a Curso and Estudiante with a schedule conflict
        Curso curso = new Curso();
        curso.setiD("curso2"); // Assume this course creates a conflict in the database
        Estudiante estudiante = new Estudiante();
        estudiante.setId(2);

        // When: instantiate the service and call the method
        ServicioCurso servicioCurso = new ServicioCurso();
        boolean resultado = servicioCurso.hayCruceHorarios(curso, estudiante);

        // Then: assert that there is a schedule conflict
        assertTrue(resultado, "Debe haber cruce de horarios para el curso2 y el estudiante 2");
    }

    @Test
    public void testHayCruceHorarios_CursoNull() {
        // Given: a null course
        Curso curso = null;
        Estudiante estudiante = new Estudiante();
        estudiante.setId(3);

        // When: call the method with a null course
        ServicioCurso servicioCurso = new ServicioCurso();
        boolean resultado = servicioCurso.hayCruceHorarios(curso, estudiante);

        // Then: assert that the result is false since the course is null
        assertFalse(resultado, "Debe devolver false si el curso es null");
    }
}
