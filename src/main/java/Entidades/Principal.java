package Entidades;

import Controladores.*;
import RepositorioBD.*;
import Servicios.*;
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
        ProfesorRepositorio profesorRepositorio = new ProfesorRepositorio();
        CursoRepositorio cursoRepositorio = new CursoRepositorio();
        EstudianteRepositorio estudianteRepositorio = new EstudianteRepositorio();
        MateriaRepositorio materiaRepositorio = new MateriaRepositorio();

        ServicioCurso servicioCurso = new ServicioCurso(cursoRepositorio, estudianteRepositorio);
        ServicioMateria servicioMateria = new ServicioMateria();
        ServicioProfesor servicioProfesor = new ServicioProfesor(profesorRepositorio, servicioCurso);
        ServicioSala servicioSala = new ServicioSala();
        ServicioEstudiante servicioEstudiante = new ServicioEstudiante(servicioCurso, estudianteRepositorio);

        ControladorLogIn controladorLogIn = new ControladorLogIn();
        ControladorPantallaCursosEncontrados controladorPantallaCursosEncontrados = new ControladorPantallaCursosEncontrados(servicioEstudiante, servicioCurso);
        ControladorPantallaBusqueda controladorPantallaBusqueda = new ControladorPantallaBusqueda();
        ControladorPantallaInscripcion controladorPantallaInscripcion = new ControladorPantallaInscripcion();
        ControladorPantallaInscribirCurso controladorPantallaInscribirCurso = new ControladorPantallaInscribirCurso(servicioEstudiante);
        ControladorPantallaBaja controladorPantallaBaja = new ControladorPantallaBaja(servicioEstudiante);
        ControladorPantallaVerInformacion controladorPantallaVerInformacion = new ControladorPantallaVerInformacion(servicioCurso);
        ControladorPantallaInformacionCompleta controladorPantallaInformacionCompleta = new ControladorPantallaInformacionCompleta();
        ControladorProfesor controladorProfesor = new ControladorProfesor();
        ControladorSeleccion controladorSeleccion = new ControladorSeleccion();
        ControladorCreacionCurso controladorCreacionCurso = new ControladorCreacionCurso(materiaRepositorio);

        SceneManager.getInstance().getControllers().put("/Pantallas/PantallaLogin.fxml", controladorLogIn);
        SceneManager.getInstance().getControllers().put("/Pantallas/pantallaInscripcion.fxml", controladorPantallaInscripcion);
        SceneManager.getInstance().getControllers().put("/Pantallas/pantallaBusqueda.fxml", controladorPantallaBusqueda);
        SceneManager.getInstance().getControllers().put("/Pantallas/pantallaCursosEncontrados.fxml", controladorPantallaCursosEncontrados);
        SceneManager.getInstance().getControllers().put("/Pantallas/pantallaInscribirCurso.fxml", controladorPantallaInscribirCurso);
        SceneManager.getInstance().getControllers().put("/Pantallas/pantallaDarBaja.fxml", controladorPantallaBaja);
        SceneManager.getInstance().getControllers().put("/Pantallas/pantallaVerInformacion.fxml", controladorPantallaVerInformacion);
        SceneManager.getInstance().getControllers().put("/Pantallas/pantallaInformacionCompleta.fxml", controladorPantallaInformacionCompleta);
        SceneManager.getInstance().getControllers().put("/Pantallas/PantallaProfesores.fxml", controladorProfesor);
        SceneManager.getInstance().getControllers().put("/Pantallas/pantallaSeleccion.fxml", controladorSeleccion);
        SceneManager.getInstance().getControllers().put("/Pantallas/pantallaCreacionCurso.fxml", controladorCreacionCurso);
        // Set the primary stage in SceneManager
        SceneManager.getInstance().setPrimaryStage(stage);
        // Load custom font
        Font.loadFont(getClass().getResourceAsStream("/fonts/Lato-Bold.ttf"), 10);
        Font.loadFont(getClass().getResourceAsStream("/fonts/LeagueGothic.ttf"), 10);
        // Set stage properties
        stage.setTitle("Bienvenido......");
        stage.setResizable(true);

        // Switch to the login scene using SceneManager

        SceneManager.getInstance().switchScene("/Pantallas/PantallaLogin.fxml", "/CssStyle/LoginStyle.css", false);

        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        launch();
