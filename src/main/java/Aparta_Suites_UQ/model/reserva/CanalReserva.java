package Aparta_Suites_UQ.model.reserva;

public enum CanalReserva {
    PORTAL(0.0, 0.30),
    DIRECTA(0.0, 0.0),
    EXTERNA(0.15, 0.0);
    private final double comision;
    private final double porcentajeAnticipo;

    CanalReserva(double comision,  double porcentajeAnticipo) {
        this.comision = comision;
        this.porcentajeAnticipo = porcentajeAnticipo;
    }

    public double getComision() {
        return comision;
    }

    public double getPorcentajeAnticipo() {
        return porcentajeAnticipo;
    }


    public int calcularComision(int valorEstancia){
        return (int) Math.round(valorEstancia * comision);
    }

    public int calcularAnticipoRequerido(int valorEstancia){
        return (int) Math.round(valorEstancia * porcentajeAnticipo);
    }
}
