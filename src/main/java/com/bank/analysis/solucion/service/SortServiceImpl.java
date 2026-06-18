package com.bank.analysis.solucion.service;

import com.bank.analysis.solucion.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Implementación del servicio de ordenamiento de transacciones.
 *
 * <p>
 * Esta clase cumple SRP porque solo contiene lógica de ordenamiento.
 *
 * @since 1.0
 */
@Service
public class SortServiceImpl implements SortService {

    /**
     * Ordena una lista de transacciones por monto usando Bubble Sort.
     *
     * @param transacciones lista de transacciones a ordenar
     * @param ascendente {@code true} para menor a mayor, {@code false} para mayor a menor
     * @return resultado del ordenamiento con lista ordenada y métricas
     * @throws NullPointerException si la lista es null
     * @since 1.0
     */
    @Override
    public SortResult ordenarManual(List<Transaction> transacciones, boolean ascendente) {

        Objects.requireNonNull(
                transacciones,
                "La lista de transacciones no puede ser null"
        );

        List<Transaction> ordenada = new ArrayList<>(transacciones);

        long comparaciones = 0;
        long intercambios = 0;

        long inicio = System.nanoTime();

        for (int i = 0; i < ordenada.size() - 1; i++) {

            boolean huboIntercambio = false;

            for (int j = 0; j < ordenada.size() - 1 - i; j++) {

                comparaciones++;

                Transaction actual = ordenada.get(j);
                Transaction siguiente = ordenada.get(j + 1);

                boolean debeIntercambiar = ascendente
                        ? actual.getMonto() > siguiente.getMonto()
                        : actual.getMonto() < siguiente.getMonto();

                if (debeIntercambiar) {
                    ordenada.set(j, siguiente);
                    ordenada.set(j + 1, actual);

                    intercambios++;
                    huboIntercambio = true;
                }
            }

            if (!huboIntercambio) {
                break;
            }
        }

        long fin = System.nanoTime();

        return new SortResult(
                ordenada,
                comparaciones,
                intercambios,
                fin - inicio
        );
    }

    /**
     * Ordena una lista de transacciones por monto usando List.sort().
     *
     * @param transacciones lista de transacciones a ordenar
     * @param ascendente {@code true} para menor a mayor, {@code false} para mayor a menor
     * @return resultado del ordenamiento con lista ordenada y métricas
     * @throws NullPointerException si la lista es null
     * @since 1.0
     */
    @Override
    public SortResult ordenarBuiltIn(List<Transaction> transacciones, boolean ascendente) {

        Objects.requireNonNull(
                transacciones,
                "La lista de transacciones no puede ser null"
        );

        List<Transaction> ordenada = new ArrayList<>(transacciones);

        Comparator<Transaction> comparador =
                Comparator.comparingDouble(Transaction::getMonto);

        if (!ascendente) {
            comparador = comparador.reversed();
        }

        long inicio = System.nanoTime();

        ordenada.sort(comparador);

        long fin = System.nanoTime();

        return new SortResult(
                ordenada,
                0,
                0,
                fin - inicio
        );
    }
}