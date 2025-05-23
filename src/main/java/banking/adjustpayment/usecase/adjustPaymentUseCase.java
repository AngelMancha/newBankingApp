package banking.adjustpayment.usecase;

import banking.common.repository.OperacionesRepository;
import banking.common.repository.model.Operacion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Service class for adjusting payments.
 */
@RequiredArgsConstructor
@Service
public class adjustPaymentUseCase implements adjustPaymentUseCaseInterface {
    private final OperacionesRepository operacionesRepository;

    /**
     * Executes the adjust payment use case.
     *
     * @param operacionesGasto   the list of expense operations
     * @param operacionesIngreso the list of income operations
     * @return the adjusted operation
     */
    @Override
    public Operacion execute(List<Operacion> operacionesGasto, List<Operacion> operacionesIngreso) {

        String nuevaEtiqueta = operacionesGasto.get(0).getEtiqueta();
        String nuevaFecha = operacionesGasto.get(0).getFechaOperacion().toString();
        String nuevoConcepto = operacionesGasto.get(0).getConcepto();
        Double nuevoImporte = 0.0;

        if (operacionesGasto.size() != 1) {
            for (Operacion gasto : operacionesGasto) {
                nuevoImporte += gasto.getImporte();
            }
        } else {
            nuevoImporte = operacionesGasto.get(0).getImporte();
        }

        for (Operacion ingreso : operacionesIngreso) {
            nuevoImporte += ingreso.getImporte();
        }

        String nuevoOriginal = "no";
        Operacion nuevoGasto = new Operacion();
        nuevoGasto.setFechaOperacion(Timestamp.valueOf(nuevaFecha));
        nuevoGasto.setImporte(nuevoImporte);
        nuevoGasto.setConcepto(nuevoConcepto);
        nuevoGasto.setEtiqueta(nuevaEtiqueta);

        // Actualizar en la base de datos los objetos de gasto y crear el nuevo:
        for (Operacion gasto : operacionesGasto) {
            operacionesRepository.updateTag(gasto.getFechaOperacion(), gasto.getImporte(), gasto.getConcepto(), "ASUMIDO");
        }

        for (Operacion ingreso : operacionesIngreso) {
            operacionesRepository.updateTag(ingreso.getFechaOperacion(), ingreso.getImporte(), ingreso.getConcepto(), "ASUMIDO");
        }
        operacionesRepository.insertOperacion(Timestamp.valueOf(nuevaFecha), nuevoImporte, nuevoConcepto, nuevaEtiqueta, nuevoOriginal);

        return nuevoGasto;
    }
}