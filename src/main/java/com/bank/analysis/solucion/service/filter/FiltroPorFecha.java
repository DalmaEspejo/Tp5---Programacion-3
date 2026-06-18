package com.bank.analysis.solucion.service.filter;

import com.bank.analysis.solucion.model.Transaction;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Estrategia de filtrado que selecciona transacciones cuya fecha
 * se encuentra dentro de un rango determinado.
 *
 * @since 1.0
 */
public class FiltroPorFecha implements FiltroPredicate {

    private final LocalDate fechaDesde;
    private final LocalDate fechaHasta;

    /**
     * Crea un filtro por rango de fechas.
     *
     * @param fechaDesde fecha mínima inclusive
     * @param fechaHasta fecha máxima inclusive
     * @throws NullPointerException si alguna fecha es null
     * @throws IllegalArgumentException si fechaDesde es posterior a fechaHasta
     */
    public FiltroPorFecha(LocalDate fechaDesde, LocalDate fechaHasta) {

        this.fechaDesde = Objects.requireNonNull(
                fechaDesde,
                "La fecha desde no puede ser null"
        );

        this.fechaHasta = Objects.requireNonNull(
                fechaHasta,
                "La fecha hasta no puede ser null"
        );

        if (fechaDesde.isAfter(fechaHasta)) {
            throw new IllegalArgumentException(
                    "La fecha desde no puede ser posterior a la fecha hasta"
            );
        }
    }

    /**
     * Verifica si la fecha de la transacción está dentro del rango.
     *
     * @param transaction transacción a evaluar
     * @return {@code true} si la fecha está dentro del rango indicado
     */
    @Override
    public boolean cumple(Transaction transaction) {
        return !transaction.getFecha().isBefore(fechaDesde)
                && !transaction.getFecha().isAfter(fechaHasta);
    }
}