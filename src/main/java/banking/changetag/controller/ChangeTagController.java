package banking.changetag.controller;

import banking.changetag.usecase.ChangeTagUseCaseInterface;
import banking.common.repository.model.Operacion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banking")
public class ChangeTagController {

    private final ChangeTagUseCaseInterface changeTagUseCase;

    @PostMapping("/change_tag")
    public ResponseEntity<Operacion> changeTag(@RequestBody Operacion requestBody) {

        String fechaOperacion = requestBody.getFechaOperacion().toString();
        double importe = requestBody.getImporte();
        double saldo = requestBody.getSaldo();
        String concepto = requestBody.getConcepto();
        String tag = requestBody.getEtiqueta();


        Operacion nuevaOperacion = changeTagUseCase.execute(fechaOperacion, importe, saldo, concepto, tag);
        // Process the data and change the tag as needed
        // For example, update the database or perform other operations

        return ResponseEntity.ok(nuevaOperacion);
    }

}