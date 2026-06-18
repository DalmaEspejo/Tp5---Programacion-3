package com.bank.analysis.solucion.report;

import com.bank.analysis.solucion.model.SearchResult;
import com.bank.analysis.solucion.model.Transaction;
import com.bank.analysis.solucion.service.SearchService;
import com.bank.analysis.solucion.service.SortService;
import com.bank.analysis.solucion.service.SortService.SortResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Servicio que genera reportes de rendimiento comparando algoritmos de
 * búsqueda y ordenamiento.
 *
 * @since 1.0
 */
@Service
public class PerformanceReport {

    private static final int WARMUP_SIZE = 1000;

    private final SearchService searchService;
    private final SortService sortService;

    public PerformanceReport(SearchService searchService, SortService sortService) {
        this.searchService = searchService;
        this.sortService = sortService;
    }

    public String generarReporte(List<Transaction> transacciones) {

        ejecutarWarmup(transacciones);

        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append(repeatString("=", 110)).append("\n");
        sb.append(centrar("REPORTE DE RENDIMIENTO - ANÁLISIS DE ALGORITMOS", 110)).append("\n");
        sb.append(repeatString("=", 110)).append("\n\n");

        sb.append(formatearFila(
                "Algoritmo",
                "n=100",
                "n=1,000",
                "n=10,000",
                "n=100,000",
                "Big O"
        ));

        sb.append(repeatString("-", 110)).append("\n");

        sb.append(formatearFila(
                "Búsqueda Lineal",
                medirConTamano(transacciones, 100, this::medirBusquedaPorId),
                medirConTamano(transacciones, 1000, this::medirBusquedaPorId),
                medirConTamano(transacciones, 10000, this::medirBusquedaPorId),
                medirConTamano(transacciones, 100000, this::medirBusquedaPorId),
                "O(n)"
        ));

        sb.append(formatearFila(
                "Búsqueda Binaria",
                medirConTamano(transacciones, 100, this::medirBusquedaBinaria),
                medirConTamano(transacciones, 1000, this::medirBusquedaBinaria),
                medirConTamano(transacciones, 10000, this::medirBusquedaBinaria),
                medirConTamano(transacciones, 100000, this::medirBusquedaBinaria),
                "O(log n)"
        ));

        sb.append(formatearFila(
                "Bubble Sort",
                medirConTamano(transacciones, 100, this::medirBubbleSort),
                medirConTamano(transacciones, 1000, this::medirBubbleSort),
                medirConTamano(transacciones, 10000, this::medirBubbleSort),
                "N/A",
                "O(n²)"
        ));

        sb.append(formatearFila(
                "Built-in Sort",
                medirConTamano(transacciones, 100, this::medirTimSort),
                medirConTamano(transacciones, 1000, this::medirTimSort),
                medirConTamano(transacciones, 10000, this::medirTimSort),
                medirConTamano(transacciones, 100000, this::medirTimSort),
                "O(n log n)"
        ));

        sb.append(repeatString("=", 110)).append("\n");

        return sb.toString();
    }

    private void ejecutarWarmup(List<Transaction> transacciones) {

        List<Transaction> sublista = obtenerSublista(transacciones, WARMUP_SIZE);

        for (int i = 0; i < 5; i++) {
            long idBuscado = sublista.get(sublista.size() / 2).getId();

            searchService.buscarPorId(idBuscado, sublista);

            List<Transaction> ordenada = new ArrayList<>(sublista);
            ordenada.sort(Comparator.comparingLong(Transaction::getId));

            searchService.buscarPorIdBinario(idBuscado, ordenada);

            sortService.ordenarManual(sublista, true);
            sortService.ordenarBuiltIn(sublista, true);
        }
    }

    private List<Transaction> obtenerSublista(List<Transaction> fuente, int tamanio) {

        if (fuente.size() >= tamanio) {
            return new ArrayList<>(fuente.subList(0, tamanio));
        }

        List<Transaction> lista = new ArrayList<>(fuente);
        Random random = new Random(42);

        for (int i = lista.size(); i < tamanio; i++) {
            lista.add(
                    com.bank.analysis.solucion.config.TransactionDataGenerator
                            .generarAleatoria(random, 5000 + i)
            );
        }

        return lista;
    }

    private String medirBusquedaPorId(List<Transaction> sublista) {

        long idBuscado = sublista.get(sublista.size() - 1).getId();

        SearchResult result =
                searchService.buscarPorId(idBuscado, sublista);

        return formatNanos(result.tiempoBusquedaNs());
    }

    private String medirBusquedaBinaria(List<Transaction> sublista) {

        List<Transaction> ordenada = new ArrayList<>(sublista);

        ordenada.sort(Comparator.comparingLong(Transaction::getId));

        long idBuscado = ordenada.get(ordenada.size() - 1).getId();

        SearchResult result =
                searchService.buscarPorIdBinario(idBuscado, ordenada);

        return formatNanos(result.tiempoBusquedaNs());
    }

    private String medirBubbleSort(List<Transaction> sublista) {

        SortResult result =
                sortService.ordenarManual(sublista, true);

        return formatNanos(result.tiempoOrdenamientoNs());
    }

    private String medirTimSort(List<Transaction> sublista) {

        SortResult result =
                sortService.ordenarBuiltIn(sublista, true);

        return formatNanos(result.tiempoOrdenamientoNs());
    }

    private String medirConTamano(
            List<Transaction> fuente,
            int tamanio,
            java.util.function.Function<List<Transaction>, String> operacion
    ) {
        try {
            List<Transaction> sublista = obtenerSublista(fuente, tamanio);
            return operacion.apply(sublista);
        } catch (RuntimeException e) {
            return "ERROR";
        }
    }

    public static String formatNanos(long nanos) {

        if (nanos < 1_000) {
            return String.format("%,d ns", nanos);
        }

        if (nanos < 1_000_000) {
            return String.format("%,d us", nanos / 1_000);
        }

        if (nanos < 1_000_000_000) {
            return String.format("%,d ms", nanos / 1_000_000);
        }

        return String.format("%,.2f s", nanos / 1_000_000_000.0);
    }

    private String formatearFila(
            String algoritmo,
            String v1,
            String v2,
            String v3,
            String v4,
            String bigO
    ) {
        return String.format(
                "%-22s %-16s %-16s %-16s %-16s %-16s%n",
                algoritmo,
                v1,
                v2,
                v3,
                v4,
                bigO
        );
    }

    private String centrar(String texto, int ancho) {

        int padding = (ancho - texto.length()) / 2;

        if (padding <= 0) {
            return texto;
        }

        return repeatString(" ", padding) + texto;
    }

    private String repeatString(String texto, int veces) {
        return texto.repeat(veces);
    }
}