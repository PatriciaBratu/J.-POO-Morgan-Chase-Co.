package org.poo.ProcessClass;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.poo.PrimaryClasses.*;
import org.poo.utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OtherTransactions {
}
@Data
@NoArgsConstructor
class PayOnline implements Process {
    private ObjectNode rootNode = null;
    private double amount;
    /** @param tran the {@link ArrayNode} representing
     *               the list of transactions to which the new
     *               transaction will be added.
     *            Must not be {@code null}.
     * @param command the {@link Command} containing the details
     *                of the transaction, such as the timestamp, amount,
     *                and commerciant.
     *                Must not be {@code null}.
     * @throws IllegalArgumentException if {@code tran} or {@code command}
     * is {@code null}.
     */
    public void addTransaction(final ArrayNode tran, final Command command) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode element = objectMapper.createObjectNode();
        element.put("timestamp", command.getTimestamp());
        element.put("description", "Card payment");
               element.put("amount", amount);
        element.put("commerciant", command.getCommerciant());

        tran.add(element);
    }
    /** @param tran the {@link ArrayNode} representing
     *               the list of transactions to which the error
     *               transaction will be added.
     *            Must not be {@code null}.
     * @param command the {@link Command} containing the
     *                timestamp for the error transaction.
     *                Must not be {@code null}.
     * @throws IllegalArgumentException if {@code tran} or {@code command}
     * is {@code null}.
     */
    public void addTransacyionError(final ArrayNode tran, final Command command) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode element = objectMapper.createObjectNode();
        element.put("timestamp", command.getTimestamp());
        element.put("description", "Insufficient funds");
        tran.add(element);
    }
    /** @param tran lista de tranzacții la care
     *              va fi adăugată tranzacția de eroare.
     *            Nu trebuie să fie {@code null}.
     * @param command obiectul care conține timestamp-ul
     *               pentru tranzacția de eroare.
     *                Nu trebuie să fie {@code null}.
     * @throws IllegalArgumentException dacă unul dintre
     * argumentele {@code tran} sau {@code command} este
     * {@code null}.
     */
    public void addTransactionError(final ArrayNode tran, final Command command) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode element = objectMapper.createObjectNode();
        element.put("timestamp", command.getTimestamp());
        element.put("description", "The card is frozen");
        tran.add(element);
    }
    /** @param tran lista de tranzacții la care va fi
     *               adăugat raportul de tranzacție.
     *            Nu trebuie să fie {@code null}.
     * @param command obiectul care conține detalii
     *                despre tranzacție, inclusiv suma,
     *                descrierea și timestamp-ul.
     *                Nu trebuie să fie {@code null}.
     * @param bank obiectul care reprezintă banca,
     *             utilizat pentru a găsi contul asociat
     *             comenzii.
     *             Nu trebuie să fie {@code null}.
     * @param account obiectul care reprezintă contul
     *                asociat tranzacției, folosit pentru
     *                a verifica dacă este un cont de economii.
     *                Nu trebuie să fie {@code null}.
     * @throws IllegalArgumentException dacă unul dintre
     * argumentele {@code tran}, {@code command}, {@code bank},
     * sau {@code account} este {@code null}.
     */
    public void addReport(final ArrayNode tran,
                          final Command command, final Bank bank,
                          final Account account) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode element = objectMapper.createObjectNode();
        Account a = bank.findAccount(command.getAccount());
        String amountWithCurrency = String.format("%.1f %s",
                command.getAmount(), command.getCurrency());
        element.put("amount", amount);
        element.put("description", "Card payment");
        element.put("commerciant", command.getCommerciant());
        element.put("account", account.isSavingsAccount());
        element.put("timestamp", command.getTimestamp());
        tran.add(element);
    }
    /** @param tran lista de tranzacții la care va fi adăugat
     *              raportul comerciantului.
     *            Nu trebuie să fie {@code null}.
     * @param command obiectul care conține detalii despre tranzacție,
     *               inclusiv comerciantul, suma și timestamp-ul.
     *                Nu trebuie să fie {@code null}.
     * @param bank obiectul care reprezintă banca, folosit pentru a
     *             adăuga tranzacția în contextul bancar.
     *             Nu trebuie să fie {@code null}.
     * @throws IllegalArgumentException dacă unul dintre argumentele
     * {@code tran}, {@code command}, sau {@code bank} este {@code null}.
     */
    public void addCommerciant(final ArrayNode tran, final Command command, final Bank bank) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode element = objectMapper.createObjectNode();
        element.put("commerciant", command.getCommerciant());
        element.put("total", amount);
        element.put("timestamp", command.getTimestamp());

        tran.add(element);
    }
    /** @param tran lista de tranzacții la care va fi adăugat raportul
     *               de distrugere a cardului.
     *            Nu trebuie să fie {@code null}.
     * @param command obiectul care conține detalii despre tranzacție,
     *                inclusiv timestamp-ul și numărul cardului.
     *                Nu trebuie să fie {@code null}.
     * @param user numele utilizatorului asociat cardului care a fost
     *             distrus. Nu trebuie să fie {@code null} sau gol.
     * @param acc IBAN-ul contului asociat cardului care a fost distrus.
     *           Nu trebuie să fie {@code null} sau gol.
     * @throws IllegalArgumentException dacă unul dintre argumentele
     * {@code tran}, {@code command}, {@code user} sau {@code acc} este
     * {@code null} sau gol.
     */
    public void addDestroy(final ArrayNode tran, final Command command,
                           final String user, final String acc) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode element = objectMapper.createObjectNode();
        element.put("timestamp", command.getTimestamp());
        element.put("description", "The card has been destroyed");
        element.put("account", acc);
        element.put("card", command.getCardNumber());
        element.put("cardHolder", user);
        tran.add(element);
    }
    /** @param tran obiectul de tip {@link ArrayNode} care conține
     *              istoricul tranzacțiilor utilizatorului.
     *             Nu trebuie să fie {@code null}.
     * @param command comanda care conține informațiile de timestamp.
     *               Nu trebuie să fie {@code null}.
     * @param numCard numărul cardului care a fost creat. Nu trebuie să
     *                fie {@code null} sau gol.
     * @param acc IBAN-ul contului asociat cardului nou creat. Nu trebuie
     *           să fie {@code null} sau gol.
     * @param user email-ul utilizatorului care deține cardul. Nu trebuie
     *             să fie {@code null} sau gol.
     * @throws IllegalArgumentException dacă unul dintre argumente este
     * {@code null} sau gol.
     */
    public void addCreate(final ArrayNode tran, final Command command,
                          final String numCard, final String acc, final String user) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode element = objectMapper.createObjectNode();
        element.put("timestamp", command.getTimestamp());
        element.put("description", "New card created");
        element.put("cardHolder", user);
        element.put("card", numCard);
        element.put("account", acc);
        tran.add(element);
    }
    private Card copyCard = new Card();
    /** @param bank obiectul bancar care conține utilizatorii
     *              și conturile bancare. Nu trebuie să fie {@code null}.
     * @param command comanda care conține informațiile despre
     *                tranzacție (numărul cardului, email-ul utilizatorului,
     *                suma, moneda și alte detalii). Nu trebuie să
     *                fie {@code null}.
     * @throws IllegalArgumentException dacă unul dintre argumentele
     * {@code bank} sau {@code command} este {@code null}.
     */
    @Override
    public void excute(final Bank bank, final Command command) {
        boolean found = false;
        boolean succees = true;

        for (int i = 0; i < bank.getUsers().length; i++) {
            for (int j = 0; j < bank.getUsers()[i].getAccounts().size(); j++) {
                Account account = bank.getUsers()[i].getAccounts().get(j);
                for (Card card:account.getCards()) {
                    if (card.getCardNumber().equals(command.getCardNumber())
                            && bank.getUsers()[i].getEmail().equals(command.getEmail())) {
                        card.setNumOfUses(card.getNumOfUses() + 1);
                        found = true;
                        if (account.getCurrency().equals(command.getCurrency())) {
                            if (!account.withdraw(command.getAmount(), card, command)) {
                                if (card.getToWarn() == 0 && card.getFoFreeze() == 0) {
                                    succees = false;
                                    addTransacyionError(bank.getUsers()[i].getTran(), command);
                                } else if (card.getFoFreeze() == 1) {
                                    addTransactionError(bank.getUsers()[i].getTran(), command);
                                    succees = false;
                                }
                            } else {
                                amount = command.getAmount();
                                addTransaction(bank.getUsers()[i].getTran(), command);
                                addReport(account.getTran(), command, bank, account);
                                addCommerciant(account.getCommerciants(), command, bank);
                                copyCard = card;
                                if (!copyCard.canIUse() && succees) {
                                    copyCard = card;
                                    addDestroy(bank.getUsers()[i].getTran(),
                                            command, bank.getUsers()[i].getEmail(),
                                            account.getIBAN());
                                    account.getCards().remove(copyCard);
                                    String numCard = Utils.generateCardNumber();


                                    Card card1 = new OneTimeCard();

                                    addCreate(bank.getUsers()[i].getTran(),
                                            command, numCard, account.getIBAN(),
                                            bank.getUsers()[i].getEmail());
                                    card1.setCardNumber(numCard);
                                    account.addCard(card1);
//                                break;
                                }
                                break;
                            }
                        } else {
                            Exchange ex = new Exchange();
                            ArrayList<String> way = ex.goThrough(bank,
                                    command.getCurrency(), account.getCurrency(),
                                    command.getAmount());

                            if (!account.withdraw(ex.convert(bank,
                                    command.getAmount(), way), card, command)) {
                                if (card.getToWarn() == 0 && card.getFoFreeze() == 0) {
                                    addTransacyionError(bank.getUsers()[i].getTran(),
                                            command);
                                    succees = false;
                                } else if (card.getFoFreeze() == 1) {
                                    addTransactionError(bank.getUsers()[i].getTran(), command);
                                    succees = false;
                                }
                            } else {
                                amount = ex.convert(bank, command.getAmount(), way);
                                addTransaction(bank.getUsers()[i].getTran(), command);
                                addReport(account.getTran(), command, bank, account);
                                addCommerciant(account.getCommerciants(), command, bank);
                                copyCard = card;
                                if (!copyCard.canIUse() && succees) {
                                    copyCard = card;
                                    addDestroy(bank.getUsers()[i].getTran(),
                                            command, bank.getUsers()[i].getEmail(),
                                            account.getIBAN());
                                    account.getCards().remove(copyCard);
                                    String numCard = Utils.generateCardNumber();
                                    Card card1 = new OneTimeCard();

                                    addCreate(bank.getUsers()[i].getTran(),
                                            command, numCard, account.getIBAN(),
                                            bank.getUsers()[i].getEmail());
                                    card1.setCardNumber(numCard);
                                    account.addCard(card1);
                                }
                                break;
                            }
                        }

                                copyCard = card;
                                if (!copyCard.canIUse()&& succees) {
                                    copyCard = card;
                                    addDestroy(bank.getUsers()[i].getTran(),
                                            command, bank.getUsers()[i].getEmail(),
                                            account.getIBAN());
                                    account.getCards().remove(copyCard);
                                    String numCard = Utils.generateCardNumber();


                                    Card card1 = new OneTimeCard();

                                    addCreate(bank.getUsers()[i].getTran(),
                                            command, numCard, account.getIBAN(),
                                            bank.getUsers()[i].getEmail());
                                    card1.setCardNumber(numCard);
                                    account.addCard(card1);
//                                break;
                                }

                        }
                    }

            }
            }
        if (!found) {
            rootNode = this.printErrNotFound(command, bank);
        }
    }
