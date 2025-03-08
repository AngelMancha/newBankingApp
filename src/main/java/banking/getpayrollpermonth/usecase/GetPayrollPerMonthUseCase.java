package banking.getpayrollpermonth.usecase;

import banking.common.repository.OperacionesRepository;
import banking.common.repository.model.Operacion;
import banking.common.repository.model.Year;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for getting payroll information per month.
 */
@Service
@RequiredArgsConstructor
public class GetPayrollPerMonthUseCase implements GetPayrollPerMonthInterface {
    private final OperacionesRepository operacionesRepository;

    /**
     * Executes the use case to get payroll information for a specified year.
     *
     * @param year the year for which payroll information is requested
     * @return the payroll information for the specified year
     */
    @Override
    public Year execute(String year) {
        System.out.println("Payroll processed");
        List<Operacion> operaciones = operacionesRepository.findAllWithPayrollTag(year);

        Year yearFilter = new Year();

        for (Operacion operacion : operaciones) {
            int month = operacion.getFechaOperacion().toLocalDateTime().getMonthValue();
            switch (month) {
                case 1:
                    yearFilter.setJanuary(yearFilter.getJanuary() + operacion.getImporte());
                    break;
                case 2:
                    yearFilter.setFebruary(yearFilter.getFebruary() + operacion.getImporte());
                    break;
                case 3:
                    yearFilter.setMarch(yearFilter.getMarch() + operacion.getImporte());
                    break;
                case 4:
                    yearFilter.setApril(yearFilter.getApril() + operacion.getImporte());
                    break;
                case 5:
                    yearFilter.setMay(yearFilter.getMay() + operacion.getImporte());
                    break;
                case 6:
                    yearFilter.setJune(yearFilter.getJune() + operacion.getImporte());
                    break;
                case 7:
                    yearFilter.setJuly(yearFilter.getJuly() + operacion.getImporte());
                    break;
                case 8:
                    yearFilter.setAugust(yearFilter.getAugust() + operacion.getImporte());
                    break;
                case 9:
                    yearFilter.setSeptember(yearFilter.getSeptember() + operacion.getImporte());
                    break;
                case 10:
                    yearFilter.setOctober(yearFilter.getOctober() + operacion.getImporte());
                    break;
                case 11:
                    yearFilter.setNovember(yearFilter.getNovember() + operacion.getImporte());
                    break;
                case 12:
                    yearFilter.setDecember(yearFilter.getDecember() + operacion.getImporte());
                    break;
                default:
                    yearFilter.setDecember(0.00);
                    break;
            }
        }

        return yearFilter;
    }
}