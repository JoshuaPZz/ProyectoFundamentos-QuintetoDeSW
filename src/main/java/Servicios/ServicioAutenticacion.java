package Servicios;

import Entidades.Estudiante;
import Entidades.Profesor;
import Entidades.Sesion;
import RepositorioBD.EstudianteRepositorio;
import RepositorioBD.ProfesorRepositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServicioAutenticacion {

    // Clase interna para el resultado de la autenticación
    public class ResultadoAutenticacion {
        private Estudiante estudiante;
        private Profesor profesor;
        private TipoUsuario tipo;

        public enum TipoUsuario {
            ESTUDIANTE,
            PROFESOR,
            NO_ENCONTRADO
        }

        public ResultadoAutenticacion(Estudiante estudiante) {
            this.estudiante = estudiante;
            this.tipo = TipoUsuario.ESTUDIANTE;
        }

        public ResultadoAutenticacion(Profesor profesor) {
            this.profesor = profesor;
            this.tipo = TipoUsuario.PROFESOR;
        }

        public ResultadoAutenticacion() {
            this.tipo = TipoUsuario.NO_ENCONTRADO;
        }

        public Estudiante getEstudiante() {
            return estudiante;
        }

        public Profesor getProfesor() {
            return profesor;
        }

        public TipoUsuario getTipo() {
            return tipo;
        }
    }

    // Método principal de autenticación
    public ResultadoAutenticacion validarCredenciales(String usuario, String contrasenia) throws SQLException {
        String queryEstudiante = "SELECT id FROM Estudiante WHERE correo = ? AND clave = ?";
        String queryProfesor = "SELECT id FROM Profesor WHERE correo = ? AND clave = ?";

        try (Connection conexion = RepositorioBD.ConexionBaseDeDatos.getConnection()) {
            // Verificar estudiante
            try (PreparedStatement ps = conexion.prepareStatement(queryEstudiante)) {
                ps.setString(1, usuario);
                ps.setString(2, contrasenia);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int estudianteId = rs.getInt("id");
                        EstudianteRepositorio estudianteRepo = new EstudianteRepositorio();
                        Estudiante estudiante = estudianteRepo.buscarPorId(estudianteId);
                        Sesion.getInstancia().setEstudiante(estudiante);
                        return new ResultadoAutenticacion(estudiante);
                    }
                }
            }

            // Si no es estudiante, verificar profesor
            try (PreparedStatement ps = conexion.prepareStatement(queryProfesor)) {
                ps.setString(1, usuario);
                ps.setString(2, contrasenia);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int profesorId = rs.getInt("id");
                        ProfesorRepositorio profesorRepo = new ProfesorRepositorio();
                        Profesor profesor = profesorRepo.buscarPorId(profesorId);
                        Sesion.getInstancia().setProfesor(profesor);
                        return new ResultadoAutenticacion(profesor);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error en la consulta de base de datos: " + e.getMessage());
            throw e;
        }

        return new ResultadoAutenticacion(); // Credenciales incorrectas
    }
}