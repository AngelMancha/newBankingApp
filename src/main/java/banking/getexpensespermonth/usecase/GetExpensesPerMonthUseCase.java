package banking.getexpensespermonth.usecase;

import banking.common.repository.OperacionesRepository;
import banking.common.repository.model.Operacion;
import banking.common.repository.model.Year;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for getting expenses information per month.
 */
@RequiredArgsConstructor
@Service
public class GetExpensesPerMonthUseCase implements GetExpensesPerMonthUseCasInterface {

    private final OperacionesRepository operacionesRepository;

    /**
     * Executes the use case to get expenses information for a specified year.
     *
     * @param year the year for which expenses information is requested
     * @return the expenses information for the specified year
     */
    @Override
    public Year execute(String year) {
        List<Operacion> operaciones = operacionesRepository.findAllWithNegativeImporte(year);
        Year yearFilter = new Year();

        for(Operacion operacion : operaciones){
            int month = operacion.getFechaOperacion().toLocalDateTime().getMonthValue();
            if (month == 1){
                yearFilter.setJanuary(yearFilter.getJanuary() + operacion.getImporte());
            } else if (month == 2){
                yearFilter.setFebruary(yearFilter.getFebruary() + operacion.getImporte());
            } else if (month == 3){
                yearFilter.setMarch(yearFilter.getMarch() + operacion.getImporte());
            } else if (month == 4){
                yearFilter.setApril(yearFilter.getApril() + operacion.getImporte());
            } else if (month == 5){
                yearFilter.setMay(yearFilter.getMay() + operacion.getImporte());
            } else if (month == 6){
                yearFilter.setJune(yearFilter.getJune() + operacion.getImporte());
            } else if (month == 7){
                yearFilter.setJuly(yearFilter.getJuly() + operacion.getImporte());
            } else if (month == 8){
                yearFilter.setAugust(yearFilter.getAugust() + operacion.getImporte());
            } else if (month == 9){
                yearFilter.setSeptember(yearFilter.getSeptember() + operacion.getImporte());
            } else if (month == 10){
                yearFilter.setOctober(yearFilter.getOctober() + operacion.getImporte());
            } else if (month == 11){
                yearFilter.setNovember(yearFilter.getNovember() + operacion.getImporte());
            } else if (month == 12){
                yearFilter.setDecember(yearFilter.getDecember() + operacion.getImporte());
            }
        }

        return yearFilter;
    }
}