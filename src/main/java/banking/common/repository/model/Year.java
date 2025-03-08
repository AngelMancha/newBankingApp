package banking.common.repository.model;

import lombok.Data;

/**
 * Represents the expenses for each month of a year.
 */
@Data
public class Year {
    private double january;
    private double february;
    private double march;
    private double april;
    private double may;
    private double june;
    private double july;
    private double august;
    private double september;
    private double october;
    private double november;
    private double december;
}