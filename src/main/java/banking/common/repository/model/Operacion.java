package banking.common.repository.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Operacion{
    private Timestamp fechaOperacion;
    private double importe;
    private double saldo;
    private String concepto;
    private String etiqueta;
}
