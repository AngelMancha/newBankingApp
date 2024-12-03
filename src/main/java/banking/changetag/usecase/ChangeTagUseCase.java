package banking.changetag.usecase;

import banking.common.repository.OperacionesRepository;
import banking.common.repository.model.Operacion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class ChangeTagUseCase implements ChangeTagUseCaseInterface {

    private final OperacionesRepository operacionesRepository;

    @Override
    public Operacion execute(String fechaOperacion, Double importe, Double saldo, String concepto, String etiqueta) {
        Timestamp fechaOperacionTimestamp = Timestamp.valueOf(fechaOperacion);
        operacionesRepository.updateTag(fechaOperacionTimestamp, importe, saldo, concepto, etiqueta);
        System.out.println("Tag changed");

        Operacion nuevaOperacion = new Operacion();
        nuevaOperacion.setFechaOperacion(fechaOperacionTimestamp);
        nuevaOperacion.setImporte(importe);
        nuevaOperacion.setSaldo(saldo);
        nuevaOperacion.setConcepto(concepto);
        nuevaOperacion.setEtiqueta(etiqueta);
        return nuevaOperacion;

    }
}