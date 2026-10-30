package Aparta_Suites_UQ.model.transacciones;

import java.time.LocalDateTime;

public class Pago {
    private int valor;
    private MedioPago medioPago;
    private LocalDateTime fecha;
    public Pago(int valor, MedioPago medioPago) {
        this.valor = valor;
        this.medioPago = medioPago;
        this.fecha = LocalDateTime.now();
    }
    public int getValor() { return valor; }
    public MedioPago getMedioPago() { return medioPago; }
    public LocalDateTime getFecha() { return fecha; }
}
