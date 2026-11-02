package Aparta_Suites_UQ.model.reserva;

public class ResultadoCancelacion {
    private double totalPagado;
    private double valorTotalEstancia;
    private long diasAntelacion;
    private double montoRetenido;
    private double montoADevolver;

    public ResultadoCancelacion(double totalPagado,  double valorTotalEstancia, long diasAntelacion, double montoRetenido, double montoADevolver) {
        this.totalPagado = totalPagado;
        this.valorTotalEstancia = valorTotalEstancia;
        this.diasAntelacion = diasAntelacion;
        this.montoRetenido = montoRetenido;
        this.montoADevolver = montoADevolver;
    }

    public double getTotalPagado() {
        return totalPagado;
    }
    public double getValorTotalEstancia() {
        return valorTotalEstancia;
    }
    public long getDiasAntelacion() {
        return diasAntelacion;
    }
    public double getMontoRetenido() {
        return montoRetenido;
    }
    public double getMontoADevolver() {
        return montoADevolver;
    }

    public String string(){
        return "Resultado Cancelacion [" +
                "Dias antelacion = " + "diasAntelacion + totalPagado = $" + totalPagado
                + " Retenido = $"  + montoRetenido + " A devolver = $" +  montoADevolver + "]";
    }

}
