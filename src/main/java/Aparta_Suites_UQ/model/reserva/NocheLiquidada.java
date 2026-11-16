package Aparta_Suites_UQ.model.reserva;
import Aparta_Suites_UQ.model.alojamiento.TipoTemporada;
import java.io.Serializable;
import java.time.LocalDate;
public record NocheLiquidada (LocalDate fecha, TipoTemporada temporada, int tarifa, int personasQuePagan, int subtotal)implements Serializable {
}
