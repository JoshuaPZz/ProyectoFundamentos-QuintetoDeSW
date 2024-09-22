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
        salas.add(nuevaSala);
        System.out.println("Sala " + ID + " creada correctamente.");
        return nuevaSala;
    }

    public String consultarSala(Sala sala) {
        if (sala != null) {
            return "Sala: " + sala.getiD() + ", Ubicación: " + sala.getUbicacion() +
                    ", Capacidad: " + sala.getCapacidad() + ", Tipo: " + sala.getTipo();
        }
        return "Sala no encontrada.";
    }



}
