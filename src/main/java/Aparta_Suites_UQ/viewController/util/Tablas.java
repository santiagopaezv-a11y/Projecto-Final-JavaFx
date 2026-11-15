package Aparta_Suites_UQ.viewController.util;

import Aparta_Suites_UQ.controller.Informe;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;

/**
 * Utilidades visuales para armar tablas y combos sin repetir código en cada pantalla.
 */
public final class Tablas {

    private Tablas() {
    }

    /** Agrega una columna de texto a la tabla. */
    public static <T> void columna(TableView<T> tabla, String titulo, Function<T, String> valor) {
        TableColumn<T, String> columna = new TableColumn<>(titulo);
        columna.setCellValueFactory(celda -> new SimpleStringProperty(valor.apply(celda.getValue())));
        tabla.getColumns().add(columna);
    }

    /** Muestra el nombre del apartamento en el combo (en lugar de su toString). */
    public static void mostrarNombres(ComboBox<Apartamento> combo) {
        combo.setConverter(new StringConverter<Apartamento>() {
            @Override
            public String toString(Apartamento apartamento) {
                return apartamento == null ? "" : apartamento.getNombre();
            }

            @Override
            public Apartamento fromString(String texto) {
                return null;
            }
        });
    }

    /** Arma las columnas y filas de un informe (las columnas dependen del informe elegido). */
    public static void cargarInforme(TableView<List<String>> tabla, Informe informe) {
        tabla.getColumns().clear();
        for (int i = 0; i < informe.columnas().size(); i++) {
            final int indice = i;
            TableColumn<List<String>, String> columna = new TableColumn<>(informe.columnas().get(i));
            columna.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().get(indice)));
            tabla.getColumns().add(columna);
        }
        tabla.setItems(FXCollections.observableArrayList(informe.filas()));
    }

    public static String dinero(double valor) {
        return String.format(Locale.of("es", "CO"), "$ %,.0f", valor);
    }
}
