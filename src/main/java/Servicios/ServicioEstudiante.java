package Servicios;
import Entidades.Curso;
import Entidades.Estudiante;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class ServicioEstudiante {
    private ServicioCurso servicioCurso;
    private List<Estudiante> estudiantes;

    public ServicioEstudiante(ServicioCurso servicioCurso) {
        this.servicioCurso = servicioCurso;
        this.estudiantes = new ArrayList<>();
    }

    // Agregar un curso dado al carrito del estudiante
    public boolean agregarCursoAlCarrito(Estudiante estudiante, Curso curso) {
        if (estudiante != null && curso != null) {
            List<Curso> cursosInscritos = estudiante.getCursos();
            List<Curso> carrito = estudiante.getCarrito();

            if (cursosInscritos.contains(curso)) {
                System.out.println("El curso ya está inscrito.");
                return false;
            }

            if (carrito.contains(curso)) {
                System.out.println("El curso ya está en el carrito.");
                return false;
            }

            carrito.add(curso);
            System.out.println("Curso agregado al carrito: " + curso.getiD());
            return true;
        }
        return false;
    }


}
