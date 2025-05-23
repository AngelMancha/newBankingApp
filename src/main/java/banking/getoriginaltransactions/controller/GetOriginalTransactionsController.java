package banking.getoriginaltransactions.controller;

import banking.common.repository.model.Operacion;
import banking.getoriginaltransactions.usecase.GetOriginalTransactionsUseCaseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banking")
@CrossOrigin(origins = "http://localhost:3000")
public class GetOriginalTransactionsController {

    private final GetOriginalTransactionsUseCaseInterface getOriginalTransactionsUseCase;

    @GetMapping("/get_original_transactions")
    public ResponseEntity<List<Operacion>> getExpenses() {
        List<Operacion> operaciones = getOriginalTransactionsUseCase.execute();
        return ResponseEntity.ok(operaciones);
    }
}
