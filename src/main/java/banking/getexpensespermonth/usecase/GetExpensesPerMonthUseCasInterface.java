package banking.getexpensespermonth.usecase;

import banking.common.repository.model.Year;

public interface GetExpensesPerMonthUseCasInterface {

    Year execute(String year);
}
