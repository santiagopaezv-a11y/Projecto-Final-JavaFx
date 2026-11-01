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
       if (cargo == null) {
       throw  new NullPointerException("El cargo no puede ser nulo.");
       }
       this.cargos.add(cargo);
    }
    public void registrarPago(Pago pago) {
        if (pago == null) {
        throw  new NullPointerException("El pago no puede ser nulo.");
        }
        this.pagos.add(pago);
    }

    //Saldo es la diferencia del total menos los pagos actuales

    public int getSaldo() {
        int totalCargos = cargos.stream().mapToInt(Cargo::getValor).sum();
        int totalPagos = pagos.stream().mapToInt(Pago::getValor).sum();
        return totalCargos - totalPagos;
    }
    public int getTotalCargos() {
        return  cargos.stream().mapToInt(Cargo::getValor).sum();
    }
    public int getTotalPagos() {
        return  pagos.stream().mapToInt(Pago::getValor).sum();
    }

    public void registrarCorreccionCargo(String motivo, int valorNegativo) {
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo no puede ser nulo.");
        }
        if (valorNegativo > 0){
            throw new IllegalArgumentException("El valor de la correccion no puede ser mayor a 0");
        }
        this.cargos.add(new Cargo("Corrección - " + motivo.trim(), valorNegativo));
    }
    public void registrarCorreccionPago(int valorNegativo, MedioPago medio) {
       if (medio == null) {
           throw new IllegalArgumentException("El medio de pago no puede ser nulo.");
       }
       if (valorNegativo > 0){
           throw new IllegalArgumentException("El valor de la correccion no puede ser mayor a 0");
       }
        this.pagos.add(new Pago(valorNegativo, medio));
    }
    public List<Cargo> getCargos() { return new ArrayList<>(cargos); }
    public List<Pago> getPagos() { return new ArrayList<>(pagos); }
}
