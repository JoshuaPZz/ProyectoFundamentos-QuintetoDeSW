package Servicios;
import Entidades.Materia;

import java.util.ArrayList;
import java.util.List;
public class ServicioMateria {
    private List<Materia> materias;

    public ServicioMateria() {
        this.materias = new ArrayList<>(); // Inicializa la lista de materias
    }

    public Materia crearMateria(String nombre, String ID, String descripcion, int creditos, List<Materia> prerequisitos, List<Materia>  corequisitos) {
        Materia nuevaMateria = new Materia();
        nuevaMateria.setNombre(nombre);
        nuevaMateria.setiD(ID);
        nuevaMateria.setDescripcion(descripcion);
        nuevaMateria.setCreditos(creditos);
        nuevaMateria.setPrerequisitos(prerequisitos != null ? prerequisitos : new ArrayList<>());
        nuevaMateria.setCorequisitos(corequisitos != null ? corequisitos : new ArrayList<>());
        return nuevaMateria;
    }

    // Método para consultar información de una materia
    public String consultarMateria(Materia materia) {
        if (materia != null) {
            return "Materia: " + materia.getNombre() + ", ID: " + materia.getiD() +
                    ", Descripción: " + materia.getDescripcion() + ", Créditos: " + materia.getCreditos();
        }
        return "Materia no encontrada.";
    }






}
