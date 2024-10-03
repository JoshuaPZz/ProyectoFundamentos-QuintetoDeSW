package Entidades;

import java.util.ArrayList;
import java.util.List;

public class Estudiante {
    private int id;
    private String nombre;
    private String documento;
    private String correo;
    private String clave;
    private int creditosmax;
    private List<Curso> cursos;
    private List<Curso> cursosVistos;
    private List<Curso> carrito;

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
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

    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }

    public List<Curso> getCursosvistos() {
        return cursosVistos;
    }

    public void setCursosvistos(List<Curso> cursosVistos) {
        this.cursosVistos = cursosVistos;
    }

    public int getCreditosmax() {
        return creditosmax;
    }

    public void setCreditosmax(int creditosmax) {
        this.creditosmax = creditosmax;
    }

    public List<Curso> getCursosVistos() {
        return cursosVistos;
    }

    public void setCursosVistos(List<Curso> cursosVistos) {
        this.cursosVistos = cursosVistos;
    }

    public void setCarrito(List<Curso> carrito) {
        this.carrito = carrito;
    }

    public List<Curso> getCarrito() { return carrito; }


    public Estudiante() {
        this.cursos = new ArrayList<>();
        this.carrito = new ArrayList<>();
        this.cursosVistos=new ArrayList<>();
    }

}
