package Servicios;

import Entidades.Materia;
import RepositorioBD.MateriaRepositorio;

import java.sql.SQLException;
import java.util.List;

public class ServicioMateria {

    //Método para consultar información de una materia
    public String consultarMateria(Materia materia) {
        if (materia != null) {
            return "Materia: " + materia.getNombre() + ", ID: " + materia.getiD() +
                    ", Descripción: " + materia.getDescripcion() + ", Créditos: " + materia.getCreditos();
        }
        return "Materia no encontrada.";
    }

    //Método para verificar si una materia cumple con los prerrequisitos
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

    //Método para verificar si una materia tiene los corequisitos cursados
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

    //Método para buscar una materia por su ID en una lista dada
    public Materia buscarMateriaPorID(String id) {
        MateriaRepositorio repositorioMateria = new MateriaRepositorio();
        Materia materia = null;
        try {
            materia = repositorioMateria.obtenerMateriaPorId(Integer.parseInt(id));
            if (materia != null) {
                return materia;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar materia por ID: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("ID de materia inválido: " + id);
        }
        System.out.println("Materia con ID: " + id + " no encontrada.");
        return null;
    }

    public void agregarMateria(Materia materia) {
        MateriaRepositorio repositorioMateria = new MateriaRepositorio();
        try {
            repositorioMateria.agregarMateria(materia);
        } catch (SQLException e) {
            System.out.println("Error al agregar materia: " + e.getMessage());
        }
    }

}

