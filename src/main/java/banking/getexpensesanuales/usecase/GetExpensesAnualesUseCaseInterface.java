package banking.getexpensesanuales.usecase;

import banking.common.repository.model.Operacion;

import java.util.List;

public interface GetExpensesAnualesUseCaseInterface {

    List<Operacion> execute(String year);

}
