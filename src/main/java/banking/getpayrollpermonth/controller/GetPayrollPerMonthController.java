package banking.getpayrollpermonth.controller;

import banking.common.repository.model.Year;
import banking.getpayrollpermonth.usecase.GetPayrollPerMonthInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling payroll per month related requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/banking")
@CrossOrigin(origins = "http://localhost:3000")
public class GetPayrollPerMonthController {

    private final GetPayrollPerMonthInterface getPayrollInterface;

    /**
     * Endpoint to get payroll information for a specified year.
     *
     * @param year the year for which payroll information is requested
     * @return ResponseEntity containing the payroll information for the specified year
     */
    @PostMapping("/get_payroll_month")
    public ResponseEntity<Year> getExpenses(@RequestBody String year) {
        Year income = getPayrollInterface.execute(year);
        return ResponseEntity.ok(income);
    }
}