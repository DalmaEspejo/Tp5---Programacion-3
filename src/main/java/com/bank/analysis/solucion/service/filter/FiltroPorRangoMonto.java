package com.bank.analysis.solucion.service.filter;

import java.util.Objects;

import com.bank.analysis.solucion.model.Transaction;

/**
 * Estrategia de filtrado que selecciona transacciones cuyo monto
 * se encuentra dentro de un rango determinado.
 *
 * @since 1.0
 */
public class FiltroPorRangoMonto implements FiltroPredicate {

    private final double montoMin;
    private final double montoMax;

    /**
     * Crea un filtro por rango de monto.
     *
     * @param montoMin monto mínimo inclusive
     * @param montoMax monto máximo inclusive
     * @throws IllegalArgumentException si el monto mínimo es mayor al máximo
     */
    public FiltroPorRangoMonto(double montoMin, double montoMax) {

        if (montoMin > montoMax) {
            throw new IllegalArgumentException(
                    "El monto mínimo no puede ser mayor que el monto máximo"
            );
        }

        this.montoMin = montoMin;
        this.montoMax = montoMax;
    }

    /**
     * Verifica si la transacción tiene un monto dentro del rango.
     *
     * @param transaction transacción a evaluar
     * @return {@code true} si el monto está entre montoMin y montoMax
     */
   @Override
public boolean cumple(Transaction transaction) {

    Objects.requireNonNull(
            transaction,
            "La transacción no puede ser null"
    );

    double monto = transaction.getMonto();

    return monto >= montoMin && monto <= montoMax;
}
}