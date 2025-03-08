package banking.common.repository.model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Represents an operation in the banking system.
 */
@Data
public class Operacion {
    /**
     * The date and time of the operation.
     */
    private Timestamp fechaOperacion;

    /**
     * The amount of the operation.
     */
    private double importe;

    /**
     * The concept or description of the operation.
     */
    private String concepto;

    /**
     * The tag or label associated with the operation.
     */
    private String etiqueta;
}