/** @param command comanda care a cauzat eroarea. Nu
 *                 trebuie să fie {@code null}.
 * @param bank obiectul bancar care conține informațiile
 *             necesare pentru validarea comenzii.
 *             Nu trebuie să fie {@code null}.
 * @return un obiect {@link ObjectNode} care reprezintă structura
 * JSON a erorii.
 * @throws IllegalArgumentException dacă unul dintre parametri este
 * {@code null}.
 */
    public ObjectNode printErrNotFound(final Command command, final Bank bank) {
        ObjectMapper objectMapper = new ObjectMapper();

        // Creăm structura JSON
        ObjectNode rootNode1 = objectMapper.createObjectNode();
        rootNode1.put("command", command.getCommand());
        rootNode1.put("timestamp", command.getTimestamp());

        // Creăm nodul "output"
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("timestamp", command.getTimestamp());
        outputNode.put("description", "Card not found");

        // Adăugăm nodul "output" în structura principală
        rootNode1.set("output", outputNode);
        return rootNode1;
    }
}
class SetMinBalance implements Process {
    @Override
    public void excute(final Bank bank, final Command command) {
        for (int i = 0; i < bank.getUsers().length; i++) {
            for (int j = 0; j < bank.getUsers()[i].getAccounts().size(); j++) {
                Account account = bank.getUsers()[i].getAccounts().get(j);
                if (account.getIBAN().equals(command.getAccount())) {
                    account.setMinBalance(command.getAmount());
                }
            }
        }
    }

}
class SendMoney implements Process {
    public void addTransaction(final ArrayNode tran, final Command command,
                               final Bank bank, final Account account) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode element = objectMapper.createObjectNode();
//        Account a = bank.findAccount(command.getAccount());
        String amountWithCurrency = String.format("%.1f %s",
                command.getAmount(), account.getCurrency());
        element.put("amount", amountWithCurrency);
        element.put("description", command.getDescription());
        element.put("receiverIBAN", command.getReceiver());
        element.put("senderIBAN", command.getAccount());
        element.put("timestamp", command.getTimestamp());
        element.put("transferType", "sent");
        tran.add(element);
    }
    public void addTransacyionError(final ArrayNode tran, final Command command) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode element = objectMapper.createObjectNode();
        element.put("timestamp", command.getTimestamp());
        element.put("description", "Insufficient funds");
        tran.add(element);
    }
    public void addReportError(final ArrayNode tran, final Command command) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode element = objectMapper.createObjectNode();
        element.put("timestamp", command.getTimestamp());
        element.put("description", "Insufficient funds");
        tran.add(element);
    }
    public void addReport(final ArrayNode tran,
                           final Command command, final Bank bank,
                           final Account account) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode element = objectMapper.createObjectNode();
        String amountWithCurrency = String.format("%.1f %s",
                command.getAmount(), account.getCurrency());
        element.put("amount", amountWithCurrency);
        element.put("description", command.getDescription());
        element.put("receiverIBAN", command.getReceiver());
        element.put("senderIBAN", command.getAccount());
        element.put("timestamp", command.getTimestamp());
        element.put("transferType", "sent");
        tran.add(element);
    }
    private double amount2;
    public void addTransactionReceive(final ArrayNode tran, final Command command,
                                      final Bank bank, final Account account) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode element = objectMapper.createObjectNode();
        String amountWithCurrency;
        if (amount2 % 1 == 0) {
            amountWithCurrency = String.format("%.1f %s", amount2,
                    account.getCurrency());
        } else {
             amountWithCurrency = new BigDecimal(amount2)
                     .stripTrailingZeros().toPlainString() + " "
                     + account.getCurrency();
        }

        element.put("amount", amountWithCurrency);
        element.put("description", command.getDescription());
        element.put("receiverIBAN", command.getReceiver());
        element.put("senderIBAN", command.getAccount());
        element.put("timestamp", command.getTimestamp());
        element.put("transferType", "received");
        tran.add(element);
    }
    public void addReportReceive(final ArrayNode tran, final Command command,
                                 final Bank bank, final Account account) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode element = objectMapper.createObjectNode();
        String amountWithCurrency;
        if (amount2 % 1 == 0) {
            amountWithCurrency = String.format("%.1f %s", amount2,
                    account.getCurrency());
        } else {
            amountWithCurrency = new BigDecimal(amount2).stripTrailingZeros()
                    .toPlainString() + " " + account.getCurrency();
        }

        element.put("amount", amountWithCurrency);
        element.put("description", command.getDescription());
        element.put("receiverIBAN", command.getReceiver());
        element.put("senderIBAN", command.getAccount());
        element.put("timestamp", command.getTimestamp());
        element.put("transferType", "received");
        tran.add(element);
    }


    @Override
    public void excute(final Bank bank, final Command command) {
        Account account = bank.findAccount(command.getAccount());
        Account receiver = bank.findAccount(command.getReceiver());
        User userReceiver = null;
        for (User user : bank.getUsers()) {
            for (Account acc : user.getAccounts()) {
                if (acc.getIBAN().equals(command.getReceiver())) {
                    userReceiver = user;
                }
            }
        }
        if (userReceiver != null) {
            if (account != null && receiver != null) {
                if (account.getCurrency().equals(receiver.getCurrency())) {
                    if (!account.withdraw2((command.getAmount()))) {
                        for (int i = 0; i < bank.getUsers().length; i++) {
                            if (command.getEmail().equals(bank.getUsers()[i].getEmail())) {
                                User user = bank.getUsers()[i];
                                addTransacyionError(user.getTran(), command);
                                addReportError(account.getTran(), command);
                            }
                        }
                    } else {
                        receiver.setBalance(receiver.getBalance()
                                + command.getAmount());
                        for (int i = 0; i < bank.getUsers().length; i++) {
                            if (command.getEmail().equals(bank
                                    .getUsers()[i].getEmail())) {
                                User user = bank.getUsers()[i];
                                addTransaction(user.getTran(), command,
                                        bank, account);
                                addReport(account.getTran(), command, bank,
                                        account);
                                amount2 = command.getAmount();
                                addTransactionReceive(userReceiver.getTran(),
                                        command, bank, receiver);
                                addReportReceive(receiver.getTran(), command,
                                        bank, receiver);
                            }
                        }
                    }
                } else {
                    Exchange ex = new Exchange();


                    if (!account.withdraw2((command.getAmount()))) {
                        for (int i = 0; i < bank.getUsers().length; i++) {
                            if (command.getEmail().equals(bank.getUsers()[i]
                                    .getEmail())) {
                                User user = bank.getUsers()[i];
                                addTransacyionError(user.getTran(), command);
                                addReportError(account.getTran(), command);
                            }
                        }

                    } else {
                        ArrayList<String> way = ex.goThrough(bank,
                                account.getCurrency(), receiver.getCurrency(),
                                command.getAmount());
//
                        receiver.setBalance(receiver.getBalance()
                                + ex.convert(bank, command.getAmount(), way));
                        amount2 = ex.convert(bank, command.getAmount(), way);
                        for (int i = 0; i < bank.getUsers().length; i++) {
                            if (command.getEmail().equals(bank.getUsers()[i]
                                    .getEmail())) {
                                User user = bank.getUsers()[i];
                                addTransaction(user.getTran(), command, bank,
                                        account);
                                addReport(account.getTran(), command, bank,
                                        account);
                                addTransactionReceive(userReceiver.getTran(),
                                        command, bank, receiver);
                                addReportReceive(receiver.getTran(), command,
                                        bank, receiver);
                            }
//                            }

                        }

                    }
                }
            }
        }
    }
}

    class SetAlias implements Process {
        @Override
        public void excute(final Bank bank, final Command command) {
            Alias alias = new Alias();
            Account account = bank.findAccount(command.getAccount());
            alias.setName(command.getAlias());
            alias.setIBAN(account.getIBAN());
            for (User user : bank.getUsers()) {
                if (user.getEmail().equals(command.getEmail())) {

                    user.getAlias().add(alias);
                }

            }
            account.setAlias(command.getAlias());
        }
    }
