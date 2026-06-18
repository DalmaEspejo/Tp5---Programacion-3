package com.bank.analysis.solucion.service;

import com.bank.analysis.solucion.model.Transaction;
import com.bank.analysis.solucion.model.TransactionType;

import com.bank.analysis.solucion.service.filter.FiltroPredicate;
import com.bank.analysis.solucion.service.filter.FiltroPorFecha;
import com.bank.analysis.solucion.service.filter.FiltroPorRangoMonto;
import com.bank.analysis.solucion.service.filter.FiltroPorTipo;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de filtrado de transacciones.
 *
 * <p>
 * Aplica el patrón Strategy usando filtros que implementan FiltroPredicate.
 *
 * @since 1.0
 */
@Service
public class FilterServiceImpl implements FilterService {

    @Override
    public List<Transaction> filtrar(
            List<Transaction> transacciones,
            List<FiltroPredicate> filtros) {

        Objects.requireNonNull(transacciones, "La lista no puede ser null");
        Objects.requireNonNull(filtros, "La lista de filtros no puede ser null");

        return transacciones.stream()
                .filter(t -> filtros.stream().allMatch(filtro -> filtro.cumple(t)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> filtrarPorTipo(
            List<Transaction> transacciones,
            TransactionType tipo) {

        return filtrar(
                transacciones,
                List.of(new FiltroPorTipo(tipo))
        );
    }

    @Override
    public List<Transaction> filtrarPorRangoMonto(
            List<Transaction> transacciones,
            double min,
            double max) {

        return filtrar(
                transacciones,
                List.of(new FiltroPorRangoMonto(min, max))
        );
    }

    @Override
    public List<Transaction> filtrarPorFecha(
            List<Transaction> transacciones,
            LocalDate desde,
            LocalDate hasta) {

        return filtrar(
                transacciones,
                List.of(new FiltroPorFecha(desde, hasta))
        );
    }

    @Override
    public List<Transaction> filtrarCombinado(
            List<Transaction> transacciones,
            TransactionType tipo,
            Double montoMin,
            Double montoMax,
            LocalDate fechaDesde,
            LocalDate fechaHasta) {

        List<FiltroPredicate> filtros = new ArrayList<>();
        
        if (tipo != null) {
            filtros.add(new FiltroPorTipo(tipo));
        }

        if (montoMin != null && montoMax != null) {
            filtros.add(new FiltroPorRangoMonto(montoMin, montoMax));
        }

        if (fechaDesde != null && fechaHasta != null) {
            filtros.add(new FiltroPorFecha(fechaDesde, fechaHasta));
        }

        return filtrar(transacciones, filtros);
    }
}