package banking.common.repository.model;

import lombok.Data;

import java.util.List;

@Data
public class AdjustPaymentRequestDto {
    private Operacion operacionGasto;
    private List<Operacion> operacionesIngreso;
}
