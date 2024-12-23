package banking.getpayroll.usecase;

import banking.common.repository.model.Year;

public interface GetPayrollInterface {
    Year execute(String year);
}
