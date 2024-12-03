package banking.getbalance.controller;

import banking.common.repository.model.Operacion;
import banking.getbalance.usecase.GetBalanceUseCaseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banking")
@CrossOrigin(origins = "http://localhost:3000")
public class GetBalanceController {

    private final GetBalanceUseCaseInterface getBalanceUseCase;

    @GetMapping("/get_balance")
    public ResponseEntity<Operacion> getBalance() {
        Operacion ultimaOperacion = getBalanceUseCase.execute();
        return ResponseEntity.ok(ultimaOperacion);
    }
}