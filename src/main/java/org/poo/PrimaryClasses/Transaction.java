package org.poo.PrimaryClasses;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Transaction {
/**
        * @param user the {@link User} whose transactions are
 *             to be printed. Must not be {@code null}.
            * @param command the {@link Command} containing the
 *               timestamp up to which transactions are to be included.
 *                          Must not be {@code null}.
            * @param output the {@link ArrayNode} to which the filtered
 *              transactions will be added. Must not be {@code null}.
            *
            * @throws IllegalArgumentException if any transaction node
 * is not of type {@link ObjectNode}.
            */
    public void printTransactions(final User user, final Command command, final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultNode = objectMapper.createObjectNode();

        // Adăugăm informațiile comenzii
        resultNode.put("command", command.getCommand());
        resultNode.put("timestamp", command.getTimestamp());

        // Creăm array-ul "output" pentru tranzacții
        ArrayNode transactionsArray = objectMapper.createArrayNode();
        // Iterare prin elementele din ArrayNode (user.getTran())
        for (JsonNode transactionNode : user.getTran()) {
            // Verifică dacă nodul curent este de tip ObjectNode
            if (transactionNode.isObject()) {
                ObjectNode transaction = (ObjectNode) transactionNode;

                // Compară timestamp-ul
                if (transaction.get("timestamp").asInt() <= command.getTimestamp()) {
                    // Creează o copie a tranzacției
                    ObjectNode filteredTransaction = objectMapper.createObjectNode();
                    filteredTransaction.setAll(transaction); // Copiază toate câmpurile

                    // Verifică dacă `description` este "Card payment"
                    if (transaction.has("description") && transaction.get("description").asText()
                            .equals("Card payment")) {
                        filteredTransaction.remove("account");
                    }

                    // Adaugă tranzacția (filtrată) la array-ul rezultat
                    transactionsArray.add(filteredTransaction);
                }
            } else {
                throw new IllegalArgumentException("Invalid transaction format. "
                        + "Expected ObjectNode.");
            }
        }

        // Adăugăm tranzacțiile filtrate în rezultatul final
        resultNode.set("output", transactionsArray);
        resultNode.put("timestamp", command.getTimestamp());
        resultNode.put("command", command.getCommand());

        // Adăugăm rezultatul final la output
        output.add(resultNode);
    }

/** @param bank the {@link Bank} object that contains the list of users. Must not be {@code null}.
 * @param command the {@link Command}
 *                object that contains the email to identify the
 *                user. Must not be {@code null}.
 *
 * @return an {@link ArrayNode} containing the transactions of the
 * user, or {@code null} if no user with
 * the specified email is found in the bank.
 *
 * @throws IllegalArgumentException if the provided email is not
 * found in any user of the bank.
 */
    public ArrayNode add(final Bank bank, final Command command) {

        for (int i = 0; i < bank.getUsers().length; i++) {
            if (command.getEmail().equals(bank.getUsers()[i].getEmail())) {
                return bank.getUsers()[i].getTran();
            }
        }
        return null;
    }
/** @param account the {@link Account} whose transactions
 *                 are to be filtered. Must not be {@code null}.
 * @param output the {@link ArrayNode} to which the filtered
 *               transactions will be added. Must not be {@code null}.
 * @param command the {@link Command} containing the timestamp range
 *               (start and end) for filtering the transactions. Must
 *                not be {@code null}.
 *
 * @throws IllegalArgumentException if any transaction node is not of
 * type {@link ObjectNode}.
 */
    public void filterTransactionsByAccount(final Account account,
                                            final ArrayNode output,
                                            final Command command) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultNode = objectMapper.createObjectNode();

        // Adăugăm câmpurile globale (în afara lui output)
        resultNode.put("command", command.getCommand());
        resultNode.put("timestamp", command.getTimestamp());

        // Creăm nodul "output"
        ObjectNode outputNode = objectMapper.createObjectNode();

        // Adăugăm balanța, moneda și IBAN-ul în "output"
        outputNode.put("balance", account.getBalance());
        outputNode.put("currency", account.getCurrency());
        outputNode.put("IBAN", account.getIBAN());

        // Creăm array-ul pentru tranzacțiile contului
        ArrayNode transactionsArray = objectMapper.createArrayNode();

        // Iterăm prin tranzacțiile contului
        for (JsonNode transactionNode : account.getTran()) {
            if (transactionNode.isObject()) {
                ObjectNode transaction = (ObjectNode) transactionNode;

                // Verificăm dacă tranzacția are câmpul "timestamp"
                if (transaction.has("timestamp")) {
                    int transactionTimestamp = transaction.get("timestamp")
                            .asInt();
                    // Verificăm dacă timestamp-ul tranzacției este în intervalul specificat
                    if (transactionTimestamp >= command.getStartTimestamp()
                            && transactionTimestamp
                            <= command.getEndTimestamp()) {
                        // Adăugăm tranzacțiile relevante
                        ObjectNode transactionDetails = objectMapper.createObjectNode();

                        // Dacă tranzacția conține un "card"
                        if (transaction.has("card")) {
                            transactionDetails.put("account", account.getIBAN());
                            transactionDetails.put("card", transaction
                                    .get("card").asText());
                            transactionDetails.put("cardHolder", transaction
                                    .get("cardHolder").asText());
                            transactionDetails.put("description", transaction
                                    .get("description").asText());
                            transactionDetails.put("timestamp", transactionTimestamp);
                        } else {


                            if (transaction.has("commerciant")) {
                                transactionDetails.put("amount", transaction
                                        .get("amount").asDouble());
                                transactionDetails.put("commerciant", transaction
                                        .get("commerciant").asText());
                                transactionDetails.put("description", transaction
                                        .get("description").asText());
                                transactionDetails.put("timestamp", transactionTimestamp);
                            } else {
//                                && "sent".equals(transaction.get("transferType").asText()
                                if (transaction.has("receiverIBAN")) {
                                    transactionDetails.put("amount",
                                            transaction.get("amount").asText());
                                    transactionDetails.put("receiverIBAN",
                                            transaction
                                            .get("receiverIBAN").asText());
                                    transactionDetails.put("senderIBAN", transaction
                                            .get("senderIBAN").asText());
                                    transactionDetails.put("description", transaction
                                            .get("description").asText());
                                    transactionDetails.put("transferType", transaction
                                            .get("transferType").asText());
                                    transactionDetails.put("timestamp",
                                            transactionTimestamp);
                                } else {
                                    if (transaction.has("description")
                                            && "Insufficient funds"
                                            .equals(transaction.get("description")
                                                    .asText())) {
                                        transactionDetails.put("description", transaction
                                                .get("description").asText());
                                        transactionDetails.put("timestamp", transactionTimestamp);
                                    } else {
                                        if (transaction.has("involvedAccounts")) {
//
                                            transactionDetails.put("amount", transaction
                                                    .get("amount").asDouble());
                                            transactionDetails.put("currency", transaction
                                                    .get("currency").asText());
                                            transactionDetails.put("error", transaction
                                                    .get("error").asText());

                                            transactionDetails.put("description", transaction
                                                    .get("description").asText());
//
                                            ArrayNode involvedAccountsArray
                                                    = objectMapper.createArrayNode();
                                            for (JsonNode accountNode : transaction
                                                    .get("involvedAccounts")) {
                                                involvedAccountsArray.add(accountNode.asText());
                                            }
                                            transactionDetails.set("involvedAccounts",
                                                    involvedAccountsArray);
                                            transactionDetails.put("timestamp",
                                                    transactionTimestamp);
                                        } else {

                                            transactionDetails.put("description",
                                                    transaction
                                                    .get("description").asText());
                                            transactionDetails.put("timestamp",
                                                    transactionTimestamp);


                                        }
                                    }
                                }
                        }
                        }
                        transactionsArray.add(transactionDetails);
                    }
                }
            }
        }

        // Adăugăm array-ul de tranzacții în "output"
        outputNode.set("transactions", transactionsArray);

        // Adăugăm "output" în resultNode
        resultNode.set("output", outputNode);

        // Adăugăm rezultatul în array-ul de output
        output.add(resultNode);
    }
    /** @param account the {@link Account} for which the
     spending report is generated. Must not be {@code null}.
     * @param output the {@link ArrayNode} to which the
     spending report will be added. Must not be {@code null}.
     * @param command the {@link Command} that contains the
      timestamp range (start and end) for filtering transactions.
       Must not be {@code null}.
     * @param bank the {@link Bank} which is used to find the
     account. Must not be {@code null}.
     *
     * @throws IllegalArgumentException if the account is a
     savings account or if the transactions are in an unexpected format.
     */
    public void spendingsReport(final Account account, final ArrayNode output,
                                final Command command, final Bank bank) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultNode = objectMapper.createObjectNode();
        Account acc = bank.findAccount(command.getAccount());
        if (!acc.isSavingsAccount()) {
            // Adăugăm câmpurile globale (în afara lui output)
            resultNode.put("command", command.getCommand());
            resultNode.put("timestamp", command.getTimestamp());

            // Creăm nodul "output"
            ObjectNode outputNode = objectMapper.createObjectNode();
            ArrayNode commerciantsArray = objectMapper.createArrayNode();
            // Adăugăm balanța, moneda și IBAN-ul în "output"
            outputNode.put("balance", account.getBalance());
            outputNode.put("currency", account.getCurrency());
            outputNode.put("IBAN", account.getIBAN());

            // Creăm array-ul pentru tranzacțiile contului
            ArrayNode transactionsArray = objectMapper.createArrayNode();

            // Iterăm prin tranzacțiile contului
            for (JsonNode transactionNode : account.getTran()) {
                if (transactionNode.isObject()) {
                    ObjectNode transaction = (ObjectNode) transactionNode;

                    // Verificăm dacă tranzacția are câmpul "timestamp"
                    if (transaction.has("timestamp")) {
                        int transactionTimestamp = transaction
                                .get("timestamp").asInt();

                        if (transactionTimestamp >= command.getStartTimestamp()
                                && transactionTimestamp <= command
                                .getEndTimestamp()) {

                            // Adăugăm tranzacțiile relevante
                            ObjectNode transactionDetails = objectMapper
                                    .createObjectNode();

                            if (transaction.has("commerciant")) {
                                    transactionDetails.put("amount", transaction
                                            .get("amount").asDouble());
                                    transactionDetails.put("commerciant", transaction
                                            .get("commerciant").asText());
                                    transactionDetails.put("description", transaction
                                            .get("description").asText());
                                    transactionDetails.put("timestamp", transactionTimestamp);
                                    transactionsArray.add(transactionDetails);

                            }
                        }


                    }
                }
            }

            // Adăugăm array-ul de tranzacții în "output"
            outputNode.set("transactions", transactionsArray);


            for (JsonNode commerciantNode : account.getCommerciants()) {
                if (commerciantNode.isObject()) {
                    ObjectNode transaction = (ObjectNode) commerciantNode;

                    // Verificăm dacă tranzacția are câmpul "timestamp"
                    if (transaction.has("timestamp")) {
                        int transactionTimestamp = transaction.get("timestamp")
                                .asInt();

                        if (transactionTimestamp >= command.getStartTimestamp()
                                && transactionTimestamp <= command.getEndTimestamp()) {
                            // Adăugăm tranzacțiile relevante
                            ObjectNode transactionDetails = objectMapper
                                    .createObjectNode();



                            if (transaction.has("commerciant")) {
                                transactionDetails.put("total", transaction.
                                        get("total").asDouble());
                                transactionDetails.put("commerciant", transaction.
                                        get("commerciant").asText());
                                commerciantsArray.add(transactionDetails);
                            }
                        }


                    }
                }
            }
            List<ObjectNode> commerciantsList = new ArrayList<>();
            for (JsonNode node : commerciantsArray) {
                if (node.isObject()) {
                    commerciantsList.add((ObjectNode) node);
                }
            }

// Sortăm lista după "total" în ordine descrescătoare
            commerciantsList.sort((o1, o2) -> {
                String commerciant1 = o1.get("commerciant").asText();
                String commerciant2 = o2.get("commerciant").asText();
                return commerciant1.compareTo(commerciant2); // Descrescător
            });

// Reconstruim ArrayNode sortat
            commerciantsArray.removeAll(); // Golim array-ul original
            for (ObjectNode node : commerciantsList) {
                commerciantsArray.add(node);
            }
            outputNode.set("commerciants", commerciantsArray);
            // Adăugăm "output" în resultNode
            resultNode.set("output", outputNode);

            // Adăugăm rezultatul în array-ul de output
            output.add(resultNode);
        }
    }
    /** @param output the {@link ArrayNode}
    where the report will be added. Must not be {@code null}.
     * @param command the {@link Command} containing
     the timestamp and other necessary information for the report. Must not be {@code null}.
     */
    public void addAccountNotFoundReport(final ArrayNode output, final Command command) {
        ObjectMapper objectMapper = new ObjectMapper();

        // Creăm obiectul "output"
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("description", "Account not found");
        outputNode.put("timestamp", command.getTimestamp());

        // Creăm obiectul principal
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("command", "report");
        resultNode.set("output", outputNode);  // Adăugăm "output" în răspuns
        resultNode.put("timestamp", command.getTimestamp());
        // Adăugăm rezultatul în ArrayNode-ul tran
        output.add(resultNode);
    }
    /** @param command the {@link Command} object
     *                  containing the command and timestamp for the report.
     *                Must not be {@code null}.
     * @return the generated JSON structure as an
     * {@link ObjectNode}. The root node contains the command,
     *         timestamp, and error message.
     */
    public ObjectNode printErrNotFound(final Command command, final Bank bank) {
        ObjectMapper objectMapper = new ObjectMapper();

        // Creăm structura JSON
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("command", command.getCommand());
        rootNode.put("timestamp", command.getTimestamp());

        // Creăm nodul "output"
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("error",
                "This kind of report is not supported for a saving account");

        // Adăugăm nodul "output" în structura principală
        rootNode.set("output", outputNode);
        return rootNode;
    }



}
