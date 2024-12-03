package banking.getincome.usecase;

import banking.common.repository.OperacionesRepository;
import banking.common.repository.model.Operacion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetIncomeUseCase implements GetIncomeUseCaseInterface {

    private final OperacionesRepository operacionesRepository;

    @Override
    public List<Operacion> execute() {
        List<Operacion> operaciones = operacionesRepository.findAllWithPositiveImporte();
        System.out.println("Income processed");
        return operaciones;
    }
}
