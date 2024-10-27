package Entidades;

import java.util.Date;

public class Horario {
    private String dia;
    private Date horaInicio;
    private Date horaFin;

    public Horario(String dia, Date horaInicio, Date horaFin) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public String getDia() {
        return dia;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

}
