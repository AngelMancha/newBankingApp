package banking.getexpenses.controller;

import banking.common.repository.model.Operacion;
import banking.getexpenses.usecase.GetExpensesUseCaseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banking")
@CrossOrigin(origins = "http://localhost:3000")
public class GetExpensesController {

    private final GetExpensesUseCaseInterface getExpensesUseCase;

    @PostMapping("/get_expenses")
    public ResponseEntity<List<Operacion>> getExpenses(@RequestBody String year) {
        List<Operacion> operaciones = getExpensesUseCase.execute(year);
        return ResponseEntity.ok(operaciones);
    }
}