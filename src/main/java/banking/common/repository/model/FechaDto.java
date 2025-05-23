package banking.common.repository.model;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for date information.
 */
@Data
public class FechaDto {
    /**
     * The month of the date.
     */
    private String month;

    /**
     * The year of the date.
     */
    private String year;
}