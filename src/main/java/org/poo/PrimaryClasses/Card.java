package org.poo.PrimaryClasses;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class Card {
    @JsonIgnore
    private String account;
    private String cardNumber;
    private String status = "active";
    @JsonIgnore
    private int foFreeze = 0;
    @JsonIgnore
    private int toWarn = 0;
    @JsonIgnore
    private int numOfUses = 0;
    @JsonIgnore
    private ArrayList<Command> impossibleComm = new ArrayList<>();
    @JsonIgnore
    private ArrayList<Command> warningCom = new ArrayList<>();
    /** </p>
            *
            * @return {@code true} if
     * the action can be performed; otherwise,
     * it will return {@code false}.
            */
    public boolean canIUse() {
        return true;
    }
}
