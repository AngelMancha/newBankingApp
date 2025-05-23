package banking.common.repository.model;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for XLS configuration.
 */
@Data
public class xlsConfigurationDto {

    /**
     * The cell reference for the date field.
     */
    private String fechaCelda;

    /**
     * The cell reference for the concept field.
     */
    private String conceptoCelda;

    /**
     * The cell reference for the amount field.
     */
    private String importeCelda;

    /**
     * The row number of the header.
     */
    private String headerRow;
}