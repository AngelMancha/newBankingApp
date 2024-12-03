package banking.getexpenses.usecase;

import banking.common.repository.model.Operacion;

import java.util.List;

public interface GetExpensesUseCaseInterface {

    List<Operacion> execute();
}
