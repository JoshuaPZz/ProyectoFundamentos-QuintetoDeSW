package RepositorioBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Entidades.Profesor;
import Entidades.Curso;

public class ProfesorRepositorio {
    private Connection getConnection() throws SQLException {
        return ConexionBaseDeDatos.getConnection();
    }

    // Método para obtener la lista de todos los profesores
    public List<Profesor> obtenerProfesores() throws SQLException {
        List<Profesor> profesores = new ArrayList<>();
        String consulta = "SELECT * FROM Profesor";

        try (Connection conexion = getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(consulta);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Profesor profesor = new Profesor();
                profesor.setId(rs.getInt("id"));
                profesor.setNombre(rs.getString("nombre"));
                profesor.setApellido(rs.getString("apellido"));
                profesor.setDocumento(rs.getString("documento"));
                profesor.setCorreo(rs.getString("correo"));
                profesor.setClave(rs.getString("clave"));
                profesor.setCursos(obtenerCursosPorProfesor(profesor.getId()));

                profesores.add(profesor);
            }
        }
        return profesores;
    }

    // Método para insertar un nuevo profesor
    public void insertarProfesor(Profesor profesor) throws SQLException {
        String consulta = "INSERT INTO Profesor (nombre, apellido, documento, correo, clave) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(consulta)) {

            pstmt.setString(1, profesor.getNombre());
            pstmt.setString(2, profesor.getApellido());
            pstmt.setString(3, profesor.getDocumento());
            pstmt.setString(4, profesor.getCorreo());
            pstmt.setString(5, profesor.getClave());

            pstmt.executeUpdate();
        }
    }

    // Método para obtener los cursos asignados a un profesor
    public List<Curso> obtenerCursosPorProfesor(int profesorId) throws SQLException {
        List<Curso> cursos = new ArrayList<>();
        String consulta = "SELECT c.id,/* c.cupos,*/ c.capacidad FROM Curso c " +
                "JOIN Asignacion a ON c.id = a.curso_id " +
                "WHERE a.profesor_id = ?";

        try (Connection conexion = getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
            pstmt.setInt(1, profesorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Curso curso = new Curso();
                    curso.setiD(rs.getString("id"));
                    //curso.setCupos(rs.getInt("cupos"));
                    curso.setCapacidad(rs.getInt("capacidad"));
                    // Puedes agregar más información si es necesario
                    cursos.add(curso);
                }
            }
        }
        return cursos;
    }
}
