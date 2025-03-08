package banking.adjustpayment.usecase;

import banking.common.repository.model.Operacion;

import java.util.List;

/**
 * Interface for the use case to adjust payments.
 */
public interface adjustPaymentUseCaseInterface {
    /**
     * Executes the adjust payment use case.
     *
     * @param operacionesGasto the list of expense operations
     * @param operacionesIngreso the list of income operations
     * @return the adjusted operation
     */
    Operacion execute(List<Operacion> operacionesGasto, List<Operacion> operacionesIngreso);
}