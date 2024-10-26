package Entidades;

import Controladores.*;
import RepositorioBD.CursoRepositorio;
import RepositorioBD.EstudianteRepositorio;
import Servicios.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ServicioEstudiante servicioEstudiante = new ServicioEstudiante();
        ServicioCurso servicioCurso = new ServicioCurso();
        ServicioMateria servicioMateria = new ServicioMateria();
        ServicioProfesor servicioProfesor = new ServicioProfesor();
        ServicioSala servicioSala = new ServicioSala();

        ControladorLogIn controladorLogIn = new ControladorLogIn();
        ControladorPantallaCursosEncontrados controladorPantallaCursosEncontrados = new ControladorPantallaCursosEncontrados(servicioEstudiante, servicioCurso);
        ControladorPantallaBusqueda controladorPantallaBusqueda = new ControladorPantallaBusqueda(controladorPantallaCursosEncontrados);
        ControladorPantallaInscripcion controladorPantallaInscripcion = new ControladorPantallaInscripcion();

        FXMLLoader loader = null;

        loader = SceneManager.getInstance().findLoader("ControladorPantallaCursosEncontrados");
        loader.setController(controladorPantallaCursosEncontrados);
        loader = SceneManager.getInstance().findLoader("ControladorLogIn");
        loader.setController(controladorLogIn);
        loader = SceneManager.getInstance().findLoader("ControladorPantallaInscripcion");
        loader.setController(controladorPantallaInscripcion);
        loader = SceneManager.getInstance().findLoader("ControladorPantallaBusqueda");
        loader.setController(controladorPantallaBusqueda);
        //Se cargan todos los controladores
        SceneManager.getInstance().loadControllers();

        // Set the primary stage in SceneManager
        SceneManager.getInstance().setPrimaryStage(stage);

        // Load custom font
        Font.loadFont(getClass().getResourceAsStream("/fonts/Lato-Bold.ttf"), 10);
        Font.loadFont(getClass().getResourceAsStream("/fonts/LeagueGothic.ttf"), 10);
        // Set stage properties
        stage.setTitle("Bienvenido......");
        stage.setResizable(true);

        // Switch to the login scene using SceneManager
        SceneManager.getInstance().switchScene("ControladorLogIn", "/CssStyle/LoginStyle.css", false);

        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        launch();
    }
}

/*
        Scanner scanner = new Scanner(System.in);
        ServicioEstudiante servicioEstudiante = new ServicioEstudiante();
        EstudianteRepositorio estudianteRepositorio = new EstudianteRepositorio();
        CursoRepositorio cursoRepositorio = new CursoRepositorio();

        Estudiante estudiante = estudianteRepositorio.buscarPorId(1);
        Curso curso = cursoRepositorio.obtenerCursoPorId("1");
        servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);

        ArrayList<String> materiasCarrito = servicioEstudiante.verCarritoToString(estudiante);
        if (materiasCarrito != null) {
            for (String materia : materiasCarrito) {
                System.out.println(materia); // Aquí mostramos cada curso en el carrito
            }
        } else {
            System.out.println("El carrito está vacío o el estudiante no es válido.");
        }


    }
}



        try {
            Scanner scanner = new Scanner(System.in);
            ServicioEstudiante servicioEstudiante = new ServicioEstudiante();
            EstudianteRepositorio estudianteRepositorio = new EstudianteRepositorio();
            CursoRepositorio cursoRepositorio = new CursoRepositorio();
            Estudiante estudiante = null;
            System.out.print("Introduce el ID del estudiante: ");
            int estudianteId = scanner.nextInt();
            estudiante = estudianteRepositorio.buscarPorId(estudianteId);
            if (estudiante == null) {
                System.out.println("Estudiante no encontrado. Terminando programa.");
                return;
            }
            System.out.println("Estudiante encontrado: " + estudiante.getNombre());

            boolean salir = false;
            while (!salir) {
                System.out.println("\n--- MENÚ ---");
                System.out.println("1. Agregar curso al carrito");
                System.out.println("2. Inscribir cursos del carrito");
                System.out.println("3. Mostrar cursos de una materia");
                System.out.println("4. Salir");
                System.out.print("Selecciona una opción: ");
                int opcion = scanner.nextInt();

                switch (opcion) {
                    case 1:
                        System.out.print("\nIntroduce el ID del curso a agregar: ");
                        String cursoId = scanner.next();
                        Curso curso = cursoRepositorio.obtenerCursoPorId(String.valueOf(cursoId));

                        if (curso != null) {
                            boolean agregado = servicioEstudiante.agregarCursoAlCarrito(estudiante, curso);
                            if (agregado) {
                                System.out.println("Curso agregado al carrito " + cursoId);
                            } else {
                                System.out.println("No se pudo agregar el curso.");
                            }
                        } else {
                            System.out.println("Curso no encontrado.");
                        }
                        break;

                    case 2:
                        System.out.println("\n--- Inscribiendo cursos del carrito ---");
                        List<Curso> carritoCopia = new ArrayList<>(estudiante.getCarrito());
                        for (Curso cursoEnCarrito : carritoCopia) {
                            boolean inscrito = servicioEstudiante.inscribirCurso(estudiante, cursoEnCarrito);
                            if (inscrito) {
                                System.out.println("Curso inscrito exitosamente: " + cursoEnCarrito.getMateria().getNombre());
                            } else {
                                System.out.println("No se pudo inscribir el curso: " + cursoEnCarrito.getMateria().getNombre());
                            }
                        }

                        break;

                    case 3:
                        System.out.print("\nIntroduce el ID de la materia: ");
                        String materiaId = scanner.next();
                        List<String> cursos = cursoRepositorio.obtenerCursosPorMateria(materiaId);

                        if (!cursos.isEmpty()) {
                            System.out.println("\n--- Cursos de la materia con ID: " + materiaId + " ---");
                            for (String cursoInfo : cursos) {
                                System.out.println(cursoInfo);
                            }
                        } else {
                            System.out.println("No se encontraron cursos para la materia con ID: " + materiaId);
                        }
                        break;

                    case 4:
                        salir = true;
                        System.out.println("Saliendo del programa.");
                        break;

                    default:
                        System.out.println("Opción no válida. Inténtalo de nuevo.");
                        break;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

}

 */