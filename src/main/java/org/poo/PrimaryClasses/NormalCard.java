package org.poo.PrimaryClasses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NormalCard extends Card {
     /** @return {@code true} if the current
      * object can be used according to the
      * superclass's implementation,
            *         otherwise {@code false}.
            */
    @Override
    public boolean canIUse() {
        return super.canIUse();

    }
}
