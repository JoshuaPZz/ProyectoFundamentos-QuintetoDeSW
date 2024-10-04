package Servicios;

import Entidades.Sala;

public class ServicioSala {

    // Método para crear una sala
    public Sala crearSala(String ID, String ubicacion, int capacidad, String tipo) {
        Sala nuevaSala = new Sala();
        nuevaSala.setiD(ID);
        nuevaSala.setUbicacion(ubicacion);
        nuevaSala.setCapacidad(capacidad);
        nuevaSala.setTipo(tipo);
        System.out.println("Sala " + ID + " creada correctamente.");
        return nuevaSala;
    }

    // Método para consultar los detalles de una sala
    public String consultarSala(Sala sala) {
        if (sala != null) {
            return "Sala: " + sala.getiD() + ", Ubicación: " + sala.getUbicacion() +
                    ", Capacidad: " + sala.getCapacidad() + ", Tipo: " + sala.getTipo();
        }
        return "Sala no encontrada.";
    }
}