@Data
@NoArgsConstructor
    class Status implements Process {
        private ObjectNode rootNode = null;

        public void addTransaction(final ArrayNode tran,
                                   final Command command) {

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode element = objectMapper.createObjectNode();
            element.put("timestamp", command.getTimestamp());
            element.put("description",
                    "You have reached the minimum amount of"
                            + " funds, the card will be frozen");
            tran.add(element);
        }


        public ObjectNode printErrNotFound(final Command command,
                                           final Bank bank) {
            ObjectMapper objectMapper = new ObjectMapper();

            // Creăm structura JSON
            ObjectNode rootNode1 = objectMapper.createObjectNode();
            rootNode1.put("command", command.getCommand());
            rootNode1.put("timestamp", command.getTimestamp());

            // Creăm nodul "output"
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("timestamp", command.getTimestamp());
            outputNode.put("description", "Card not found");

            // Adăugăm nodul "output" în structura principală
            rootNode1.set("output", outputNode);
            return rootNode1;
        }

    private static final int MINIMUM_DIFFERENCE = 30;
        @Override
        public void excute(final Bank bank, final Command command) {

            boolean found = false;
            for (int i = 0; i < bank.getUsers().length; i++) {
                for (int j = 0; j < bank.getUsers()[i]
                        .getAccounts().size(); j++) {
                    Account account = bank.getUsers()[i]
                            .getAccounts().get(j);
                    for (Card card : account.getCards()) {
                        if (card.getCardNumber().equals(command
                                .getCardNumber())) {
                            found = true;
                            if (card.getFoFreeze() == 1) {
                                card.setStatus("frozen");

                            }
                            if (account.getBalance()
                                    - account.getMinBalance() <= MINIMUM_DIFFERENCE
                                    && card.getFoFreeze() != 1) {

                                addTransaction(bank.getUsers()[i]
                                        .getTran(), command);

                                return;
                            }


                        }
                    }
                }
            }
            if (!found) {
                rootNode = this.printErrNotFound(command, bank);
            }
        }
    }

    class SplitPayment implements Process {
        private double amount;
        public void addTransaction(final ArrayNode tran,
                                   final Command command, final Bank bank) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode element = objectMapper.createObjectNode();
            Account a = bank.findAccount(command.getAccount());
            String amountWithCurrency = String.format("%s %.2f %s",
                    "Split payment of", command.getAmount(), command
                            .getCurrency());
            element.put("amount", amount);
            element.put("description", amountWithCurrency);
            element.put("timestamp", command.getTimestamp());
            element.put("currency", command.getCurrency());
            ArrayNode involvedAccounts = objectMapper.createArrayNode();
            for (String account : command.getAccounts()) {
                involvedAccounts.add(account);
            }
            element.set("involvedAccounts", involvedAccounts);
            tran.add(element);
        }
        private String acc = null;
        public void addTransactionError(final ArrayNode tran,
                                        final Command command, final Bank bank) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode element = objectMapper.createObjectNode();
            Account a = bank.findAccount(command.getAccount());
            String amountWithCurrency = String.format("%s %.2f %s",
                    "Split payment of", command.getAmount(), command
                            .getCurrency());

            element.put("amount", amount);
            element.put("description", amountWithCurrency);
            element.put("error", "Account "
                    + acc + " has insufficient funds for a split payment.");
            element.put("timestamp", command.getTimestamp());
            element.put("currency", command.getCurrency());
            ArrayNode involvedAccounts = objectMapper.createArrayNode();
            for (String account : command.getAccounts()) {
                involvedAccounts.add(account);
            }
            element.set("involvedAccounts", involvedAccounts);
            tran.add(element);
        }
        public void addReportError(final ArrayNode tran,
                                   final Command command, final Bank bank) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode element = objectMapper.createObjectNode();
            Account a = bank.findAccount(command.getAccount());
            String amountWithCurrency = String.format("%s %.2f %s",
                    "Split payment of", command.getAmount(),
                    command.getCurrency());
            element.put("amount", amount);
            element.put("description", amountWithCurrency);
            element.put("error", "Account " + acc
                    + " has insufficient funds for a split payment.");
            element.put("timestamp", command.getTimestamp());
            element.put("currency", command.getCurrency());
            ArrayNode involvedAccounts = objectMapper.createArrayNode();
            for (String account : command.getAccounts()) {
                involvedAccounts.add(account);
            }
            element.set("involvedAccounts", involvedAccounts);
            tran.add(element);
        }
        @Override
        public void excute(final Bank bank, final Command command) {
            List<String> accounts = command.getAccounts();
            double distributedSum = command.getAmount() / accounts.size();
            amount = distributedSum;
            boolean itsOk = true;
            for (String iban : accounts) {
                Account account = bank.findAccount(iban);
                if (account != null) {
                    if (account.getCurrency().equals(command.getCurrency())) {
                        if (account.getBalance() < distributedSum) {
                            itsOk = false;
                            acc = account.getIBAN();
                        }
                    } else {
                        Exchange ex = new Exchange();


                        ArrayList<String> way = ex.goThrough(bank,
                                command.getCurrency(), account.getCurrency(),
                                distributedSum);
                        if (account.getBalance() < ex.convert(bank,
                                distributedSum, way)) {
                            itsOk = false;
                            acc = account.getIBAN();
                        }
                    }
                }
            }
            if (!itsOk) {
                for (String iban : accounts) {
                    Account account = bank.findAccount(iban);
                    if (account != null) {
                        User user = new User();
                        User userFound = user.findUser(iban, bank);

                        addTransactionError(userFound.getTran(), command, bank);
                        addReportError(account.getTran(), command, bank);
                    }
                    }
            }
            if (itsOk) {
                for (String iban : accounts) {
                    Account account = bank.findAccount(iban);
                    if (account != null) {
                        if (account.getCurrency().equals(command.getCurrency())) {
                            if (!account.withdraw2((distributedSum))) {

                                User user = new User();
                                User userFound = user.findUser(iban, bank);
                                addTransaction(userFound.getTran(), command, bank);
                            } else {


                                User user = new User();
                                User userFound = user.findUser(iban, bank);
                                addTransaction(userFound.getTran(), command, bank);


                            }
                        } else {
                            Exchange ex = new Exchange();


                            ArrayList<String> way = ex.goThrough(bank,
                                    command.getCurrency(), account.getCurrency(),
                                    distributedSum);

                            if (!account.withdraw2(ex.convert(bank, distributedSum,
                                    way))) {
                                User user = new User();
                                User userFound = user.findUser(iban, bank);
                                addTransaction(userFound.getTran(), command, bank);
                            } else {
                                User user = new User();
                                User userFound = user.findUser(iban, bank);
                                addTransaction(userFound.getTran(), command, bank);
                            }
                        }
                    }
                }
            }
        }
    }
