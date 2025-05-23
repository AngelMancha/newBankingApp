package banking.changetag.usecase;

import banking.common.repository.OperacionesRepository;
import banking.common.repository.model.Operacion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Service class for changing the tag of an operation.
 */
@Service
@RequiredArgsConstructor
public class ChangeTagUseCase implements ChangeTagUseCaseInterface {

    private final OperacionesRepository operacionesRepository;

    /**
     * Executes the change tag use case.
     *
     * @param fechaOperacion the date of the operation
     * @param importe        the amount of the operation
     * @param concepto       the concept of the operation
     * @param etiqueta       the new tag for the operation
     * @return the updated operation
     */
    @Override
    public Operacion execute(String fechaOperacion, Double importe, String concepto, String etiqueta) {
        Timestamp fechaOperacionTimestamp = Timestamp.valueOf(fechaOperacion);
        operacionesRepository.updateTag(fechaOperacionTimestamp, importe, concepto, etiqueta);
        System.out.println("Tag changed");

        Operacion nuevaOperacion = new Operacion();
        nuevaOperacion.setFechaOperacion(fechaOperacionTimestamp);
        nuevaOperacion.setImporte(importe);
        nuevaOperacion.setConcepto(concepto);
        nuevaOperacion.setEtiqueta(etiqueta);
        return nuevaOperacion;
    }
}