package Aparta_Suites_UQ.model.reserva;

public enum CanalReserva {
    PORTAL(0.0),
    DIRECTA(0.0),
    EXTERNA(0.15);
    private final double comision;

    CanalReserva(double comision) {
        this.comision = comision;
    }

    public double getComision() {
        return comision;
    }
}
