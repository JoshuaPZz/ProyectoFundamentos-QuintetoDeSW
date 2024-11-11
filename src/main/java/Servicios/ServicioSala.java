package Servicios;

import Entidades.Sala;
import RepositorioBD.SalaRepositorio;

import java.sql.SQLException;

public class ServicioSala {

    //Método para crear una sala
    public void agregarSala(Sala sala) {
        SalaRepositorio repositorio = new SalaRepositorio();
        try {
            repositorio.insertarSala(sala);
        } catch (SQLException e) {
            System.out.println("Error al agregar sala: " + e.getMessage());
        }

    }

    //Método para consultar los detalles de una sala
    public Sala consultarSala(Sala sala) {
        if (sala != null) {
            return sala; // Retorna la instancia de Sala encontrada
        }
        return null;
        /*
        if (sala != null) {
            return "Sala: " + sala.getiD() + ", Ubicación: " + sala.getUbicacion() +
                    ", Capacidad: " + sala.getCapacidad() + ", Tipo: " + sala.getTipo();
        }
        return "Sala no encontrada.";

         */
    }
}
