package RepositorioBD;
import Entidades.Curso;
import Entidades.Estudiante;
import Entidades.Materia;
import Entidades.Profesor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;


public class ConexionBaseDeDatos {

    private Connection conexion;
    private static String conexionURL = "jdbc:sqlserver://SAMUEL\\SQLEXPRESS;databaseName=Prueba;user=sa;password=farito94;encrypt=true;trustServerCertificate=true;";

    private static Connection connection;

    private ConexionBaseDeDatos() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(conexionURL);
                System.out.println("Conexión establecida correctamente.");
            } catch (SQLException e) {
                System.out.println("Error al conectar a la base de datos: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }


    public static void cerrarConexion() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
    }
    /*public static void main(String[] args) {

        ProfesorRepositorio profesorRepo = new ProfesorRepositorio();

        try {
            // Insertar un profesor
            Profesor profesor = new Profesor();
            profesor.setNombre("Joshua");
            profesor.setApellido("Pérez");
            profesor.setDocumento("12334248");
            profesor.setCorreo("joshua.perez@ejemplo.com");
            profesor.setClave("clavew23");

            profesorRepo.insertarProfesor(profesor);

            // Obtener y mostrar la lista de profesores
            List<Profesor> profesores = profesorRepo.obtenerProfesores();
            for (Profesor prof : profesores) {
                System.out.println("Profesor ID: " + prof.getId());
                System.out.println("Nombre: " + prof.getNombre() + " " + prof.getApellido());
                System.out.println("Cursos asignados: " + prof.getCursos().size());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        /*
        try {
            MateriaRepositorio materiaRepo = new MateriaRepositorio();

            // Agregar una nueva materia
            Materia nuevaMateria = new Materia();
            nuevaMateria.setNombre("Matemáticas");
            nuevaMateria.setDescripcion("Curso de matemáticas básicas");
            nuevaMateria.setCreditos(4);
            materiaRepo.agregarMateria(nuevaMateria);
            System.out.println("Materia agregada: " + nuevaMateria.getiD());

            // Obtener una materia por ID
            Materia materiaObtenida = materiaRepo.obtenerMateriaPorId(1); // Cambia el ID según el que quieras probar
            if (materiaObtenida != null) {
                System.out.println("Materia obtenida: " + materiaObtenida.getNombre());
            }

            // Listar todas las materias
            System.out.println("Lista de materias:");
            for (Materia materia : materiaRepo.listarMaterias()) {
                System.out.println("Materia: " + materia.getNombre() + " - Créditos: " + materia.getCreditos());
            }

        } catch (Exception e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
            e.printStackTrace();
        }
    }
        /*
        // Crear una instancia del repositorio
        CursoRepositorio cursoRepositorio = new CursoRepositorio();

        try {
            // Intentar obtener un curso por su ID
            String idCurso = "1";  // Suponiendo que existe un curso con ID 1 en la base de datos
            Curso curso = cursoRepositorio.obtenerCursoPorId(idCurso);

            if (curso != null) {
                // Imprimir la información del curso
                System.out.println("Curso ID: " + curso.getiD());
                System.out.println("Cupos: " + curso.getCupos());
                System.out.println("Capacidad: " + curso.getCapacidad());
                System.out.println("Materia: " + curso.getMateria().getNombre());

                // Imprimir los horarios
                System.out.println("Horarios:");
                for (java.util.Date horario : curso.getHorarios()) {
                    System.out.println(horario);
                }

                // Imprimir las salas
                System.out.println("Salas:");
                curso.getSalas().forEach(sala -> System.out.println("Sala ID: " + sala.getiD()));

                // Imprimir los profesores
                System.out.println("Profesores:");
                curso.getProfesores().forEach(profesor -> System.out.println("Profesor ID: " + profesor.getId() + ", Nombre: " + profesor.getNombre()));

                // Imprimir los estudiantes
                System.out.println("Estudiantes:");
                curso.getEstudiantes().forEach(estudiante -> System.out.println("Estudiante ID: " + estudiante.getId() + ", Nombre: " + estudiante.getNombre()));
            } else {
                System.out.println("No se encontró el curso con ID: " + idCurso);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el curso: " + e.getMessage());
            e.printStackTrace();
        }
    }

         */
        /*
        EstudianteRepositorio estudianteRepositorio = new EstudianteRepositorio();
        try {
            // Obtener lista de estudiantes
            List<Estudiante> estudiantes = estudianteRepositorio.listaEstudiante();

            // Verificar si hay estudiantes
            if (estudiantes.isEmpty()) {
                System.out.println("No se encontraron estudiantes.");
            } else {
                // Recorrer y mostrar la información de cada estudiante
                for (Estudiante estudiante : estudiantes) {
                    System.out.println("ID: " + estudiante.getId());
                    System.out.println("Nombre: " + estudiante.getNombre());
                    System.out.println("Correo: " + estudiante.getCorreo());

                    // Mostrar los cursos actuales del estudiante
                    System.out.println("Cursos:");
                    for (Curso curso : estudiante.getCursos()) {
                        System.out.println("  Curso ID: " + curso.getiD());
                        System.out.println("  Materia: " + curso.getMateria().getNombre());
                        System.out.println("  Capacidad: " + curso.getCapacidad());
                        System.out.println("  Cupos: " + curso.getCupos());
                        System.out.println("  Horarios: " + curso.getHorarios());
                        System.out.println("  Salas: ");
                    }

                    // Mostrar los cursos vistos del estudiante
                    System.out.println("Cursos Vistos:");
                    for (Curso cursoVisto : estudiante.getCursosVistos()) {
                        System.out.println("  Curso Visto ID: " + cursoVisto.getiD());
                        System.out.println("  Materia: " + cursoVisto.getMateria().getNombre());
                    }

                    // Mostrar el carrito de cursos del estudiante
                    System.out.println("Carrito:");
                    for (Curso cursoCarrito : estudiante.getCarrito()) {
                        System.out.println("  Curso en Carrito ID: " + cursoCarrito.getiD());
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la lista de estudiantes: " + e.getMessage());
            e.printStackTrace();
        }

        // Asegurarse de cerrar la conexión
        cerrarConexion();  // Asegúrate de que el método cerrarConexion esté implementado correctamente.
    }

         */
}
