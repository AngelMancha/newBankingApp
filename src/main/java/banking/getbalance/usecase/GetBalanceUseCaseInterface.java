package banking.getbalance.usecase;

import banking.common.repository.model.Operacion;

public interface GetBalanceUseCaseInterface {
    Operacion execute();
}
