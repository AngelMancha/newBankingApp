package banking.getpayroll.controller;

import banking.common.repository.model.Year;
import banking.getpayroll.usecase.GetPayrollInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banking")
@CrossOrigin(origins = "http://localhost:3000")
public class GetPayrollController {

    private final GetPayrollInterface getPayrollInterface;

    @PostMapping("/get_payroll")
    public ResponseEntity<Year> getExpenses(@RequestBody String year) {
        Year income = getPayrollInterface.execute(year);
        System.out.println(("hola hocho"));
        return ResponseEntity.ok(income);
    }
}
