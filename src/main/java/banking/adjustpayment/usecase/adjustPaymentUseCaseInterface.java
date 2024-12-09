package banking.adjustpayment.usecase;

import banking.common.repository.model.Operacion;

import java.util.List;

public interface adjustPaymentUseCaseInterface {
    Operacion execute(List<Operacion> operacionesGasto, List<Operacion> operacionesIngreso);
}
