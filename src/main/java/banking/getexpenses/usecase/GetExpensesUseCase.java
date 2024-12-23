package banking.getexpenses.usecase;

import banking.common.repository.OperacionesRepository;
import banking.common.repository.model.Operacion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetExpensesUseCase implements GetExpensesUseCaseInterface {

    private final OperacionesRepository operacionesRepository;

    @Override
    public List<Operacion> execute(String year) {
        List<Operacion> operaciones = operacionesRepository.findAllWithNegativeImporte(year);
        System.out.println("Expenses processed");
        return operaciones;
    }
}
