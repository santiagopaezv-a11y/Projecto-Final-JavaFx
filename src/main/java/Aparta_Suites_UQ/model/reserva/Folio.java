package Aparta_Suites_UQ.model.reserva;

import Aparta_Suites_UQ.model.transacciones.Cargo;
import Aparta_Suites_UQ.model.transacciones.MedioPago;
import Aparta_Suites_UQ.model.transacciones.Pago;

import java.util.ArrayList;
import java.util.List;

public class Folio {
    private List<Cargo> cargos;
    private List<Pago> pagos;
    public Folio() {
        this.cargos = new ArrayList<>();
        this.pagos = new ArrayList<>();
    }
    public void agregarCargo(Cargo cargo) {
       if (cargo != null) this.cargos.add(cargo);
    }
    public void registrarPago(Pago pago) {
        if (pago != null) this.pagos.add(pago);
    }

    //Saldo es la diferencia del total menos los pagos actuales
    public int getSaldo() {
        int totalCargos = cargos.stream().mapToInt(Cargo::getValor).sum();
        int totalPagos = pagos.stream().mapToInt(Pago::getValor).sum();
        return totalCargos - totalPagos;
    }
    public void registrarCorreccionCargo(String motivo, int valorNegativo) {
        this.cargos.add(new Cargo("Corrección - " + motivo, valorNegativo));
    }
    public void registrarCorreccionPago(int valorNegativo, MedioPago medio) {
        this.pagos.add(new Pago(valorNegativo, medio));
    }
    public List<Cargo> getCargos() { return new ArrayList<>(cargos); }
    public List<Pago> getPagos() { return new ArrayList<>(pagos); }
}
