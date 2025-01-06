package banking.getexpenses.controller;

import banking.common.repository.model.FechaDto;
import banking.common.repository.model.Operacion;
import banking.getexpensesanuales.usecase.GetExpensesAnualesUseCaseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banking")
@CrossOrigin(origins = "http://localhost:3000")
public class GetExpensesAnualesController {

    private final GetExpensesAnualesUseCaseInterface getExpensesAnualesUseCase;

    @PostMapping("/get_expenses_anuales")
    public ResponseEntity<List<Operacion>> getExpenses(@RequestBody FechaDto fecha) {
        String year = fecha.getYear();
        List<Operacion> operaciones = getExpensesAnualesUseCase.execute(year);
        return ResponseEntity.ok(operaciones);
    }
}