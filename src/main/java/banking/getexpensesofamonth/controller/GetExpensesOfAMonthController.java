package banking.getexpensesofamonth.controller;

import banking.common.repository.model.FechaDto;
import banking.common.repository.model.Operacion;
import banking.getexpensesofamonth.usecase.GetExpensesUseCaseOfAMonthInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling expenses related requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/banking")
@CrossOrigin(origins = "*")
public class GetExpensesOfAMonthController {

    private final GetExpensesUseCaseOfAMonthInterface getExpensesUseCase;

    /**
     * Endpoint to get expenses information for a specified year and month.
     *
     * @param fecha the date object containing the year and month for which expenses information is requested
     * @return ResponseEntity containing a list of operations representing the expenses for the specified year and month
     */
    @PostMapping("/get_expenses")
    public ResponseEntity<List<Operacion>> getExpenses(@RequestBody FechaDto fecha) {
        String year = fecha.getYear();
        String month = fecha.getMonth();
        List<Operacion> operaciones = getExpensesUseCase.execute(year, month);
        return ResponseEntity.ok(operaciones);
    }
}