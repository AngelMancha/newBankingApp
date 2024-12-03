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
        Operacion operacionGasto = request.getOperacionGasto();
        List<Operacion> operacionesIngreso = request.getOperacionesIngreso();
        //imprimelo
        System.out.println(operacionGasto);

        // Llamada al caso de uso
        Operacion nuevoGasto = adjustPaymentUseCase.execute(
                operacionGasto.getFechaOperacion().toString(), operacionGasto.getImporte(), operacionGasto.getSaldo(),
                operacionGasto.getConcepto(), operacionGasto.getEtiqueta(), operacionesIngreso);


        // Respuesta exitosa
        return ResponseEntity.ok(nuevoGasto);
    }
}
