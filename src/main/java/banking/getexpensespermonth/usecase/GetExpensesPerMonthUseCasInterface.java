package banking.getexpensespermonth.usecase;

import banking.common.repository.model.Year;

/**
 * Interface for getting expenses information per month.
 */
public interface GetExpensesPerMonthUseCasInterface {

    /**
     * Executes the use case to get expenses information for a specified year.
     *
     * @param year the year for which expenses information is requested
     * @return the expenses information for the specified year
     */
    Year execute(String year);
}