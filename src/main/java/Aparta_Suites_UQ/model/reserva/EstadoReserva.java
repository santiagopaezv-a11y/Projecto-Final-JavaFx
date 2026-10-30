package Aparta_Suites_UQ.model.reserva;

public enum EstadoReserva {
    PENDIENTE,
    CONFIRMADA,
    EN_CURSO,
    FINALIZADA,
    CANCELADA,
    NO_SHOW;
    public boolean esActiva() {
        return this == PENDIENTE || this == CONFIRMADA || this == EN_CURSO;
    }
    public boolean esFinal() {
        return this == FINALIZADA || this == CANCELADA || this == NO_SHOW;
    }
}
