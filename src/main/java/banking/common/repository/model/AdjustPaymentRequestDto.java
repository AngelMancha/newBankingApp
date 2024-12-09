package banking.common.repository.model;

import lombok.Data;

import java.util.List;

@Data
public class AdjustPaymentRequestDto {
    private List<Operacion> operacionesGasto;
    private List<Operacion> operacionesIngreso;
}
