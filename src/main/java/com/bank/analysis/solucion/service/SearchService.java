package com.bank.analysis.solucion.service;

import java.util.List;

import com.bank.analysis.solucion.model.SearchResult;
import com.bank.analysis.solucion.model.Transaction;

/**
 * Servicio de búsqueda de transacciones.
 *
 * @since 1.0
 */
public interface SearchService {

    SearchResult buscarPorId(long id, List<Transaction> transacciones);

    SearchResult buscarPorIdBinario(long id, List<Transaction> transacciones);

    List<Transaction> buscarPorMonto(
            double montoMin,
            double montoMax,
            List<Transaction> transacciones
    );
}