package banking.getpayrollpermonth.usecase;

import banking.common.repository.model.Year;

/**
 * Interface for getting payroll information per month.
 */
public interface GetPayrollPerMonthInterface {

    /**
     * Executes the use case to get payroll information for a specified year.
     *
     * @param year the year for which payroll information is requested
     * @return the payroll information for the specified year
     */
    Year execute(String year);
}