package banking.common.repository;

import banking.common.repository.model.Operacion;

import java.sql.Timestamp;
import java.util.List;

/**
 * Repository interface for managing operations in the database.
 */
public interface OperacionesRepository {

    /**
     * Counts the number of operations with the specified date, amount, and concept.
     *
     * @param fechaOperacion the date of the operation
     * @param importe        the amount of the operation
     * @param concepto       the concept of the operation
     * @return the count of matching operations
     */
    int countByFechaOperacionAndImporteAndConcepto(Timestamp fechaOperacion, Double importe, String concepto);

    /**
     * Inserts a new operation into the database.
     *
     * @param fechaOperacion the date of the operation
     * @param importe        the amount of the operation
     * @param concepto       the concept of the operation
     * @param etiqueta       the tag of the operation
     * @param original       the original status of the operation
     */
    void insertOperacion(Timestamp fechaOperacion, Double importe, String concepto, String etiqueta, String original);

    /**
     * Updates the tag of an operation in the database.
     *
     * @param fechaOperacion the date of the operation
     * @param importe        the amount of the operation
     * @param concepto       the concept of the operation
     * @param nuevaEtiqueta  the new tag of the operation
     */
    void updateTag(Timestamp fechaOperacion, Double importe, String concepto, String nuevaEtiqueta);

    /**
     * Finds all operations with a negative amount for the specified year and month.
     *
     * @param year  the year of the operations
     * @param month the month of the operations
     * @return a list of operations with a negative amount
     */
    List<Operacion> findAllWithNegativeImporte(String year, String month);

    /**
     * Finds all operations with a negative amount for the specified year.
     *
     * @param year the year of the operations
     * @return a list of operations with a negative amount
     */
    List<Operacion> findAllWithNegativeImporte(String year);

    /**
     * Finds all operations with a positive amount for the specified year and month.
     *
     * @param year  the year of the operations
     * @param month the month of the operations
     * @return a list of operations with a positive amount
     */
    List<Operacion> findAllWithPositiveImporte(String year, String month);

    /**
     * Finds all operations with a positive amount for the specified year.
     *
     * @param year the year of the operations
     * @return a list of operations with a positive amount
     */
    List<Operacion> findAllWithPositiveImporte(String year);

    /**
     * Finds all operations with the original tag.
     *
     * @return a list of operations with the original tag
     */
    List<Operacion> findAllWithOriginalTag();

    /**
     * Performs an auxiliary database operation.
     *
     * @return a list of operations after performing the auxiliary operation
     */
    List<Operacion> auxiliarDbOperation();

    /**
     * Finds all operations with the payroll tag for the specified year.
     *
     * @param year the year of the operations
     * @return a list of operations with the payroll tag
     */
    List<Operacion> findAllWithPayrollTag(String year);

    /**
     * Finds all operations with the interest tag.
     *
     * @return a list of operations with the interest tag
     */
    List<Operacion> findAllWithInterestTag();
}