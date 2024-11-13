package Entidades;

import java.util.Date;

public class Horario {
    private String dia;
    private Date horaInicio;
    private Date horaFin;
    private boolean asignado;

    public Horario(String dia, Date horaInicio, Date horaFin, boolean asignado) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.asignado = asignado;
    }

    public Horario(String dia, Date horaInicio, Date horaFin) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public Horario() {

    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    public void setAsignado(boolean asignado) {
        this.asignado = asignado;
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

    public boolean isAsignado() {
        return asignado;
    }

}
