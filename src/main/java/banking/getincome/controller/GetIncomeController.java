package banking.getincome.controller;

import banking.common.repository.model.FechaDto;
import banking.common.repository.model.Operacion;
import banking.getincome.usecase.GetIncomeUseCaseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banking")
@CrossOrigin(origins = "http://localhost:3000")
public class GetIncomeController {

    private final GetIncomeUseCaseInterface getIncomeUseCase;

    @PostMapping("/get_income")
    public ResponseEntity<List<Operacion>> getExpenses(@RequestBody FechaDto fecha) {
        String year = fecha.getYear();
        String month = fecha.getMonth();
        List<Operacion> operaciones = getIncomeUseCase.execute(year, month);
        return ResponseEntity.ok(operaciones);
    }
}