package com.bank.analysis.inicial;

/**
 * Clase original ("código sucio") utilizada como punto de partida para la
 * refactorización.
 *
 * <p>
 * Características intencionalmente malas:
 * <ul>
 * <li>Atributos públicos.</li>
 * <li>Tipos poco apropiados (String para fecha y tipo).</li>
 * <li>Sin encapsulamiento.</li>
 * <li>Sin validaciones.</li>
 * </ul>
 */
public class Transaction {

    /** Identificador de la transacción */
    public long id;

    /** Monto de la transacción */
    public double mnt;

    /** Tipo de transacción */
    public String tipo;

    /** Fecha (String en lugar de LocalDate) */
    public String fec;

    /** Descripción */
    public String desc;

    public Transaction(
            long id,
            double mnt,
            String tipo,
            String fec,
            String desc) {

        this.id = id;
        this.mnt = mnt;
        this.tipo = tipo;
        this.fec = fec;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", mnt=" + mnt +
                ", tipo='" + tipo + '\'' +
                ", fec='" + fec + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}