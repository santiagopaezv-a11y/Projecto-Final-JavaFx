package Aparta_Suites_UQ.model.reserva;

import java.time.LocalDateTime;

public class ConflictoCanal {
    private String codigoExterno;
    private String canalOrigen;
    private String motivo;
    private LocalDateTime fechaRegistro;

    public ConflictoCanal( String codigoExterno, String canalOrigen, String motivo, LocalDateTime fechaRegistro) {
    if (codigoExterno == null || codigoExterno.trim().isEmpty() ) {
        throw new IllegalArgumentException("El codigo de Externo no puede estar vacio. ");
    }
    if (canalOrigen == null || canalOrigen.trim().isEmpty() ) {
        throw new IllegalArgumentException("El canal origen no puede estar vacio. ");

    }
    if (motivo == null || motivo.trim().isEmpty() ) {
        throw new IllegalArgumentException("El motivo del conflicto no puede estar vacio. ");
    }
    this.codigoExterno = codigoExterno;
    this.canalOrigen = canalOrigen;
    this.motivo = motivo;
    this.fechaRegistro = fechaRegistro;
    }

    public String getCodigoExterno() {
        return codigoExterno;
    }

    public String getCanalOrigen() {
        return canalOrigen;
    }
    public String getMotivo() {
        return motivo;
    }
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public String toString() {
        return "ConflictoCanal[ " +
                " codigoExterno = " + codigoExterno +
                "motivo = " + motivo + "fechaRegistro = " + fechaRegistro + "]";
    }
}
