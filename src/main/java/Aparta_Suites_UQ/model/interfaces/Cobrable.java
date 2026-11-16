package Aparta_Suites_UQ.model.interfaces;

public interface Cobrable {
    double getTotalCargos();
    double getTotalPagos();
    default double getSaldo() {
        return getTotalCargos() - getTotalPagos();
    }
}
