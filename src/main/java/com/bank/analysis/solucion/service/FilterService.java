package com.bank.analysis.solucion.service;

import com.bank.analysis.solucion.model.Transaction;
import com.bank.analysis.solucion.model.TransactionType;
import com.bank.analysis.solucion.service.filter.FiltroPredicate;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio de filtrado de transacciones.
 *
 * <p>
 * Aplica el patrón Strategy mediante filtros que implementan
 * {@link FiltroPredicate}.
 *
 * @since 1.0
 */
public interface FilterService {

    /**
     * Aplica una lista de filtros sobre las transacciones.
     *
     * @param transacciones lista de transacciones a filtrar
     * @param filtros lista de filtros a aplicar
     * @return lista filtrada
     * @since 1.0
     */
    List<Transaction> filtrar(
            List<Transaction> transacciones,
            List<FiltroPredicate> filtros
    );

    List<Transaction> filtrarPorTipo(
            List<Transaction> transacciones,
            TransactionType tipo
    );

    List<Transaction> filtrarPorRangoMonto(
            List<Transaction> transacciones,
            double min,
            double max
    );

    List<Transaction> filtrarPorFecha(
            List<Transaction> transacciones,
            LocalDate desde,
            LocalDate hasta
    );

    List<Transaction> filtrarCombinado(
            List<Transaction> transacciones,
            TransactionType tipo,
            Double montoMin,
            Double montoMax,
            LocalDate fechaDesde,
            LocalDate fechaHasta
    );
}