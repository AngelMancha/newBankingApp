package banking.getexpensesofamonth.usecase;

import banking.common.repository.model.Operacion;

import java.util.List;

/**
 * Interface for getting expenses information.
 */
public interface GetExpensesUseCaseOfAMonthInterface {

    /**
     * Executes the use case to get expenses information for a specified year and month.
     *
     * @param year the year for which expenses information is requested
     * @param month the month for which expenses information is requested
     * @return a list of operations representing the expenses for the specified year and month
     */
    List<Operacion> execute(String year, String month);
}