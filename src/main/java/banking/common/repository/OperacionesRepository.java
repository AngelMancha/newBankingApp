package banking.common.repository;

import banking.common.repository.model.Operacion;

import java.sql.Timestamp;
import java.util.List;

public interface OperacionesRepository {
    int countByFechaOperacionAndImporteAndSaldoAndConcepto(Timestamp fechaOperacion, Double importe, Double saldo, String concepto);

    void insertOperacion(Timestamp fechaOperacion, Double importe, Double saldo, String concepto, String etiqueta, String original);

    void updateTag(Timestamp fechaOperacion, Double importe, Double saldo, String concepto, String nuevaEtiqueta);

    List<Operacion> findAllWithNegativeImporte();

    List<Operacion> findAllWithPositiveImporte();

    List<Operacion> findAllWithOriginalTag();

    Operacion findLatestOperacion();
}