/*
        List<Profesor> profesores = new ArrayList<>();
        Profesor profesor = new Profesor();
        profesor.setId(1);
        profesor.setNombre("Juan Perez");
        profesores.add(profesor);
        MateriaRepositorio materiaRepositorio = new MateriaRepositorio();
        materiaRepositorio.obtenerMaterias();
        Scanner scanner = new Scanner(System.in);
        Materia materiaSeleccion = new Materia();
        Horario horario = null;
        try {
            // Obtener y mostrar la lista de materias
            List<Materia> materias = materiaRepositorio.obtenerMaterias();
            System.out.println("Materias disponibles:");
            for (Materia materia : materias) {
                System.out.println("ID: " + materia.getiD() + ", Nombre: " + materia.getNombre());
            }

            // Pedir al usuario que seleccione una materia
            System.out.print("Ingrese el ID de la materia que desea seleccionar: ");
            String idSeleccionado = scanner.nextLine();

            // Buscar la materia seleccionada
            Materia materiaSeleccionada = materiaRepositorio.obtenerMateriaPorId(Integer.parseInt(idSeleccionado));


            System.out.print("Ingrese el número de cupos para el curso: ");
            int cupos;
            while (true) {
                try {
                    cupos = Integer.parseInt(scanner.nextLine());
                    if (cupos > 0) {
                        break; // Salir del bucle si el número de cupos es válido
                    } else {
                        System.out.print("Ingrese un número de cupos válido (mayor que 0): ");
                    }
                } catch (NumberFormatException e) {
                    System.out.print("Por favor, ingrese un número válido de cupos: ");
                }
            }

            SalaRepositorio salaRepositorio = new SalaRepositorio();
            ServicioSala servicioSala = new ServicioSala();
            List<Sala> salas = salaRepositorio.obtenerSalas();
            System.out.println("Salas disponibles:");
            for (Sala sala : salas) {
                System.out.println("ID: " + sala.getiD() + ", Ubicación: " + sala.getUbicacion() +
                        ", Capacidad: " + sala.getCapacidad() + ", Tipo: " + sala.getTipo());
            }

            // Permitir que el usuario seleccione una sala por ID
            System.out.print("Ingrese el ID de la sala que desea seleccionar: ");
            int idSalaSeleccionada = Integer.parseInt(scanner.nextLine());
            Sala salaSeleccionada = salaRepositorio.obtenerSalaPorId(String.valueOf(idSalaSeleccionada));
            int capacidad = salaRepositorio.obtenerCapacidadSala(salaSeleccionada.getiD());

            CursoRepositorio cursoRepositorio = new CursoRepositorio();
            List<HorarioDisponible> horariosDisponibles = cursoRepositorio.obtenerHorariosDisponiblesPorSala(Integer.parseInt(salaSeleccionada.getiD()));

            if (!horariosDisponibles.isEmpty()) {
                System.out.println("Horarios disponibles para la sala " + salaSeleccionada.getUbicacion() + ":");
                for (HorarioDisponible horarios : horariosDisponibles) {
                    // Mostrar el horario con su día, hora de inicio y fin
                    String dia = cursoRepositorio.obtenerNombreDiaSemana(horarios.getDiaSemanaId());  // Método que convierte id de día a nombre
                    System.out.println("ID: " + horarios.getId() + ", Día: " + dia +
                            ", Hora de inicio: " + horarios.getHoraInicio() + ", Hora de fin: " + horarios.getHoraFin());
                }
                System.out.print("Ingrese el ID del horario que desea seleccionar: ");
                int idHorarioSeleccionado = Integer.parseInt(scanner.nextLine());
                horario = cursoRepositorio.obtenerHorarioPorRelacion(idHorarioSeleccionado);
            }
            EstudianteRepositorio estudianteRepositorio = new EstudianteRepositorio();
            ServicioCurso servicioCurso = new ServicioCurso(cursoRepositorio, estudianteRepositorio);
            Curso cursoCreado = servicioCurso.crearCurso(materiaSeleccionada, capacidad, horario, salaSeleccionada, cupos, profesores);
            System.out.println("\n✅ El proceso de creación del curso ha finalizado correctamente.");
            System.out.println("Puede proceder a realizar otras operaciones en el sistema.");

        } catch (SQLException e) {
            System.err.println("\n❌ Error en el proceso de creación del curso:");
            System.err.println("Detalles del error: " + e.getMessage());
            System.err.println("Por favor, contacte al administrador del sistema si el problema persiste.");
        } finally {
            scanner.close();
        }




    }

 */
    }
}
/*
        try {
            // Crear instancias de Profesor y Materia
            Profesor profesor = new Profesor(); // Asegúrate de que esta clase tenga al menos el método setId() o similar
            profesor.setId(30); // Suponiendo que el profesor con ID 1 ya existe en la base de datos
            Materia materia = new Materia();
            materia.setiD("1"); // Asegúrate de que la materia con ID 1 ya exista

            // Crear lista de Horarios (asume que tienes una clase Horario y sus métodos)
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Date horaInicio = dateFormat.parse("12:00"); // Ejemplo de hora de inicio
            Date horaFin = dateFormat.parse("13:00"); // Ejemplo de hora de fin
            List<Horario> horarios = new ArrayList<>();
            Horario horario1 = new Horario("Sabado", horaInicio, horaFin);
            horarios.add(horario1);

            // Crear lista de Salas (asume que tienes una clase Sala y sus métodos)
            List<Sala> salas = new ArrayList<>();
            Sala sala1 = new Sala();
            sala1.setiD("14"); // Asegúrate de que esta sala exista en la base de datos
            salas.add(sala1);

            // Capacidad y cupos del curso
            int capacidad = 30;
            int cupos = 25;

            // Crear instancia del servicio y ejecutar la función
            ServicioProfesor servicioProfesor = new ServicioProfesor();
            Curso nuevoCurso = servicioProfesor.crearYAsignarCurso(
                    profesor, "81", materia, capacidad, horarios, salas, cupos
            );

            if (nuevoCurso != null) {
                System.out.println("Curso creado y asignado con éxito:");
                System.out.println("ID del curso: " + nuevoCurso.getiD());
                System.out.println("Materia: " + nuevoCurso.getMateria().getiD());
                System.out.println("Profesor asignado: " + profesor.getId());
            } else {
                System.out.println("Error al crear o asignar el curso.");
            }
        } catch (SQLException e) {
            System.out.println("Error de SQL: " + e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

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