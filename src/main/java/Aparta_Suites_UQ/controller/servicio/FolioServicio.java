package Aparta_Suites_UQ.controller.servicio;

import Aparta_Suites_UQ.model.reserva.Folio;
import Aparta_Suites_UQ.model.transacciones.Cargo;
import Aparta_Suites_UQ.model.transacciones.MedioPago;
import Aparta_Suites_UQ.model.transacciones.Pago;

public class FolioServicio {

    private static FolioServicio instance;

    private FolioServicio() {
    }

    public static FolioServicio getInstance() {
        if (instance == null) {
            instance = new FolioServicio();
        }
        return instance;
    }

    public void agregarCargo(Folio folio, String concepto, String valorTxt) {
        double valor;
        try {
            valor = Double.parseDouble(valorTxt.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("El valor del cargo debe ser numérico.");
        }
        String conceptoLimpio = concepto == null ? "" : concepto.trim();
        if (conceptoLimpio.isEmpty() || valor <= 0) {
            throw new IllegalArgumentException("Ingrese un concepto válido y un valor mayor a cero.");
        }
        folio.agregarCargo(new Cargo(conceptoLimpio, valor));
    }

    public void registrarPago(Folio folio, MedioPago medio, String valorTxt) {
        double valor;
        try {
            valor = Double.parseDouble(valorTxt.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("El valor del pago debe ser numérico.");
        }
        if (medio == null || valor <= 0) {
            throw new IllegalArgumentException("Seleccione un medio de pago y un valor mayor a cero.");
        }
        folio.registrarPago(new Pago(valor, medio));
    }

    public void registrarCorreccionCargo(Folio folio, String motivo, String valorTxt) {
        try {
            int valorNegativo = Integer.parseInt(valorTxt.trim());
            if (valorNegativo > 0) {
                valorNegativo = -valorNegativo;
            }
            folio.registrarCorreccionCargo(motivo == null ? "" : motivo.trim(), valorNegativo);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al registrar corrección de cargo: " + e.getMessage());
        }
    }

    public void registrarCorreccionPago(Folio folio, MedioPago medio, String valorTxt) {
        int valorInput;
        try {
            valorInput = Integer.parseInt(valorTxt.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("El valor de la corrección debe ser un número entero.");
        }
        if (medio == null || valorInput <= 0) {
            throw new IllegalArgumentException(
                    "Seleccione un medio de pago y digite un valor positivo para corregir.");
        }
        try {
            folio.registrarCorreccionPago(-Math.abs(valorInput), medio);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al registrar corrección de pago: " + e.getMessage());
        }
    }
}
