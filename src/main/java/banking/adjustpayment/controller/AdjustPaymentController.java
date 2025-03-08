package banking.adjustpayment.controller;

import banking.adjustpayment.usecase.adjustPaymentUseCaseInterface;
import banking.common.repository.model.AdjustPaymentRequestDto;
import banking.common.repository.model.Operacion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling requests to adjust payments.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/banking")
@CrossOrigin(origins = "http://localhost:3000")
public class AdjustPaymentController {

    private final adjustPaymentUseCaseInterface adjustPaymentUseCase;

    /**
     * Endpoint to adjust payments.
     *
     * @param request the request body containing the details of the operations
     * @return the adjusted operation
     */
    @PostMapping("/adjust_payment")
    public ResponseEntity<Operacion> adjustPayment(@RequestBody AdjustPaymentRequestDto request) {
        List<Operacion> operacionesGasto = request.getOperacionesGasto();
        List<Operacion> operacionesIngreso = request.getOperacionesIngreso();
        Operacion nuevoGasto = adjustPaymentUseCase.execute(operacionesGasto, operacionesIngreso);
        return ResponseEntity.ok(nuevoGasto);
    }
}