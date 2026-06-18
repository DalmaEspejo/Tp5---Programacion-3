package com.bank.analysis.solucion.service;

import com.bank.analysis.solucion.model.Transaction;

import java.util.List;

/**
 * Servicio de ordenamiento de transacciones.
 *
 * <p>
 * Define operaciones para comparar Bubble Sort O(n²) contra el ordenamiento
 * nativo de Java, basado en TimSort O(n log n).
 *
 * @since 1.0
 */
public interface SortService {

    /**
     * Ordena una lista de transacciones por monto usando Bubble Sort.
     *
     * @param transacciones lista de transacciones a ordenar
     * @param ascendente {@code true} para menor a mayor, {@code false} para mayor a menor
     * @return resultado del ordenamiento con lista ordenada y métricas
     * @throws NullPointerException si la lista es null
     * @since 1.0
     */
    SortResult ordenarManual(List<Transaction> transacciones, boolean ascendente);

    /**
     * Ordena una lista de transacciones por monto usando List.sort().
     *
     * @param transacciones lista de transacciones a ordenar
     * @param ascendente {@code true} para menor a mayor, {@code false} para mayor a menor
     * @return resultado del ordenamiento con lista ordenada y métricas
     * @throws NullPointerException si la lista es null
     * @since 1.0
     */
    SortResult ordenarBuiltIn(List<Transaction> transacciones, boolean ascendente);

    /**
     * Resultado inmutable de una operación de ordenamiento.
     *
     * @param ordenada lista ordenada resultante
     * @param comparaciones cantidad de comparaciones realizadas
     * @param intercambios cantidad de intercambios realizados
     * @param tiempoOrdenamientoNs tiempo de ordenamiento en nanosegundos
     *
     * @since 1.0
     */
    record SortResult(
            List<Transaction> ordenada,
            long comparaciones,
            long intercambios,
            long tiempoOrdenamientoNs) {

        /**
         * Constructor canónico con validaciones.
         *
         * @throws IllegalArgumentException si alguna métrica es negativa
         * @throws NullPointerException si la lista ordenada es null
         */
        public SortResult {
            if (ordenada == null) {
                throw new NullPointerException("La lista ordenada no puede ser null");
            }

            if (comparaciones < 0) {
                throw new IllegalArgumentException("Las comparaciones no pueden ser negativas");
            }

            if (intercambios < 0) {
                throw new IllegalArgumentException("Los intercambios no pueden ser negativos");
            }

            if (tiempoOrdenamientoNs < 0) {
                throw new IllegalArgumentException("El tiempo no puede ser negativo");
            }
        }
    }
}