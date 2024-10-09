package Entidades;

import Controladores.SceneManager;
import RepositorioBD.CursoRepositorio;
import RepositorioBD.EstudianteRepositorio;
import Servicios.ServicioCurso;
import Servicios.ServicioEstudiante;
import javafx.application.Application;
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
        Scanner scanner = new Scanner(System.in);
        ServicioEstudiante servicioEstudiante = new ServicioEstudiante();
        EstudianteRepositorio estudianteRepositorio = new EstudianteRepositorio();
        CursoRepositorio cursoRepositorio = new CursoRepositorio();

        Estudiante estudiante = null;

        try {
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
                System.out.println("3. Salir");
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