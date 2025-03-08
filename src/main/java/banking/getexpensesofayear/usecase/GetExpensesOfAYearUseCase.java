package banking.getexpensesofayear.usecase;

import banking.common.repository.OperacionesRepository;
import banking.common.repository.model.Operacion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for getting annual expenses information.
 */
@RequiredArgsConstructor
@Service
public class GetExpensesOfAYearUseCase implements GetExpensesOfAYearUseCaseInterface {

    private final OperacionesRepository operacionesRepository;

    /**
     * Executes the use case to get annual expenses information for a specified year.
     *
     * @param year the year for which annual expenses information is requested
     * @return a list of operations representing the expenses for the specified year
     */
    @Override
    public List<Operacion> execute(String year) {
        List<Operacion> operaciones = operacionesRepository.findAllWithNegativeImporte(year);
        System.out.println("Expenses processed");
        return operaciones;
    }
}