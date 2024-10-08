package Entidades;

import Controladores.SceneManager;
import RepositorioBD.CursoRepositorio;
import RepositorioBD.EstudianteRepositorio;
import Servicios.ServicioCurso;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Principal extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Set the primary stage in SceneManager
        SceneManager.getInstance().setPrimaryStage(stage);

        // Load custom font
        Font.loadFont(getClass().getResourceAsStream("/fonts/Lato-Bold.ttf"), 10);
        Font.loadFont(getClass().getResourceAsStream("/fonts/LeagueGothic.ttf"), 10);
        // Set stage properties
        stage.setTitle("Bienvenido......");
        stage.setResizable(true);

        // Switch to the login scene using SceneManager
        SceneManager.getInstance().switchScene("/Pantallas/pantallaLogin.fxml", "/CssStyle/LoginStyle.css");

        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        //launch();
        // Crear instancia de ServicioCurso
        CursoRepositorio cursoRepositorio = new CursoRepositorio();
        EstudianteRepositorio estudianteRepositorio = new EstudianteRepositorio();

        ServicioCurso servicioCurso = new ServicioCurso();

        // Simulación de un Curso para las pruebas
        Curso curso = new Curso();
        curso.setiD("1");  // Asignar ID ficticio al curso

        // Simulación de un Estudiante para las pruebas
        //Estudiante estudiante = new Estudiante();
        //estudiante.setId(1);  // Asignar ID ficticio al estudiante

        // 1. Consultar un curso
        /*
        Curso consultado = servicioCurso.consultarCurso(curso);
        if (consultado != null) {
            System.out.println("Curso consultado: ID " + consultado.getiD() + " Materia: " + consultado.getMateria().getNombre());
        } else {
            System.out.println("No se encontró el curso.");
        }

         */

        

        try {
            List<Estudiante> listaEstudiantes = estudianteRepositorio.listaEstudiante();

            // Imprimir los datos de los estudiantes
            for (Estudiante estudiante : listaEstudiantes) {
                System.out.println("ID: " + estudiante.getId());
                System.out.println("Nombre: " + estudiante.getNombre());
                System.out.println("Documento: " + estudiante.getDocumento());
                System.out.println("Correo: " + estudiante.getCorreo());
                System.out.println("Cursos Actuales: " + estudiante.getCursos().size());
                System.out.println("Cursos Vistos: " + estudiante.getCursosVistos().size());
                System.out.println("Carrito: " + estudiante.getCarrito().size());
                System.out.println("------");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la lista de estudiantes: " + e.getMessage());
        }
    }

/*
        // 2. Ver la lista de estudiantes de un curso
        List<Estudiante> estudiantes = servicioCurso.verEstudiantes(curso);
        if (!estudiantes.isEmpty()) {
            System.out.println("Lista de estudiantes inscritos en el curso:");
            for (Estudiante est : estudiantes) {
                System.out.println("Estudiante: " + est.getNombre());
            }
        } else {
            System.out.println("No hay estudiantes inscritos en este curso.");
        }

        // 3. Ver el profesor principal del curso
        Profesor profesor = servicioCurso.verProfesor(curso);
        if (profesor != null) {
            System.out.println("Profesor del curso: " + profesor.getNombre());
        } else {
            System.out.println("No se encontró un profesor para este curso.");
        }

        // 4. Buscar un curso por ID
        String idCurso = "C002";  // ID ficticio
        Curso cursoBuscado = servicioCurso.buscarCursoPorID(idCurso);
        if (cursoBuscado != null) {
            System.out.println("Curso encontrado: " + cursoBuscado.getNombre());
        } else {
            System.out.println("No se encontró el curso con ID: " + idCurso);
        }

        // 5. Verificar si hay cruce de horarios
        boolean hayCruce = servicioCurso.hayCruceHorarios(curso, estudiante);
        if (hayCruce) {
            System.out.println("Hay un cruce de horarios.");
        } else {
            System.out.println("No hay cruce de horarios.");
        }

        // 6. Verificar si el estudiante cumple con los requisitos del curso
        boolean cumpleRequisitos = servicioCurso.cumpleRequisitos(estudiante, curso);
        if (cumpleRequisitos) {
            System.out.println("El estudiante cumple con los requisitos para inscribirse en el curso.");
        } else {
            System.out.println("El estudiante no cumple con los requisitos.");
        }
    }
    }

 */
}