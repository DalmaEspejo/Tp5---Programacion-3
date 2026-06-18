package com.bank.analysis.inicial;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class TransactionProcessor {

    private List<Transaction> transacciones;
    private Random random;

    private static final String[] TIPOS = { "DEPOSITO", "RETIRO", "TRANSFERENCIA", "PAGO" };

    private static final String[] DESCS = {
            "Pago de servicios", "Transferencia recibida", "Depósito en cajero",
            "Retiro en sucursal", "Compra con tarjeta", "Pago de nómina",
            "Transferencia enviada", "Depósito en ventanilla", "Retiro en cajero",
            "Pago de factura", "Compra en línea", "Pago recurrente",
            "Depósito de cheque", "Transferencia entre cuentas", "Pago de préstamo",
            "Retiro sin tarjeta", "Depósito móvil", "Pago de impuestos",
            "Transferencia internacional", "Abono de intereses"
    };

    public TransactionProcessor() {
        this.transacciones = new ArrayList<>();
        this.random = new Random(42);

// Datos predecibles para tests
        transacciones.add(new Transaction(1001, 1500.00, "DEPOSITO", "2024-01-15", "Depósito en cajero"));
        transacciones.add(new Transaction(1002, 500.00, "RETIRO", "2024-01-16", "Retiro en sucursal"));
        transacciones.add(new Transaction(1003, 2500.00, "TRANSFERENCIA", "2024-01-17", "Transferencia recibida"));
        transacciones.add(new Transaction(1004, 350.00, "PAGO", "2024-01-18", "Pago de servicios"));
        transacciones.add(new Transaction(1005, 10000.00, "DEPOSITO", "2024-01-19", "Depósito en ventanilla"));
        transacciones.add(new Transaction(1006, 200.00, "RETIRO", "2024-01-20", "Retiro en cajero"));
        transacciones.add(new Transaction(1007, 5000.00, "TRANSFERENCIA", "2024-01-21", "Transferencia enviada"));
        transacciones.add(new Transaction(1008, 750.00, "PAGO", "2024-01-22", "Pago de factura"));
        transacciones.add(new Transaction(1009, 3200.00, "DEPOSITO", "2024-01-23", "Depósito móvil"));
        transacciones.add(new Transaction(1010, 1800.00, "RETIRO", "2024-01-24", "Retiro sin tarjeta"));
        transacciones.add(new Transaction(1011, 4200.00, "TRANSFERENCIA", "2024-02-01", "Transferencia internacional"));
        transacciones.add(new Transaction(1012, 900.00, "PAGO", "2024-02-02", "Pago de impuestos"));
        transacciones.add(new Transaction(1013, 6700.00, "DEPOSITO", "2024-02-03", "Depósito de cheque"));
        transacciones.add(new Transaction(1014, 150.00, "RETIRO", "2024-02-04", "Retiro en cajero"));
        transacciones.add(new Transaction(1015, 8900.00, "TRANSFERENCIA", "2024-02-05", "Transferencia entre cuentas"));
        transacciones.add(new Transaction(1016, 430.00, "PAGO", "2024-02-06", "Pago recurrente"));
        transacciones.add(new Transaction(1017, 11000.00, "DEPOSITO", "2024-02-07", "Abono de intereses"));
        transacciones.add(new Transaction(1018, 600.00, "RETIRO", "2024-02-08", "Retiro en sucursal"));
        transacciones.add(new Transaction(1019, 7700.00, "TRANSFERENCIA", "2024-02-09", "Transferencia recibida"));
        transacciones.add(new Transaction(1020, 280.00, "PAGO", "2024-02-10", "Compra en línea"));
    }       
    

    public void generarTransacciones(int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            long id = 2000 + i;
            double monto = Math.round((100.0 + random.nextDouble() * 50000.0) * 100.0) / 100.0;
            String tipo = TIPOS[random.nextInt(TIPOS.length)];
            int anio = 2024;
            int mes = 1 + random.nextInt(12);
            int dia = 1 + random.nextInt(28);
            String fecha = String.format("%d-%02d-%02d", anio, mes, dia);
            String descripcion = DESCS[random.nextInt(DESCS.length)];

            transacciones.add(new Transaction(id, monto, tipo, fecha, descripcion));
        }
    }

    public List<Transaction> getTransacciones() {
        return transacciones;
    }

    public Transaction buscarPorId(long id) {
        for (Transaction t : transacciones) {
            if (t.id == id) {
                return t;
            }
        }

        return null;
    }

    public Transaction buscarPorIdBinario(List<Transaction> listaOrdenada, long id) {
        int izquierda = 0;
        int derecha = listaOrdenada.size() - 1;

        while (izquierda <= derecha) {
            int medio = izquierda + (derecha - izquierda) / 2;
            Transaction transDelMedio = listaOrdenada.get(medio);

            if (transDelMedio.id == id) {
                return transDelMedio;
            } else if (transDelMedio.id < id) {
                izquierda = medio + 1;
            } else {
                derecha = medio - 1;
            }
        }

        return null;
    }

    public List<Transaction> buscarPorMonto(double montoMin, double montoMax) {
        List<Transaction> resultado = new ArrayList<>();

        for (Transaction t : transacciones) {
            if (t.mnt >= montoMin && t.mnt <= montoMax) {
                resultado.add(t);
            }
        }

        return resultado;
    }

    public List<Transaction> buscarPorTipo(String tipo) {
        List<Transaction> resultado = new ArrayList<>();

        for (Transaction t : transacciones) {
            if (t.tipo == null && tipo == null) {
                resultado.add(t);
            } else if (t.tipo != null && t.tipo.equals(tipo)) {
                resultado.add(t);
            }
        }

        return resultado;
    }

    public List<Transaction> ordenarManual(boolean ascendente) {
        List<Transaction> copia = new ArrayList<>(transacciones);

        long comparaciones = 0;
        long intercambios = 0;

        for (int i = 0; i < copia.size() - 1; i++) {
            for (int j = 0; j < copia.size() - 1 - i; j++) {
                comparaciones++;

                Transaction a = copia.get(j);
                Transaction b = copia.get(j + 1);

                boolean estanMal = ascendente
                        ? a.mnt > b.mnt
                        : a.mnt < b.mnt;

                if (estanMal) {
                    copia.set(j, b);
                    copia.set(j + 1, a);
                    intercambios++;
                }
            }
        }

        System.out.println("Bubble Sort -> comparaciones: " + comparaciones
                + ", intercambios: " + intercambios);

        return copia;
    }

    public List<Transaction> ordenarBuiltIn(boolean ascendente) {
        List<Transaction> copia = new ArrayList<>(transacciones);

        Comparator<Transaction> comparador =
                Comparator.comparingDouble(t -> t.mnt);

        if (!ascendente) {
            comparador = comparador.reversed();
        }

        copia.sort(comparador);

        return copia;
    }

    public List<Transaction> filtrarAvanzado(
            String tipo,
            Double montoMin,
            Double montoMax,
            String fechaDesde,
            String fechaHasta) {

        List<Transaction> resultado = new ArrayList<>();

        for (Transaction t : transacciones) {
            boolean cumple = true;

            if (tipo != null && !tipo.isEmpty()) {
                if (!tipo.equals(t.tipo)) {
                    cumple = false;
                }
            }

            if (cumple && montoMin != null) {
                if (t.mnt < montoMin) {
                    cumple = false;
                }
            }

            if (cumple && montoMax != null) {
                if (t.mnt > montoMax) {
                    cumple = false;
                }
            }

            if (cumple && fechaDesde != null && !fechaDesde.isEmpty()) {
                if (t.fec.compareTo(fechaDesde) < 0) {
                    cumple = false;
                }
            }

            if (cumple && fechaHasta != null && !fechaHasta.isEmpty()) {
                if (t.fec.compareTo(fechaHasta) > 0) {
                    cumple = false;
                }
            }

            if (cumple) {
                resultado.add(t);
            }
        }

        return resultado;
    }

    public void imprimir(List<Transaction> lista, int maxMostrar) {
        int count = 0;

        for (Transaction t : lista) {
            if (maxMostrar > 0 && count >= maxMostrar) {
                System.out.println("... y " + (lista.size() - maxMostrar) + " más");
                break;
            }

            System.out.println(t);
            count++;
        }
    }

    public void contarPorTipo() {
        int depositos = 0;
        int retiros = 0;
        int transferencias = 0;
        int pagos = 0;

        for (Transaction t : transacciones) {
            if ("DEPOSITO".equals(t.tipo)) {
                depositos++;
            } else if ("RETIRO".equals(t.tipo)) {
                retiros++;
            } else if ("TRANSFERENCIA".equals(t.tipo)) {
                transferencias++;
            } else if ("PAGO".equals(t.tipo)) {
                pagos++;
            }
        }

        System.out.println("=== Conteo por tipo ===");
        System.out.println("DEPOSITOS: " + depositos);
        System.out.println("RETIROS: " + retiros);
        System.out.println("TRANSFERENCIAS: " + transferencias);
        System.out.println("PAGOS: " + pagos);
    }

    public double calcularBalance() {
        double total = 0;

        for (Transaction t : transacciones) {
            if ("DEPOSITO".equals(t.tipo) || "TRANSFERENCIA".equals(t.tipo)) {
                total += t.mnt;
            } else {
                total -= t.mnt;
            }
        }

        return total;
    }
}