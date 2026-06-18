package com.bank.analysis.solucion;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bank.analysis.solucion.config.TransactionDataGenerator;
import com.bank.analysis.solucion.model.SearchResult;
import com.bank.analysis.solucion.model.Transaction;
import com.bank.analysis.solucion.model.TransactionType;
import com.bank.analysis.solucion.report.PerformanceReport;
import com.bank.analysis.solucion.service.FilterService;
import com.bank.analysis.solucion.service.SearchService;
import com.bank.analysis.solucion.service.SortService;
import com.bank.analysis.solucion.service.SortService.SortResult;

@SpringBootApplication
public class MainRefactorizado implements CommandLineRunner {

    private final TransactionDataGenerator generador;
    private final PerformanceReport performanceReport;
    private final FilterService filterService;
    private final SearchService searchService;
    private final SortService sortService;

    public MainRefactorizado(
            TransactionDataGenerator generador,
            PerformanceReport performanceReport,
            FilterService filterService,
            SearchService searchService,
            SortService sortService) {

        this.generador = generador;
        this.performanceReport = performanceReport;
        this.filterService = filterService;
        this.searchService = searchService;
        this.sortService = sortService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainRefactorizado.class, args);
    }

    @Override
    public void run(String... args) {

        System.out.println("==========================================");
        System.out.println(" SISTEMA DE ANÁLISIS DE TRANSACCIONES");
        System.out.println(" VERSIÓN REFACTORIZADA (SOLID + SPRING)");
        System.out.println("==========================================");

        List<Transaction> transacciones = generador.generarDatosPredecibles(1000);

        System.out.println("Transacciones generadas: " + transacciones.size());

        demostrarBusqueda(transacciones);
        demostrarOrdenamiento(transacciones);
        demostrarFiltrado(transacciones);

        System.out.println(performanceReport.generarReporte(transacciones));
    }

    private void demostrarBusqueda(List<Transaction> transacciones) {

        System.out.println("\n==========================================");
        System.out.println(" DEMOSTRACIÓN DE BÚSQUEDA");
        System.out.println("==========================================");

        long idBuscado = transacciones.get(500).getId();

        SearchResult resultadoLineal = searchService.buscarPorId(idBuscado, transacciones);

        System.out.println("\nID buscado: " + idBuscado);
        System.out.println("\n--- Búsqueda Lineal ---");
        System.out.println("Encontrada....: " + resultadoLineal.encontrada());
        System.out.println("Comparaciones.: " + resultadoLineal.comparacionesRealizadas());
        System.out.println("Tiempo........: "
                + PerformanceReport.formatNanos(resultadoLineal.tiempoBusquedaNs()));
        System.out.println("Transacción encontrada:");
        imprimirTransaccion(resultadoLineal.transaction());

        List<Transaction> ordenadasPorId = transacciones.stream()
                .sorted(Comparator.comparingLong(Transaction::getId))
                .toList();

        SearchResult resultadoBinaria = searchService.buscarPorIdBinario(idBuscado, ordenadasPorId);

        System.out.println("\n--- Búsqueda Binaria ---");
        System.out.println("Encontrada....: " + resultadoBinaria.encontrada());
        System.out.println("Comparaciones.: " + resultadoBinaria.comparacionesRealizadas());
        System.out.println("Tiempo........: "
                + PerformanceReport.formatNanos(resultadoBinaria.tiempoBusquedaNs()));
        System.out.println("Transacción encontrada:");
        imprimirTransaccion(resultadoBinaria.transaction());
    }

    private void demostrarOrdenamiento(List<Transaction> transacciones) {

        System.out.println("\n==========================================");
        System.out.println(" DEMOSTRACIÓN DE ORDENAMIENTO");
        System.out.println("==========================================");

        List<Transaction> sublista = transacciones.subList(
                0,
                Math.min(100, transacciones.size())
        );

        SortResult resultadoBubble = sortService.ordenarManual(sublista, false);

        System.out.println("\n--- Bubble Sort descendente ---");
        System.out.println("Cantidad.......: " + sublista.size());
        System.out.println("Comparaciones.: " + resultadoBubble.comparaciones());
        System.out.println("Intercambios..: " + resultadoBubble.intercambios());
        System.out.println("Tiempo........: "
                + PerformanceReport.formatNanos(resultadoBubble.tiempoOrdenamientoNs()));

        System.out.println("\nPrimeras 5 transacciones ordenadas:");
        resultadoBubble.ordenada().stream()
                .limit(5)
                .forEach(this::imprimirTransaccionSeparada);

        SortResult resultadoTim = sortService.ordenarBuiltIn(transacciones, true);

        System.out.println("\n--- Built-in Sort ascendente ---");
        System.out.println("Cantidad.......: " + transacciones.size());
        System.out.println("Tiempo........: "
                + PerformanceReport.formatNanos(resultadoTim.tiempoOrdenamientoNs()));

        System.out.println("\nPrimeras 5 transacciones ordenadas:");
        resultadoTim.ordenada().stream()
                .limit(5)
                .forEach(this::imprimirTransaccionSeparada);
    }

    private void demostrarFiltrado(List<Transaction> transacciones) {

        System.out.println("\n==========================================");
        System.out.println(" DEMOSTRACIÓN DE FILTRADO");
        System.out.println("==========================================");

        List<Transaction> depositos = filterService.filtrarPorTipo(
                transacciones,
                TransactionType.DEPOSITO
        );

        System.out.println("\n--- Filtro por tipo: DEPÓSITO ---");
        System.out.println("Cantidad encontrada: " + depositos.size());

        System.out.println("\nPrimeros 3 depósitos:");
        depositos.stream()
                .limit(3)
                .forEach(this::imprimirTransaccionSeparada);

        List<Transaction> filtradas = filterService.filtrarCombinado(
                transacciones,
                TransactionType.TRANSFERENCIA,
                1000.0,
                20000.0,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 6, 30)
        );

        System.out.println("\n--- Filtro combinado ---");
        System.out.println("Tipo.............: TRANSFERENCIA");
        System.out.println("Monto mínimo.....: $1.000");
        System.out.println("Monto máximo.....: $20.000");
        System.out.println("Fecha desde......: 2024-01-01");
        System.out.println("Fecha hasta......: 2024-06-30");
        System.out.println("Cantidad encontrada: " + filtradas.size());

        System.out.println("\nPrimeros 3 resultados del filtro combinado:");
        filtradas.stream()
                .limit(3)
                .forEach(this::imprimirTransaccionSeparada);

        List<Transaction> q1 = filterService.filtrarPorFecha(
                transacciones,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31)
        );

        System.out.println("\n--- Filtro por fecha: Q1 2024 ---");
        System.out.println("Cantidad encontrada: " + q1.size());
    }

    private void imprimirTransaccion(Transaction t) {

        if (t == null) {
            System.out.println("No se encontró la transacción.");
            return;
        }

        System.out.println("ID..........: " + t.getId());
        System.out.println("Tipo........: " + t.getTipo());
        System.out.println("Monto.......: $" + String.format("%,.2f", t.getMonto()));
        System.out.println("Fecha.......: " + t.getFecha());
        System.out.println("Descripción.: " + t.getDescripcion());
    }

    private void imprimirTransaccionSeparada(Transaction t) {
        System.out.println("------------------------------------------");
        imprimirTransaccion(t);
    }
}