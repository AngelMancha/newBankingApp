package banking.getpayroll.usecase;

import banking.common.repository.OperacionesRepository;
import banking.common.repository.model.Operacion;
import banking.common.repository.model.Year;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPayrollUseCase implements GetPayrollInterface {
    private final OperacionesRepository operacionesRepository;
    @Override
    public Year execute(String year) {
        System.out.println("Payroll processed");
        List<Operacion> operaciones = operacionesRepository.findAllWithPayrollTag(year);

        Year yearFilter = new Year();

        for(Operacion operacion : operaciones){
            int month = operacion.getFechaOperacion().toLocalDateTime().getMonthValue();
            if (month == 1){
                yearFilter.setJanuary(operacion.getImporte());
            } else if (month == 2){
                yearFilter.setFebruary(operacion.getImporte());
            } else if (month == 3){
                yearFilter.setMarch(operacion.getImporte());
            } else if (month == 4){
                yearFilter.setApril(operacion.getImporte());
            } else if (month == 5){
                yearFilter.setMay(operacion.getImporte());
            } else if (month == 6){
                yearFilter.setJune(operacion.getImporte());
            } else if (month == 7){
                yearFilter.setJuly(operacion.getImporte());
            } else if (month == 8){
                yearFilter.setAugust(operacion.getImporte());
            } else if (month == 9){
                yearFilter.setSeptember(operacion.getImporte());
            } else if (month == 10){
                yearFilter.setOctober(operacion.getImporte());
            } else if (month == 11){
                yearFilter.setNovember(operacion.getImporte());
            } else if (month == 12){
                yearFilter.setDecember(operacion.getImporte());
            } else {
                yearFilter.setDecember(0.00);
            }
        }

        return yearFilter;
    }
}
