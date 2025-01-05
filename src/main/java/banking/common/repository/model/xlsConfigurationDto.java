package banking.common.repository.model;

import lombok.Data;

@Data
public class xlsConfigurationDto {
    private String fechaNombre;
    private String fechaCelda;
    private String conceptoNombre;
    private String conceptoCelda;
    private String importeNombre;
    private String importeCelda;
    private String headerRow;
}