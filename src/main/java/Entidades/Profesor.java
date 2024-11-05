package Entidades;

import java.util.List;

public class Profesor {
    private int id;
    private String nombre;
    private String apellido;
    private String documento;
    private List<Curso> cursos;
    private String correo;
    private String clave;

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Profesor() {
    }

    public Profesor(String nombre, String apellido, String documento, List<Curso> cursos, String correo, String clave) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.cursos = cursos;
        this.correo = correo;
        this.clave = clave;
    }
}