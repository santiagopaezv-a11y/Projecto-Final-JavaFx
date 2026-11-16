package Aparta_Suites_UQ.model.reserva;

import java.io.Serializable;

public record PoliticaCancelacion(int diasDevolucionTotal, int diasRetencionParcial, double porcentajeRetencionParcial) implements Serializable {
    public PoliticaCancelacion {
        if (diasRetencionParcial < 0) {
            throw new IllegalArgumentException("Los días de retención parcial no pueden ser negativos.");
        }
        if (diasDevolucionTotal <= diasRetencionParcial) {
            throw new IllegalArgumentException(
                    "Los días para devolución total deben ser mayores que los días de retención parcial.");
        }
        if (porcentajeRetencionParcial < 0 || porcentajeRetencionParcial > 1) {
            throw new IllegalArgumentException("El porcentaje de retención debe estar entre 0 % y 100 %.");
        }
    }
    public static PoliticaCancelacion porDefecto() {
        return new PoliticaCancelacion(7, 2, 0.5);
    }
    public String descripcion() {
        return "Más de " + diasDevolucionTotal + " días: devolución total; entre " + diasRetencionParcial + " y "
                + diasDevolucionTotal + " días: se retiene el " + Math.round(porcentajeRetencionParcial * 100)
                + " %; con menos de " + diasRetencionParcial + " días o no-show: se retiene el 100 %";
    }
}
