package banking.adjustpayment.usecase;

import banking.common.repository.OperacionesRepository;
import banking.common.repository.model.Operacion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@RequiredArgsConstructor
@Service
public class adjustPaymentUseCase implements adjustPaymentUseCaseInterface {
    private final OperacionesRepository operacionesRepository;

    @Override
    public Operacion execute(String fechaOperacionG, Double importeG, Double saldoG, String conceptoG, String etiquetaG, List<Operacion> operacionesIngreso) {
        String nuevaEtiqueta = etiquetaG;
        String nuevaFecha = fechaOperacionG;
        String nuevoConcepto = conceptoG;


        Double nuevoImporte = importeG;
        for(Operacion ingreso: operacionesIngreso){
            nuevoImporte += ingreso.getImporte();
        }

        Double nuevoSaldo = 0.0;
        String nuevoOriginal = "no";
        Operacion nuevoGasto = new Operacion();
        nuevoGasto.setFechaOperacion(Timestamp.valueOf(nuevaFecha));
        nuevoGasto.setImporte(nuevoImporte);
        nuevoGasto.setSaldo(nuevoSaldo);
        nuevoGasto.setConcepto(nuevoConcepto);
        nuevoGasto.setEtiqueta(nuevaEtiqueta);


        //actualizar en la base de datos los 2 objetos y crear el nuevo:
        operacionesRepository.updateTag(Timestamp.valueOf(fechaOperacionG), importeG, saldoG, conceptoG, "ASUMIDO");

        for(Operacion ingreso: operacionesIngreso){
            operacionesRepository.updateTag(ingreso.getFechaOperacion(), ingreso.getImporte(), ingreso.getSaldo(), ingreso.getConcepto(), "ASUMIDO");
        }
        operacionesRepository.insertOperacion(Timestamp.valueOf(nuevaFecha), nuevoImporte, nuevoSaldo, nuevoConcepto, nuevaEtiqueta, nuevoOriginal);

        return nuevoGasto;


    }
}
