package banking.getexpensesofamonth.usecase;

import banking.common.repository.OperacionesRepository;
import banking.common.repository.model.Operacion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for getting expenses information.
 */
@RequiredArgsConstructor
@Service
public class GetExpensesOfAMonthUseCaseOfAMonth implements GetExpensesUseCaseOfAMonthInterface {

    private final OperacionesRepository operacionesRepository;

    /**
     * Executes the use case to get expenses information for a specified year and month.
     *
     * @param year the year for which expenses information is requested
     * @param month the month for which expenses information is requested
     * @return a list of operations representing the expenses for the specified year and month
     */
    @Override
    public List<Operacion> execute(String year, String month) {
        List<Operacion> operaciones = operacionesRepository.findAllWithNegativeImporte(year, month);
        System.out.println("Expenses processed");
        return operaciones;
    }
}