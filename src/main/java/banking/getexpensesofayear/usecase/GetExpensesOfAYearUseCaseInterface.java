package banking.getexpensesofayear.usecase;

import banking.common.repository.model.Operacion;

import java.util.List;

/**
 * Interface for getting annual expenses information.
 */
public interface GetExpensesOfAYearUseCaseInterface {

    /**
     * Executes the use case to get annual expenses information for a specified year.
     *
     * @param year the year for which annual expenses information is requested
     * @return a list of operations representing the expenses for the specified year
     */
    List<Operacion> execute(String year);

}