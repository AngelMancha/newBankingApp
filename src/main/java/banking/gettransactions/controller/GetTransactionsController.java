package banking.gettransactions.controller;

import banking.gettransactions.usecase.GetTransactionsUseCaseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/banking")

public class GetTransactionsController  {
    protected final GetTransactionsUseCaseInterface getTransactionsUseCase;

    @GetMapping("/transactions")
    public String processTransactions() {
        getTransactionsUseCase.execute();
        return "Transactions processed";
    }
}