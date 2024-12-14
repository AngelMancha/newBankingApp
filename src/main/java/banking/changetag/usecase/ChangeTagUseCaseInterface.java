package banking.changetag.usecase;

import banking.common.repository.model.Operacion;

public interface ChangeTagUseCaseInterface {
    Operacion execute(String fechaOperacion, Double importe, String concepto, String etiqueta);
}
