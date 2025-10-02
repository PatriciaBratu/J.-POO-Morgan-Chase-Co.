package org.poo.PrimaryClasses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SavingsAccount extends Account {
    private String type = "savings";
    @JsonIgnore
    private double interestRate;

}
