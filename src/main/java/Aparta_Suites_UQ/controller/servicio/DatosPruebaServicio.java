package Aparta_Suites_UQ.controller.servicio;

import Aparta_Suites_UQ.model.Empresa;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.EstadoOperativo;
import Aparta_Suites_UQ.model.huesped.Huesped;
import Aparta_Suites_UQ.model.huesped.Ocupante;
import Aparta_Suites_UQ.model.reserva.CanalReserva;
import Aparta_Suites_UQ.model.reserva.EstadoReserva;
import Aparta_Suites_UQ.model.reserva.Estancia;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.model.transacciones.Cargo;
import Aparta_Suites_UQ.model.transacciones.MedioPago;
import Aparta_Suites_UQ.model.transacciones.Pago;

import java.time.LocalDate;
import java.util.List;
public class DatosPruebaServicio {

    public static void cargar() {

        try {
            LocalDate hoy = LocalDate.now();

            Apartamento apto101 = new Apartamento("Apto 101 - Suite Presidencial", 2, 4, "Vista al jardín, Balcón, Jacuzzi", 150000.0, EstadoOperativo.PREPARADO);
            Apartamento apto102 = new Apartamento("Apto 102 - Junior Suite", 1, 2, "Cocina equipada, Aire acondicionado", 100000.0, EstadoOperativo.OCUPADO);
            Apartamento apto201 = new Apartamento("Apto 201 - Estándar Doble", 2, 3, "Vista a la ciudad, Wifi de alta velocidad", 120000.0, EstadoOperativo.PREPARADO);
            Apartamento apto202 = new Apartamento("Apto 202 - Estándar Sencilla", 1, 2, "Smart TV, Escritorio", 90000.0, EstadoOperativo.OCUPADO);
            Apartamento apto301 = new Apartamento("Apto 301 - Penthouse Suite", 3, 6, "Terraza privada, Asador, Vista panorámica", 250000.0, EstadoOperativo.EN_PREPARACION);
            Empresa.getInstance().getListaApartamentos().addAll(List.of(apto101,apto102,apto201,apto202,apto301));

            Huesped h1 = new Huesped("Carlos Mendoza", "1094123456", LocalDate.of(1990, 5, 12), "3101234567", "carlos@email.com", null);
            Huesped h2 = new Huesped("Ana María Gómez", "1094987654", LocalDate.of(1988, 11, 23), "3159876543", "ana@email.com", null);
            Huesped h3 = new Huesped("Juan David Pérez", "1094111222", LocalDate.of(1995, 3, 15), "3001112233", "juan@email.com", null);
            Huesped h4 = new Huesped("Laura Restrepo", "1094333444", LocalDate.of(1992, 8, 30), "3123334455", "laura@email.com", null);
            Huesped h5 = new Huesped("Santiago Osorio", "1094555666", LocalDate.of(1985, 1, 10), "3185556677", "santi@email.com", null);
            Huesped h6 = new Huesped("Valentina Ríos", "1094777888", LocalDate.of(1998, 7, 19), "3117778899", "vale@email.com", null);
            Huesped h7 = new Huesped("Andrés Felipe Jaramillo", "1094999000", LocalDate.of(1987, 12, 5), "3149990011", "andres@email.com", null);
            Huesped h8 = new Huesped("Mariana Aristizábal", "1094222333", LocalDate.of(1994, 4, 25), "3162223344", "mariana@email.com", null);
            Huesped h9 = new Huesped("Camilo Torres", "1094444555", LocalDate.of(1991, 9, 8), "3174445566", "camilo@email.com", null);
            Huesped h10 = new Huesped("Daniela Castaño", "1094666777", LocalDate.of(1996, 6, 14), "3196667788", "daniela@email.com", null);
            Empresa.getInstance().getListaHuespedes().addAll(List.of(h1,h2,h3,h4,h5,h6,h7,h8,h9,h10));
            List<Ocupante> ocu1 = List.of(new Ocupante("Sonia Mendoza", LocalDate.of(1992, 3, 10)));
            List<Ocupante> ocu2 = List.of(new Ocupante("Mateo Gómez", LocalDate.of(2015, 8, 20)));
            List<Ocupante> ocu5 = List.of(new Ocupante("Felipe Osorio", LocalDate.of(2010, 11, 4)), new Ocupante("Lucía Osorio", LocalDate.of(1987, 2, 18)));
            Reserva res1 = new Reserva(new Estancia(hoy, hoy.plusDays(3)), CanalReserva.DIRECTA, h1, apto101, ocu1, 450000.0);
            res1.setEstado(EstadoReserva.CONFIRMADA);
            res1.getFolio().agregarCargo(new Cargo("Alojamiento 3 Noches (Apto 101)", 450000.0));
            res1.getFolio().registrarPago(new Pago(150000.0, MedioPago.EFECTIVO));


            Reserva res2 = new Reserva(new Estancia(hoy.minusDays(2), hoy.plusDays(1)), CanalReserva.DIRECTA, h2, apto102, ocu2, 300000.0);
            res2.setEstado(EstadoReserva.EN_CURSO);
            res2.getFolio().agregarCargo(new Cargo("Alojamiento 3 Noches (Apto 102)", 300000.0));
            res2.getFolio().agregarCargo(new Cargo("Servicio Restaurante - Cena", 80000.0));
            res2.getFolio().registrarPago(new Pago(380000.0, MedioPago.TARJETA));

            Reserva res3 = new Reserva(new Estancia(hoy.plusDays(2), hoy.plusDays(5)), CanalReserva.DIRECTA, h3, apto201, ocu5, 360000.0);
            res3.setEstado(EstadoReserva.PENDIENTE);
            res3.getFolio().agregarCargo(new Cargo("Alojamiento 3 Noches (Apto 201)", 360000.0));

            Reserva res4 = new Reserva(new Estancia(hoy.minusDays(1), hoy.plusDays(2)), CanalReserva.DIRECTA, h4, apto202, ocu1, 270000.0);
            res4.setEstado(EstadoReserva.EN_CURSO);
            res4.getFolio().agregarCargo(new Cargo("Alojamiento 3 Noches (Apto 202)", 270000.0));
            res4.getFolio().agregarCargo(new Cargo("Minibar - Bebidas", 45000.0));
            res4.getFolio().agregarCargo(new Cargo("Servicio Lavandería", 25000.0));
            res4.getFolio().registrarPago(new Pago(100000.0, MedioPago.TRANSFERENCIA));


            Reserva res5 = new Reserva(new Estancia(hoy, hoy.plusDays(4)), CanalReserva.DIRECTA, h5, apto301, ocu2, 1000000.0);
            res5.setEstado(EstadoReserva.CONFIRMADA);
            res5.getFolio().agregarCargo(new Cargo("Alojamiento 4 Noches (Penthouse)", 1000000.0));
            res5.getFolio().registrarPago(new Pago(1000000.0, MedioPago.TARJETA));


            Reserva res6 = new Reserva(new Estancia(hoy.minusDays(5), hoy.minusDays(1)), CanalReserva.DIRECTA, h6, apto101, ocu5, 600000.0);
            res6.setEstado(EstadoReserva.FINALIZADA);
            res6.getFolio().agregarCargo(new Cargo("Alojamiento 4 Noches (Apto 101)", 600000.0));
            res6.getFolio().registrarPago(new Pago(600000.0, MedioPago.EFECTIVO));


            Reserva res7 = new Reserva(new Estancia(hoy.minusDays(1), hoy.plusDays(1)), CanalReserva.DIRECTA, h7, apto201, ocu1, 240000.0);
            res7.setEstado(EstadoReserva.NO_SHOW);
            res7.getFolio().agregarCargo(new Cargo("Penalidad No-Show (Alojamiento)", 240000.0));


            Reserva res8 = new Reserva(new Estancia(hoy.plusDays(5), hoy.plusDays(7)), CanalReserva.DIRECTA, h8, apto102, ocu1, 200000.0);
            res8.setEstado(EstadoReserva.CONFIRMADA);
            res8.getFolio().agregarCargo(new Cargo("Alojamiento 2 Noches (Apto 102)", 200000.0));
            res8.getFolio().agregarCargo(new Cargo("Cargo Erróneo Parqueadero", 30000.0));
            res8.getFolio().agregarCargo(new Cargo("Corrección - Parqueadero", -30000.0));
            res8.getFolio().registrarPago(new Pago(100000.0, MedioPago.TRANSFERENCIA));


            Reserva res9 = new Reserva(new Estancia(hoy.plusDays(10), hoy.plusDays(12)), CanalReserva.DIRECTA, h9, apto202, ocu1, 180000.0);
            res9.setEstado(EstadoReserva.CANCELADA);
            res9.getFolio().agregarCargo(new Cargo("Alojamiento 2 Noches (Apto 202)", 180000.0));
            res9.getFolio().agregarCargo(new Cargo("Anulación por cancelación de reserva", -180000.0));


            Reserva res10 = new Reserva(new Estancia(hoy.plusDays(6),hoy.plusDays(10)), CanalReserva.DIRECTA, h10, apto301, ocu5, 500000.0);
            res10.setEstado(EstadoReserva.CONFIRMADA);
            res10.getFolio().agregarCargo(new Cargo("Alojamiento 2 Noches (Penthouse)", 500000.0));
            res10.getFolio().registrarPago(new Pago(200000.0, MedioPago.TARJETA));
            res10.getFolio().registrarPago(new Pago(150000.0, MedioPago.EFECTIVO));


            Empresa.getInstance().getListaReservas().addAll(List.of(res1, res2, res3, res4, res5, res6, res7, res8, res9, res10));

        } catch (Exception e) {
            System.err.println("Error al cargar datos de prueba: " + e.getMessage());
        }
    }
}
