package org.poo.PrimaryClasses;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.poo.fileio.CommerciantInput;

import java.util.List;

@Data
@NoArgsConstructor
public class Commerciant {
    private int id;
    private String description;
    private List<String> commerciants;

    public Commerciant(final CommerciantInput commerciant) {
        this.id = commerciant.getId();
        this.description = commerciant.getDescription();
        this.commerciants = commerciant.getCommerciants();
    }
}
