package banking.common.repository;

import banking.common.repository.model.Operacion;

import java.sql.Timestamp;
import java.util.List;

public interface OperacionesRepository {
    int countByFechaOperacionAndImporteAndConcepto(Timestamp fechaOperacion, Double importe, String concepto);

    void insertOperacion(Timestamp fechaOperacion, Double importe, String concepto, String etiqueta, String original);

    void updateTag(Timestamp fechaOperacion, Double importe, String concepto, String nuevaEtiqueta);

    List<Operacion> findAllWithNegativeImporte(String year);

    List<Operacion> findAllWithPositiveImporte();

    List<Operacion> findAllWithOriginalTag();

    List<Operacion> auxiliarDbOperation();

    List<Operacion> findAllWithPayrollTag(String year);

    List<Operacion> findAllWithInterestTag();

}