package banking.common.repository.model;

import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object (DTO) for adjusting payment requests.
 */
@Data
public class AdjustPaymentRequestDto {
    /**
     * The list of expense operations.
     */
    private List<Operacion> operacionesGasto;

    /**
     * The list of income operations.
     */
    private List<Operacion> operacionesIngreso;
}