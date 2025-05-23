package banking.changetag.controller;

import banking.changetag.usecase.ChangeTagUseCaseInterface;
import banking.common.repository.model.Operacion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling requests to change the tag of an operation.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/banking")
public class ChangeTagController {

    private final ChangeTagUseCaseInterface changeTagUseCase;

    /**
     * Endpoint to change the tag of an operation.
     *
     * @param requestBody the request body containing the operation details
     * @return the updated operation
     */
    @PostMapping("/change_tag")
    public ResponseEntity<Operacion> changeTag(@RequestBody Operacion requestBody) {

        String fechaOperacion = requestBody.getFechaOperacion().toString();
        double importe = requestBody.getImporte();
        String concepto = requestBody.getConcepto();
        String tag = requestBody.getEtiqueta();

        Operacion nuevaOperacion = changeTagUseCase.execute(fechaOperacion, importe, concepto, tag);

        return ResponseEntity.ok(nuevaOperacion);
    }

}