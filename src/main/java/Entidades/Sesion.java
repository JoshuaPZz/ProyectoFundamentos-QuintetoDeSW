package Entidades;

public class Sesion {

    private static Sesion instancia;
    private Estudiante estudiante;

    // Constructor privado para evitar instancias externas
    private Sesion() {}

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
    }

    // Método para obtener el estudiante actual de la sesión
    public Estudiante getEstudiante() {
        return estudiante;
    }

    // Método para cerrar la sesión
    public void cerrarSesion() {
        estudiante = null;  // O resetear cualquier dato relevante
        instancia = null;   // Opcional si quieres que se pueda reiniciar la sesión
    }
}
