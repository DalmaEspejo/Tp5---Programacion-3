package com.bank.analysis.solucion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bank.analysis.solucion.report.PerformanceReport;
import com.bank.analysis.solucion.service.SearchService;
import com.bank.analysis.solucion.service.SortService;

/**
 * Configuración explícita de beans de Spring para el contexto de la aplicación.
 * <p>
 * <b>Propósito educativo:</b> Demostrar la Inversión de Control (IoC) y la
 * Inyección de Dependencias (DI) de forma explícita con {@code @Configuration}
 * y {@code @Bean}.
 * <p>
 * Los servicios concretos ({@code SearchServiceImpl}, {@code SortServiceImpl},
 * {@code FilterServiceImpl}) son detectados automáticamente por
 * {@code @ComponentScan} gracias a sus anotaciones {@code @Service}.
 * <p>
 * Esta clase define manualmente los beans que requieren ensamblaje explícito
 * o que no están anotados con estereotipos de Spring.
 * <p>
 * <b>Grafo de dependencias:</b>
 * <pre>
 * TransactionDataGenerator (independiente, sin anotación)
 * SearchServiceImpl (@Service, independiente)
 * SortServiceImpl (@Service, independiente)
 * FilterServiceImpl (@Service, independiente)
 * PerformanceReport → SearchService, SortService (inyectados por Spring)
 * MainRefactorizado → PerformanceReport, TransactionDataGenerator,
 *                      FilterService, SearchService, SortService
 * </pre>
 */
@Configuration
public class AppConfig {

    /**
     * Generador de datos de prueba. No tiene {@code @Service} porque es una
     * utilidad, no un servicio de negocio. Se define explícitamente aquí para
     * demostrar la creación programática de beans.
     *
     * @return instancia de {@link TransactionDataGenerator}
     */
    @Bean
    public TransactionDataGenerator transactionDataGenerator() {
        return new TransactionDataGenerator();
    }

    /**
     * Servicio de reportes de rendimiento.
     * <p>
     * Se define como {@code @Bean} explícito (en lugar de {@code @Service})
     * para demostrar cómo Spring resuelve el grafo de dependencias:
     * {@code SearchService} y {@code SortService} son inyectados automáticamente
     * desde los beans detectados por component scan.
     *
     * @param searchService servicio de búsqueda (inyectado por Spring)
     * @param sortService   servicio de ordenamiento (inyectado por Spring)
     * @return instancia configurada de {@link PerformanceReport}
     */
    @Bean
    public PerformanceReport performanceReport(SearchService searchService, SortService sortService) {
        return new PerformanceReport(searchService, sortService);
    }
}
