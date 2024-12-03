package banking.getoriginaltransactions.usecase;

import banking.common.repository.model.Operacion;

import java.util.List;

public interface GetOriginalTransactionsUseCaseInterface {
    List<Operacion> execute();
}
