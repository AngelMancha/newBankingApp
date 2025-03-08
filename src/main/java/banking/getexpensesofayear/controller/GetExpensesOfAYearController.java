package banking.getexpensesofayear.controller;

import banking.common.repository.model.FechaDto;
import banking.common.repository.model.Operacion;
import banking.getexpensesofayear.usecase.GetExpensesOfAYearUseCaseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling annual expenses related requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/banking")
@CrossOrigin(origins = "http://localhost:3000")
public class GetExpensesOfAYearController {

    private final GetExpensesOfAYearUseCaseInterface getExpensesAnualesUseCase;

    /**
     * Endpoint to get annual expenses information for a specified year.
     *
     * @param fecha the date object containing the year for which annual expenses information is requested
     * @return ResponseEntity containing a list of operations representing the expenses for the specified year
     */
    @PostMapping("/get_expenses_anuales")
    public ResponseEntity<List<Operacion>> getExpenses(@RequestBody FechaDto fecha) {
        String year = fecha.getYear();
        List<Operacion> operaciones = getExpensesAnualesUseCase.execute(year);
        return ResponseEntity.ok(operaciones);
    }
}