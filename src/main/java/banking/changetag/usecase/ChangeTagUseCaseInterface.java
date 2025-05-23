package banking.changetag.usecase;

import banking.common.repository.model.Operacion;

/**
 * Interface for the use case to change the tag of an operation.
 */
public interface ChangeTagUseCaseInterface {
    /**
     * Executes the change tag use case.
     *
     * @param fechaOperacion the date of the operation
     * @param importe        the amount of the operation
     * @param concepto       the concept of the operation
     * @param etiqueta       the new tag for the operation
     * @return the updated operation
     */
    Operacion execute(String fechaOperacion, Double importe, String concepto, String etiqueta);
}