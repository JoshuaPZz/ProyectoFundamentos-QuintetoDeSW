package Servicios;

import Entidades.Materia;

import java.util.List;

public class ServicioMateria {

    // Método para consultar información de una materia
    public String consultarMateria(Materia materia) {
        if (materia != null) {
            return "Materia: " + materia.getNombre() + ", ID: " + materia.getiD() +
                    ", Descripción: " + materia.getDescripcion() + ", Créditos: " + materia.getCreditos();
        }
        return "Materia no encontrada.";
    }

    // Método para verificar si una materia cumple con los prerrequisitos
    public boolean cumplePrerequisitos(Materia materia, List<Materia> materiasCursadas) {
        if (materia != null && materiasCursadas != null) {
            for (Materia prerequisito : materia.getPrerequisitos()) {
                if (!materiasCursadas.contains(prerequisito)) {
                    System.out.println("No cumple con el prerrequisito: " + prerequisito.getNombre());
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    // Método para verificar si una materia tiene los corequisitos cursados
    public boolean cumpleCorequisitos(Materia materia, List<Materia> materiasInscritas) {
        if (materia != null && materiasInscritas != null) {
            for (Materia corequisito : materia.getCorequisitos()) {
                if (!materiasInscritas.contains(corequisito)) {
                    System.out.println("No cumple con el corequisito: " + corequisito.getNombre());
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    // Método para buscar una materia por su ID en una lista dada
    public Materia buscarMateriaPorID(String id, List<Materia> listaMaterias) {
        if (listaMaterias != null) {
            for (Materia materia : listaMaterias) {
                if (materia.getiD().equals(id)) {
                    return materia; // Devuelve la materia si se encuentra
                }
            }
        }
        System.out.println("Materia no encontrada con ID: " + id);
        return null; // Devuelve null si no se encuentra la materia
    }
}

