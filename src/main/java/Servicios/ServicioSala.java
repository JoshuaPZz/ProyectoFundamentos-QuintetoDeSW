package Servicios;
import java.util.ArrayList;
import java.util.List;

import Entidades.Sala;

public class ServicioSala {

    private List<Sala> salas;
    public ServicioSala() {
        this.salas = new ArrayList<>();
    }
    public Sala crearSala(String ID, String ubicacion, int capacidad, String tipo) {
        Sala nuevaSala = new Sala();
        nuevaSala.setiD(ID);
        nuevaSala.setUbicacion(ubicacion);
        nuevaSala.setCapacidad(capacidad);
        nuevaSala.setTipo(tipo);
        salas.add(nuevaSala); // Agregar la nueva sala a la lista
        System.out.println("Sala " + ID + " creada correctamente.");
        return nuevaSala;
    }

}
