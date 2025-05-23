package banking.gettransactions.usecase;

import banking.common.repository.model.xlsConfigurationDto;

public interface GetTransactionsUseCaseInterface {
    void execute(byte[] file, xlsConfigurationDto request);
}
