package Aparta_Suites_UQ.model.reserva;

import java.io.Serializable;

/**
 * Política de cancelación. Se "congela" en cada reserva al crearla: si el administrador la cambia después,
 * las reservas ya creadas conservan la que tenían.
 * <ul>
 *   <li>Con más de {@code diasDevolucionTotal} días de antelación se devuelve todo lo pagado.</li>
 *   <li>Entre {@code diasRetencionParcial} y {@code diasDevolucionTotal} días se retiene el porcentaje parcial del valor de la estancia.</li>
 *   <li>Con menos de {@code diasRetencionParcial} días (48 horas por defecto) o en no-show se retiene el 100 %.</li>
 * </ul>
 */
public final class PoliticaCancelacion implements Serializable {

    private final int diasDevolucionTotal;
    private final int diasRetencionParcial;
    private final double porcentajeRetencionParcial;

    public PoliticaCancelacion(int diasDevolucionTotal, int diasRetencionParcial, double porcentajeRetencionParcial) {
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
        this.diasDevolucionTotal = diasDevolucionTotal;
        this.diasRetencionParcial = diasRetencionParcial;
        this.porcentajeRetencionParcial = porcentajeRetencionParcial;
    }

    public static PoliticaCancelacion porDefecto() {
        return new PoliticaCancelacion(7, 2, 0.5);
    }

    public int getDiasDevolucionTotal() {
        return diasDevolucionTotal;
    }

    public int getDiasRetencionParcial() {
        return diasRetencionParcial;
    }

    public double getPorcentajeRetencionParcial() {
        return porcentajeRetencionParcial;
    }

    public String descripcion() {
        return "Más de " + diasDevolucionTotal + " días: devolución total; entre " + diasRetencionParcial + " y "
                + diasDevolucionTotal + " días: se retiene el " + Math.round(porcentajeRetencionParcial * 100)
                + " %; con menos de " + diasRetencionParcial + " días o no-show: se retiene el 100 %";
    }
}
