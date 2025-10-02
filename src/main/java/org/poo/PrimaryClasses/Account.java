package org.poo.PrimaryClasses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class Account {
    private String currency;
    private String type;
    @JsonProperty("IBAN")
    private String IBAN;
    private double balance;
    @JsonIgnore
    private boolean isSavingsAccount;
    @JsonIgnore
    private double minBalance;
    @JsonIgnore
    private String alias;
    @JsonIgnore
    private ArrayNode tran = new ObjectMapper().createArrayNode();
    @JsonIgnore
    private ArrayNode commerciants = new ObjectMapper().createArrayNode();
    private static final int MINIMUM_DIFFERENCE = 30;
    /**
     * Adds the specified amount to the account balance.
     * <p>
     * This method retrieves the current balance using {@link #getBalance()},
     * adds the provided amount to it, and updates the balance using {@link #setBalance(double)}.
     * </p>
     *
     * @param amount the amount to be added to the current balance.
     *               Must be a positive value to increase the account balance.
     * @throws IllegalArgumentException if the specified amount is negative.
     * @see #getBalance()
     * @see #setBalance(double)
     */

    public void addFund(final double amount) {
        this.setBalance(this.getBalance() + amount);
    }
    /**
     * Attempts to withdraw the specified amount from the account balance.
     * <p>
     * This method checks if the current balance is sufficient to cover the withdrawal amount.
     * If the balance is sufficient, the amount is deducted from the balance, and the method
     * returns {@code true}. Otherwise, no changes are made to the balance, and the method
     * returns {@code false}.
     * </p>
     *
     * @param amount the amount to be withdrawn. Must be a positive value.
     * @return {@code true} if the withdrawal was successful (sufficient balance),
     *         {@code false} otherwise.
     * @throws IllegalArgumentException if the specified amount is negative.
     * @see #getBalance()
     * @see #setBalance(double)
     */
    public boolean withdraw2(final double amount) {
        if (this.getBalance() >= amount) {
            this.setBalance(this.getBalance() - amount);
            return true;
        } else {
            return false;
        }
    }
    /**
    * @param amount  the amount to withdraw. Must be a positive value.
 * @param card    the {@link Card} object associated with the withdrawal operation.
            * @param command the {@link Command} representing this withdrawal operation.
            * @return {@code true} if the withdrawal is successful, {@code false} otherwise.
 * @throws IllegalArgumentException if the withdrawal amount is negative.
 * @see #getBalance()
 * @see #setBalance(double)
 * @see Card#setFoFreeze(int)
 * @see Card#setToWarn(int)
 */
    public boolean withdraw(final double amount, final Card card, final Command command) {
        if (this.getBalance() - amount >= this.getMinBalance()
                && card.getFoFreeze() == 0
                && card.getToWarn() == 0) {
            this.setBalance(this.getBalance() - amount);
            if (getBalance() - getMinBalance() <= MINIMUM_DIFFERENCE) {
                card.setToWarn(1);
                card.getWarningCom().add(command);
            }
            return true;
        } else {
            if (this.getBalance() - amount < this.getMinBalance()
                    && this.getBalance() - amount >= 0
                    && card.getFoFreeze() == 0
                    && card.getToWarn() == 0) {
                    card.setFoFreeze(1);
                    card.getImpossibleComm().add(command);
                    return false;

            }
        }
        return false;
    }
    private ArrayList<Card> cards = new ArrayList<>();
    /** @param card the {@link Card} to be added to this account. Must not be {@code null}.
            * @throws NullPointerException if the provided card is {@code null}.
            * @see Card
 * @see java.util.List
 */
    public void addCard(final Card card) {
        this.cards.add(card);
    }
}
