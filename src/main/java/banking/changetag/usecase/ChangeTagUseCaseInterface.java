package banking.changetag.usecase;

import banking.common.repository.model.Operacion;

public interface ChangeTagUseCaseInterface {
    Operacion execute(String fechaOperacion, Double importe, Double saldo, String concepto, String etiqueta);
}
