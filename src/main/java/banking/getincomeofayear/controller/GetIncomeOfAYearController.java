package banking.getincomeofayear.controller;

import banking.common.repository.model.FechaDto;
import banking.common.repository.model.Operacion;
import banking.getincomeofayear.usecase.GetIncomeOfAYearUseCaseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling income related requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/banking")
@CrossOrigin(origins = "*")
public class GetIncomeOfAYearController {

    private final GetIncomeOfAYearUseCaseInterface getIncomeUseCase;

    /**
     * Endpoint to get income information for a specified year and month.
     *
     * @param fecha the date object containing the year and month for which income information is requested
     * @return ResponseEntity containing a list of operations representing the income for the specified year and month
     */
    @PostMapping("/get_income")
    public ResponseEntity<List<Operacion>> getExpenses(@RequestBody FechaDto fecha) {
        String year = fecha.getYear();
        String month = fecha.getMonth();
        List<Operacion> operaciones = getIncomeUseCase.execute(year, month);
        return ResponseEntity.ok(operaciones);
    }
}