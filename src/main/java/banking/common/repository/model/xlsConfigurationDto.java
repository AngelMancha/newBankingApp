package banking.common.repository.model;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for XLS configuration.
 */
@Data
public class xlsConfigurationDto {
    /**
     * The name of the date field.
     */
    private String fechaNombre;

    /**
     * The cell reference for the date field.
     */
    private String fechaCelda;

    /**
     * The name of the concept field.
     */
    private String conceptoNombre;

    /**
     * The cell reference for the concept field.
     */
    private String conceptoCelda;

    /**
     * The name of the amount field.
     */
    private String importeNombre;

    /**
     * The cell reference for the amount field.
     */
    private String importeCelda;

    /**
     * The row number of the header.
     */
    private String headerRow;
}