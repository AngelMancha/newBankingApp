package banking.getexpenses.controller;

import banking.common.repository.model.Operacion;
import banking.getexpenses.usecase.GetExpensesUseCaseInterface;
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
public class GetExpensesController {

    private final GetExpensesUseCaseInterface getExpensesUseCase;

    @GetMapping("/get_expenses")
    public ResponseEntity<List<Operacion>> getExpenses() {
        List<Operacion> operaciones = getExpensesUseCase.execute();
        return ResponseEntity.ok(operaciones);
    }
}