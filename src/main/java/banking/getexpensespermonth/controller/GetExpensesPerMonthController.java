package banking.getexpensespermonth.controller;

import banking.common.repository.model.FechaDto;
import banking.common.repository.model.Year;
import banking.getexpensespermonth.usecase.GetExpensesPerMonthUseCasInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banking")
@CrossOrigin(origins = "http://localhost:3000")
public class GetExpensesPerMonthController {

    private final GetExpensesPerMonthUseCasInterface getExpensesPerMonthUseCasInterface;

    @PostMapping("/get_expenses_month")
    public ResponseEntity<Year> getExpenses(@RequestBody FechaDto fecha) {
        String year = fecha.getYear();
        Year operaciones = getExpensesPerMonthUseCasInterface.execute(year);
        return ResponseEntity.ok(operaciones);
    }
}