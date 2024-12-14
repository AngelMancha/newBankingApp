package banking.common.repository;

import banking.common.repository.model.Operacion;

import java.sql.Timestamp;
import java.util.List;

public interface OperacionesRepository {
    int countByFechaOperacionAndImporteAndConcepto(Timestamp fechaOperacion, Double importe, String concepto);

    void insertOperacion(Timestamp fechaOperacion, Double importe, String concepto, String etiqueta, String original);

    void updateTag(Timestamp fechaOperacion, Double importe, String concepto, String nuevaEtiqueta);

    List<Operacion> findAllWithNegativeImporte();

    List<Operacion> findAllWithPositiveImporte();

    List<Operacion> findAllWithOriginalTag();

    List<Operacion> auxiliarDbOperation();
}