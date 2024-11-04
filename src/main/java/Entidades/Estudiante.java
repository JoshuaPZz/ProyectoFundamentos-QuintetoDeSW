package Entidades;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Estudiante {
    private int id;
    private String nombre;
    private String documento;
    private String correo;
    private String clave;
    private int creditosmax;
    private ObservableList<Curso> cursos;
    private ObservableList<Curso> cursosVistos;
    private ObservableList<Curso>  carrito;

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

    public ObservableList<Curso> getCursosObservable() {
        return cursos ; // Wraps the existing list in an ObservableList
    }
    public void setCursos(List<Curso> cursos) {
        this.cursos.addAll(cursos);
    }

    public List<Curso> getCursosvistos() {
        return cursosVistos;
    }
    public ObservableList<Curso> getCursosObservableVistos() {
        return cursosVistos;
    }

    public void setCursosvistos(List<Curso> cursosVistos) {
        this.cursosVistos.setAll(cursosVistos);
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
        this.cursosVistos.setAll(cursosVistos);
    }

    public void setCarrito(List<Curso> carrito) {
        this.carrito.setAll(carrito);
    }

    public List<Curso> getCarrito() { return carrito; }

    public ObservableList<Curso> getCarritosObservable() {
        return this.carrito;
    }

    public Estudiante(int id, String nombre, String documento, String correo, String clave, int creditosmax, List<Curso> cursos, List<Curso> cursosVistos, List<Curso> carrito) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
        this.clave = clave;
        this.creditosmax = creditosmax;
        this.cursos = FXCollections.observableList(cursos);
        this.carrito = FXCollections.observableList(carrito);
        this.cursosVistos = FXCollections.observableList(cursosVistos);
    }
    public Estudiante() {
        this.cursos = FXCollections.observableArrayList();
        this.cursosVistos = FXCollections.observableArrayList();
        this.carrito = FXCollections.observableArrayList();
    }

}
