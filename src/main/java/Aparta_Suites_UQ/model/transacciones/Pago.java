package Aparta_Suites_UQ.model.transacciones;

import java.time.LocalDateTime;

public class Pago {
    private final double valor;
    private final MedioPago medioPago;
    private final LocalDateTime fecha;

    public Pago(double valor, MedioPago medioPago) {
        if (medioPago == null) {
            throw new IllegalArgumentException("El medio de pago no puede ser nulo.");
        }
        // SE CAMBIA: Permitir valores negativos para las correcciones
        if (valor == 0) {
            throw new IllegalArgumentException("El valor del pago no puede ser cero.");
        }
        this.valor = valor;
        this.medioPago = medioPago;
        this.fecha = LocalDateTime.now();
    }

    public double getValor() { return valor; }
    public MedioPago getMedioPago() { return medioPago; }
    public LocalDateTime getFecha() { return fecha; }
}
