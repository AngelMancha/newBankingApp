package banking.getexpenses.controller;

import banking.common.repository.model.Operacion;
import banking.getincome.usecase.GetIncomeUseCaseInterface;
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
public class GetIncomeController {

    private final GetIncomeUseCaseInterface getIncomeUseCase;

    @GetMapping("/get_income")
    public ResponseEntity<List<Operacion>> getExpenses() {
        List<Operacion> operaciones = getIncomeUseCase.execute();
        return ResponseEntity.ok(operaciones);
    }
}