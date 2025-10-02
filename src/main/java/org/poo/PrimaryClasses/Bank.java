package org.poo.PrimaryClasses;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
    public class Bank {
    private User[] users;
    private Exchange[] exchangeRates;
    private ArrayList<Command> commands = new ArrayList<>();
    private Commerciant[] commerciants;
/**
        * @param user the {@link User} to be added. Must not be {@code null}.
            * @throws NullPointerException if the provided user is {@code null}.
            */
    public void addUser(final User user) {
        if (users == null) {
            users = new User[1];
            users[0] = user;
        } else {
            User[] newUsers = new User[users.length + 1];
            System.arraycopy(users, 0, newUsers, 0, users.length);

            newUsers[users.length] = user;

            users = newUsers;
        }
    }
 /**
         * @param exchange the {@link Exchange} object to be added to the exchange rates list.
 *                 Must not be {@code null}.
            * @throws NullPointerException if the provided {@code exchange} object is {@code null}.
            * @see System#arraycopy(Object, int, Object, int, int)
 */
    public void addExchange(final Exchange exchange) {
        if (exchangeRates == null) {
            exchangeRates = new Exchange[1];
            exchangeRates[0] = exchange;
        } else {
            Exchange[] newExchange = new Exchange[exchangeRates.length + 1];
            System.arraycopy(exchangeRates, 0, newExchange, 0, exchangeRates.length);

            newExchange[exchangeRates.length] = exchange;

            exchangeRates = newExchange;
        }
    }
 /** @param commmerciant the {@link Commerciant} object to be added to the commerciants list.
            *                     Must not be {@code null}.
            * @throws NullPointerException if
  * the provided {@code commmerciant} object is {@code null}.
            * @see System#arraycopy(Object, int, Object, int, int)
 */
    public void addCommerciants(final Commerciant commmerciant) {
        if (commerciants == null) {
            commerciants = new Commerciant[1];
            commerciants[0] = commmerciant;
        } else {
            Commerciant[] newCommerciant = new Commerciant[commerciants.length + 1];
            System.arraycopy(commerciants, 0, newCommerciant, 0, commerciants.length);

            newCommerciant[exchangeRates.length] = commmerciant;

            commerciants = newCommerciant;
        }
    }
/** @param command the {@link Command} object to be added to the list. Must not be {@code null}.
            * @throws NullPointerException if the provided {@code command} is {@code null}.
            * @see Command

 */
    public void addCommand(final Command command) {

        this.commands.add(command);
    }
 /**
         * @param acc the IBAN of the account to search for.
  *           Must not be {@code null}.
            * @return the {@link Account}
  * with the matching IBAN, or {@code null} if
  * no such account is found.
 * @throws NullPointerException if the provided {@code acc} is {@code null}.
            * @see Account
 */
    public Account findAccount(final String acc) {
        Account account = new Account();
        for (int i = 0; i < this.getUsers().length; i++) {
            for (int j = 0; j < this.getUsers()[i].getAccounts().size(); j++) {
                 account = this.getUsers()[i].getAccounts().get(j);
                if (account.getIBAN().equals(acc)) {
                    return account;
                }
            }
        }
        return null;

    }
}
