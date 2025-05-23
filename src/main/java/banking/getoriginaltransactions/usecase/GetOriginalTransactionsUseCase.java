package banking.getoriginaltransactions.usecase;

import banking.common.repository.OperacionesRepository;
import banking.common.repository.model.Operacion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetOriginalTransactionsUseCase implements GetOriginalTransactionsUseCaseInterface {

    private final OperacionesRepository operacionesRepository;

    @Override
    public List<Operacion> execute() {
        List<Operacion> operaciones = operacionesRepository.findAllWithOriginalTag();
        System.out.println("Original processed");
        return operaciones;
    }
}
