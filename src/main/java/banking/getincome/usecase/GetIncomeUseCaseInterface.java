package banking.getincome.usecase;

import banking.common.repository.model.Operacion;

import java.util.List;

public interface GetIncomeUseCaseInterface {
    List<Operacion> execute();
}
