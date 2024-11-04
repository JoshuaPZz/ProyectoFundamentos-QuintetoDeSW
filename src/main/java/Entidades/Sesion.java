package Entidades;

public class Sesion {
    private static Sesion instancia;
    private Estudiante estudiante;
    private Profesor profesor;
    private TipoUsuario tipoUsuarioActual;

    public enum TipoUsuario {
        ESTUDIANTE,
        PROFESOR,
        NINGUNO
    }

    // Constructor privado para evitar instancias externas
    private Sesion() {
        this.tipoUsuarioActual = TipoUsuario.NINGUNO;
    }

    // Método estático para obtener la instancia única de la sesión
    public static Sesion getInstancia() {
        if (instancia == null) {
            instancia = new Sesion();
        }
        return instancia;
    }

    // Método para establecer el estudiante
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        this.profesor = null; // Al establecer un estudiante, limpiamos el profesor
        this.tipoUsuarioActual = TipoUsuario.ESTUDIANTE;
    }

    // Método para establecer el profesor
    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
        this.estudiante = null; // Al establecer un profesor, limpiamos el estudiante
        this.tipoUsuarioActual = TipoUsuario.PROFESOR;
    }

    // Método para obtener el estudiante actual de la sesión
    public Estudiante getEstudiante() {
        return estudiante;
    }

    // Método para obtener el profesor actual de la sesión
    public Profesor getProfesor() {
        return profesor;
    }

    // Método para obtener el tipo de usuario actual
    public TipoUsuario getTipoUsuarioActual() {
        return tipoUsuarioActual;
    }

    // Método para verificar si hay una sesión activa
    public boolean hayUsuarioActivo() {
        return tipoUsuarioActual != TipoUsuario.NINGUNO;
    }

    // Método para verificar si el usuario actual es estudiante
    public boolean esEstudiante() {
        return tipoUsuarioActual == TipoUsuario.ESTUDIANTE;
    }

    // Método para verificar si el usuario actual es profesor
    public boolean esProfesor() {
        return tipoUsuarioActual == TipoUsuario.PROFESOR;
    }

    // Método para cerrar la sesión
    public void cerrarSesion() {
        estudiante = null;
        profesor = null;
        tipoUsuarioActual = TipoUsuario.NINGUNO;
        instancia = null;   // Opcional si quieres que se pueda reiniciar la sesión
    }

    // Método para obtener el nombre del usuario actual (sea estudiante o profesor)
    public String getNombreUsuarioActual() {
        switch (tipoUsuarioActual) {
            case ESTUDIANTE:
                return estudiante != null ? estudiante.getNombre() : "";
            case PROFESOR:
                return profesor != null ? profesor.getNombre() + " " + profesor.getApellido() : "";
            default:
                return "";
        }
    }

    // Método para obtener el correo del usuario actual
    public String getCorreoUsuarioActual() {
        switch (tipoUsuarioActual) {
            case ESTUDIANTE:
                return estudiante != null ? estudiante.getCorreo() : "";
            case PROFESOR:
                return profesor != null ? profesor.getCorreo() : "";
            default:
                return "";
        }
    }
}