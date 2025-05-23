package banking.getincomeofayear.usecase;

import banking.common.repository.model.Operacion;

import java.util.List;

/**
 * Interface for getting income information.
 */
public interface GetIncomeOfAYearUseCaseInterface {

    /**
     * Executes the use case to get income information for a specified year and month.
     *
     * @param year the year for which income information is requested
     * @param month the month for which income information is requested
     * @return a list of operations representing the income for the specified year and month
     */
    List<Operacion> execute(String year, String month);
}