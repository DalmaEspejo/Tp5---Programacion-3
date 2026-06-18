package com.bank.analysis.solucion.service.filter;

import com.bank.analysis.solucion.model.Transaction;

/**
 * Estrategia de filtrado para transacciones.
 *
 * <p>
 * Esta interfaz representa un criterio de filtrado que puede aplicarse a una
 * transacción. Forma parte del patrón Strategy, permitiendo agregar nuevos
 * filtros sin modificar el código existente.
 *
 * @since 1.0
 */
@FunctionalInterface
public interface FiltroPredicate {

    /**
     * Determina si una transacción cumple el criterio de filtrado.
     *
     * @param transaction transacción a evaluar
     * @return {@code true} si la transacción cumple el criterio;
     *         {@code false} en caso contrario.
     */
    boolean cumple(Transaction transaction);

}