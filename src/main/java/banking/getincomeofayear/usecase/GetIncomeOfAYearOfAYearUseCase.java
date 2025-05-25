package banking.getincomeofayear.usecase;

import banking.common.repository.OperacionesRepository;
import banking.common.repository.model.Operacion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for getting income information.
 */
@RequiredArgsConstructor
@Service
public class GetIncomeOfAYearOfAYearUseCase implements GetIncomeOfAYearUseCaseInterface {

    private final OperacionesRepository operacionesRepository;

    /**
     * Executes the use case to get income information for a specified year and month.
     *
     * @param year  the year for which income information is requested
     * @param month the month for which income information is requested
     * @return a list of operations representing the income for the specified year and month
     */
    @Override
    public List<Operacion> execute(String year, String month) {
        if (month == null || month.isEmpty()) {
            List<Operacion> operaciones = operacionesRepository.findAllWithPositiveImporte(year);
            System.out.println("Income processed");
            return operaciones;
        } else {
            List<Operacion> operaciones = operacionesRepository.findAllWithPositiveImporte(year, month);
            System.out.println("Income processed");
            return operaciones;
        }
    }
}