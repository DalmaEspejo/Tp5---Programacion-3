package com.bank.analysis.solucion.service.filter;

import java.util.Objects;

import com.bank.analysis.solucion.model.Transaction;
import com.bank.analysis.solucion.model.TransactionType;

/**
 * Estrategia de filtrado que selecciona únicamente las transacciones
 * de un tipo determinado.
 *
 * <p>
 * Forma parte del patrón Strategy, permitiendo agregar nuevos filtros
 * sin modificar el código existente.
 *
 * @since 1.0
 */
public class FiltroPorTipo implements FiltroPredicate {

    /** Tipo de transacción que se desea filtrar. */
    private final TransactionType tipo;

    /**
     * Crea un filtro por tipo de transacción.
     *
     * @param tipo tipo de transacción que debe cumplir la transacción
     * @throws NullPointerException si el tipo es nulo
     */
    public FiltroPorTipo(TransactionType tipo) {
        this.tipo = Objects.requireNonNull(
                tipo,
                "El tipo de transacción no puede ser null"
        );
    }

    /**
     * Verifica si una transacción pertenece al tipo indicado.
     *
     * @param transaction transacción a evaluar
     * @return {@code true} si la transacción es del tipo indicado;
     *         {@code false} en caso contrario.
     */
    @Override
    public boolean cumple(Transaction transaction) {
        return transaction.getTipo() == tipo;
    }
}