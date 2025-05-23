package banking.getbalance.usecase;

import banking.common.repository.OperacionesRepository;
import banking.common.repository.model.Operacion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetBalanceUseCase implements GetBalanceUseCaseInterface {

    private final OperacionesRepository operacionesRepository;

    @Override
    public List<Operacion> execute() {
        return operacionesRepository.auxiliarDbOperation();
    }
}
