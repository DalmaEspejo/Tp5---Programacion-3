package com.bank.analysis.solucion.service;

import com.bank.analysis.solucion.model.SearchResult;
import com.bank.analysis.solucion.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Implementación del servicio de búsqueda de transacciones.
 *Esta clase cumple SRP porque solo contiene lógica de búsqueda.
 * @since 1.0
 */
@Service
public class SearchServiceImpl implements SearchService {

    /**
     * Busca una transacción por ID usando búsqueda lineal.
     *
     * @param id identificador de la transacción a buscar
     * @param transacciones lista de transacciones donde se realizará la búsqueda
     * @return resultado con tiempo de búsqueda y comparaciones realizadas
     * @throws NullPointerException si la lista de transacciones es null
     * @since 1.0
     */
    @Override
    public SearchResult buscarPorId(
        long id, 
        List<Transaction> transacciones) {

        Objects.requireNonNull(
                transacciones,
                "La lista de transacciones no puede ser null"
        );

        int comparaciones = 0;
        long inicio = System.nanoTime();

        for (Transaction transaction : transacciones) {

            comparaciones++;

            if (transaction.getId() == id) {

                long fin = System.nanoTime();

                return new SearchResult(
                        transaction,
                        fin - inicio,
                        comparaciones
                );
            }
        }

        long fin = System.nanoTime();

        return new SearchResult(
                null,
                fin - inicio,
                comparaciones
        );
    }

    /**
     * Busca una transacción por ID usando búsqueda binaria.
     *
     * @param id identificador de la transacción a buscar
     * @param transacciones lista ordenada por ID ascendente
     * @return resultado con tiempo de búsqueda y comparaciones realizadas
     * @throws NullPointerException si la lista de transacciones es null
     * @since 1.0
     */
    @Override
    public SearchResult buscarPorIdBinario(
        long id, 
        List<Transaction> transacciones) {

        Objects.requireNonNull(
                transacciones,
                "La lista de transacciones no puede ser null"
        );

        int comparaciones = 0;
        long inicio = System.nanoTime();

        int izquierda = 0;
        int derecha = transacciones.size() - 1;

        while (izquierda <= derecha) {

            int medio = izquierda + (derecha - izquierda) / 2;
            Transaction actual = transacciones.get(medio);

            comparaciones++;

            if (actual.getId() == id) {

                long fin = System.nanoTime();

                return new SearchResult(
                        actual,
                        fin - inicio,
                        comparaciones
                );
            }

            if (actual.getId() < id) {
                izquierda = medio + 1;
            } else {
                derecha = medio - 1;
            }
        }

        long fin = System.nanoTime();

        return new SearchResult(
                null,
                fin - inicio,
                comparaciones
        );
    }

    /**
     * Busca transacciones por rango de monto usando recorrido lineal.
     *
     * @param montoMin monto mínimo inclusive
     * @param montoMax monto máximo inclusive
     * @param transacciones lista de transacciones a filtrar
     * @return lista con las transacciones dentro del rango
     * @throws NullPointerException si la lista de transacciones es null
     * @since 1.0
     */
    @Override
    public List<Transaction> buscarPorMonto(
            double montoMin,
            double montoMax,
            List<Transaction> transacciones
    ) {

        Objects.requireNonNull(
                transacciones,
                "La lista de transacciones no puede ser null"
        );

        List<Transaction> resultado = new ArrayList<>();

        for (Transaction transaction : transacciones) {

            if (transaction.getMonto() >= montoMin
                    && transaction.getMonto() <= montoMax) {

                resultado.add(transaction);
            }
        }

        return resultado;
    }
}