package banking.adjustpayment.controller;

import banking.adjustpayment.usecase.adjustPaymentUseCaseInterface;
import banking.common.repository.model.AdjustPaymentRequestDto;
import banking.common.repository.model.Operacion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banking")
@CrossOrigin(origins = "http://localhost:3000")
public class AdjustPaymentController {

    private final adjustPaymentUseCaseInterface adjustPaymentUseCase;

    @PostMapping("/adjust_payment")
    public ResponseEntity<Operacion> adjustPayment(@RequestBody AdjustPaymentRequestDto request) {
        // Extraer datos de las operaciones
        List<Operacion> operacionesGasto= request.getOperacionesGasto();
        List<Operacion> operacionesIngreso = request.getOperacionesIngreso();


        // Llamada al caso de uso
        Operacion nuevoGasto = adjustPaymentUseCase.execute(operacionesGasto, operacionesIngreso);


        // Respuesta exitosa
        return ResponseEntity.ok(nuevoGasto);
    }
}
