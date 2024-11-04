package Servicios;

import Entidades.Curso;
import Entidades.Estudiante;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServicioCursoTest {

    @Test
    public void testHayCruceHorarios_SinCruce() {

        Curso curso = new Curso();
        curso.setiD("curso1");
        Estudiante estudiante = new Estudiante();
        estudiante.setId(1);


       // ServicioCurso servicioCurso = new ServicioCurso();
        //boolean resultado = servicioCurso.hayCruceHorarios(curso, estudiante);


        //assertFalse(resultado, "No debe haber cruce de horarios para el curso1 y el estudiante 1");
    }

    @Test
    public void testHayCruceHorarios_ConCruce() {

        Curso curso = new Curso();
        curso.setiD("curso2");
        Estudiante estudiante = new Estudiante();
        estudiante.setId(2);


        //ServicioCurso servicioCurso = new ServicioCurso();
        //boolean resultado = servicioCurso.hayCruceHorarios(curso, estudiante);


        //assertTrue(resultado, "Debe haber cruce de horarios para el curso2 y el estudiante 2");
    }

    @Test
    public void testHayCruceHorarios_CursoNull() {

        Curso curso = null;
        Estudiante estudiante = new Estudiante();
        estudiante.setId(3);


      //  ServicioCurso servicioCurso = new ServicioCurso();
       // boolean resultado = servicioCurso.hayCruceHorarios(curso, estudiante);


        //assertFalse(resultado, "Debe devolver false si el curso es null");
    }
}
