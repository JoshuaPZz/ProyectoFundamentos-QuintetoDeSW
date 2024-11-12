package Entidades;

import java.util.Date;

public class HorarioDisponible {
    private int id;
    private int diaSemanaId;
    private Date horaInicio;
    private Date horaFin;

    public HorarioDisponible(int id, int diaSemanaId, Date horaInicio, Date horaFin) {
        this.id = id;
        this.diaSemanaId = diaSemanaId;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDiaSemanaId() {
        return diaSemanaId;
    }

    public void setDiaSemanaId(int diaSemanaId) {
        this.diaSemanaId = diaSemanaId;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    @Override
    public String toString() {
        return "HorarioDisponible{" +
                "id=" + id +
                ", diaSemanaId=" + diaSemanaId +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                '}';
    }
}