@Data
@NoArgsConstructor
    class ChangeInterest implements Process {
        private ObjectNode rootNode1 = null;
        public ObjectNode printErr(final Command command, final Bank bank) {
            ObjectMapper objectMapper = new ObjectMapper();

            // Creăm structura JSON
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("command", command.getCommand());
            rootNode.put("timestamp", command.getTimestamp());

            // Creăm nodul "output"
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("timestamp", command.getTimestamp());
            outputNode.put("description", "This is not a savings account");

            // Adăugăm nodul "output" în structura principală
            rootNode.set("output", outputNode);
            return rootNode;
        }
        public void addTransaction(final ArrayNode tran,
                                   final Command command, final Bank bank) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode element = objectMapper.createObjectNode();
            String amountWithCurrency = String.format("%s %.2f",
                    "Interest rate of the account changed to",
                    command.getInterestRate());
            element.put("description", amountWithCurrency);
            element.put("timestamp", command.getTimestamp());

            tran.add(element);
        }
        @Override
        public void excute(final Bank bank, final Command command) {
            Account acc = bank.findAccount(command.getAccount());
            User user1 = null;
            for (User user: bank.getUsers()) {
                for (Account a:user.getAccounts()) {
                    if (a.getIBAN().equals(command.getAccount())) {
                        user1 = user;
                    }
                }
            }
            if (user1 != null) {
                if (acc != null) {
                    if (!acc.isSavingsAccount()) {
                        rootNode1 = printErr(command, bank);
                        return;
                    } else {
                        ((SavingsAccount) acc)
                                .setInterestRate(command
                                        .getInterestRate());
                        addTransaction(user1.getTran(), command, bank);
                    }

                }
            }
        }
    }
@Data
@NoArgsConstructor
    class AddInterest implements Process {
        private ObjectNode rootNode1 = null;
        public ObjectNode printErr(final Command command,
                                   final Bank bank) {
            ObjectMapper objectMapper = new ObjectMapper();

            // Creăm structura JSON
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("command", command.getCommand());
            rootNode.put("timestamp", command.getTimestamp());

            // Creăm nodul "output"
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("timestamp", command.getTimestamp());
            outputNode.put("description",
                    "This is not a savings account");

            // Adăugăm nodul "output" în structura principală
            rootNode.set("output", outputNode);
            return rootNode;
        }
        @Override
        public void excute(final Bank bank, final Command command) {
            Account acc = bank.findAccount(command.getAccount());
            if (acc != null) {
                if (!acc.isSavingsAccount()) {
                    rootNode1 = printErr(command, bank);
                    return;
                } else {
                    acc.setBalance(acc.getBalance()
                            + acc.getBalance() * ((SavingsAccount) acc)
                            .getInterestRate());
                }

            }
        }
    }

