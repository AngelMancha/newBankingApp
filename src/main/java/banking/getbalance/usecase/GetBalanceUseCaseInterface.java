package banking.getbalance.usecase;

import banking.common.repository.model.Operacion;

import java.util.List;

public interface GetBalanceUseCaseInterface {
    List<Operacion> execute();
}
