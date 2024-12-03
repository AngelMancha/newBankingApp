package banking.adjustpayment.usecase;

import banking.common.repository.model.Operacion;

import java.util.List;

public interface adjustPaymentUseCaseInterface {
    Operacion execute(String fechaOperacionG, Double importeG, Double saldoG, String conceptoG, String etiquetaG, List<Operacion> operacionesIngreso);
}
