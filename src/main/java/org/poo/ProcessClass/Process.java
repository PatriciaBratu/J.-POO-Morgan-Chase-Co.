package org.poo.ProcessClass;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.poo.PrimaryClasses.*;
import org.poo.utils.Utils;

public interface Process {
    /**
     * @param bank reprerezinta banca
     * @param command reprezinta comanda in sine
     */

    void excute(Bank bank, Command command);
}
@Data
@NoArgsConstructor
 class CreateAccount implements Process {
//    private String command;
    private String email;
    private String currency;
    private String accountType;
    private int timestamp;
    private double interestRate;
    public void addTransaction(final ArrayNode tran,
                               final Command command) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode element = objectMapper.createObjectNode();
        element.put("timestamp", command.getTimestamp());
        element.put("description", "New account created");
        tran.add(element);
    }
    public void addReport(final ArrayNode tran, final Command command,
                          final Bank bank) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode element = objectMapper.createObjectNode();
        Account a = bank.findAccount(command.getAccount());

        element.put("description", "New account created");


        element.put("timestamp", command.getTimestamp());
        tran.add(element);
    }



     public void excute(final Bank bank, final Command command) {
         Account account;
         String iban = Utils.generateIBAN();
         if (command.getAccountType().equals("savings")) {

              account = new SavingsAccount();
              account.setSavingsAccount(true);
             ((SavingsAccount) account).setInterestRate(command
                     .getInterestRate());
              account.setIBAN(iban);
              account.setCurrency(command.getCurrency());
         } else {

              account = new ClassicAccount();
              account.setSavingsAccount(false);
              account.setIBAN(iban);
             account.setCurrency(command.getCurrency());
         }


        for (int i = 0; i < bank.getUsers().length; i++) {
            if (command.getEmail().equals(bank.getUsers()[i].getEmail())) {
                bank.getUsers()[i].addAccount(account);
                User user = bank.getUsers()[i];
                addTransaction(user.getTran(), command);
                addReport(account.getTran(), command, bank);
            }
        }

     }



}

 class AddFunds implements Process {
//    private String commmand;
    private String IBAN;
    private double amount;
    private int timestamp;
     @Override
     public void excute(final Bank bank, final Command command) {
         for (int i = 0; i < bank.getUsers().length; i++) {
             for (int j = 0; j < bank.getUsers()[i].getAccounts().size(); j++) {
                 Account account = bank.getUsers()[i].getAccounts().get(j);
                 if (account.getIBAN().equals(command.getAccount())) {
                     account.addFund(command.getAmount());
                 }
             }
         }
     }
 }
@Data
@NoArgsConstructor
 class CreateCard implements Process {
     private String numCard;
     public void addTransaction(final ArrayNode tran, final Command command) {

         ObjectMapper objectMapper = new ObjectMapper();
         ObjectNode element = objectMapper.createObjectNode();
         element.put("timestamp", command.getTimestamp());
         element.put("description", "New card created");
         element.put("cardHolder", command.getEmail());
         element.put("card", numCard);
         element.put("account", command.getAccount());
         tran.add(element);
     }
     public void addReport(final ArrayNode tran, final Command command,
                           final Bank bank, final Account account,
                           final Card card, final User user) {
         ObjectMapper objectMapper = new ObjectMapper();
         ObjectNode element = objectMapper.createObjectNode();
         Account a = bank.findAccount(command.getAccount());
         element.put("account", account.getIBAN());
         element.put("card", card.getCardNumber());
         element.put("cardHolder", user.getEmail());
         element.put("description", "New card created");


         element.put("timestamp", command.getTimestamp());
         tran.add(element);
     }
     @Override
     public void excute(final Bank bank, final Command command) {
         for (int i = 0; i < bank.getUsers().length; i++) {
             if (bank.getUsers()[i].getEmail().equals(command
                     .getEmail())) {
                 for (int j = 0; j < bank.getUsers()[i].getAccounts()
                         .size(); j++) {
                     Account account = bank.getUsers()[i].getAccounts()
                             .get(j);
                     if (account.getIBAN().equals(command.getAccount())) {
                         numCard = Utils.generateCardNumber();
                         Card card = new NormalCard();

                         card.setCardNumber(numCard);

                         account.addCard(card);
                         addTransaction(bank.getUsers()[i].getTran(),
                                 command);
                         addReport(account.getTran(), command, bank,
                                 account, card, bank.getUsers()[i]);
                     }
                 }
             }
         }
     }
 }
@Data
@NoArgsConstructor
 class CreateOneTimeCard implements Process {
     private String numCard1;
     public void addTransaction(final ArrayNode tran,
                                final Command command) {

         ObjectMapper objectMapper = new ObjectMapper();
         ObjectNode element = objectMapper.createObjectNode();
         element.put("timestamp", command.getTimestamp());
         element.put("description", "New card created");
         element.put("cardHolder", command.getEmail());
         element.put("card", numCard1);
         element.put("account", command.getAccount());
         tran.add(element);
     }
     public void addReport(final ArrayNode tran, final Command command,
                           final Bank bank, final Account account,
                           final Card card, final User user) {
         ObjectMapper objectMapper = new ObjectMapper();
         ObjectNode element = objectMapper.createObjectNode();
         Account a = bank.findAccount(command.getAccount());
         element.put("account", account.getIBAN());
         element.put("card", card.getCardNumber());
         element.put("cardHolder", user.getEmail());
         element.put("description", "New card created");


         element.put("timestamp", command.getTimestamp());
         tran.add(element);
     }
     @Override
     public void excute(final Bank bank, final Command command) {
         for (int i = 0; i < bank.getUsers().length; i++) {
             if (bank.getUsers()[i].getEmail().equals(command.getEmail())) {
                 for (int j = 0; j < bank.getUsers()[i].getAccounts().size(); j++) {
                     Account account = bank.getUsers()[i].getAccounts().get(j);
                     if (account.getIBAN().equals(command.getAccount())) {
                         numCard1 = Utils.generateCardNumber();
                         Card card = new OneTimeCard();

                         card.setCardNumber(numCard1);
                         account.addCard(card);
                         addTransaction(bank.getUsers()[i]
                                 .getTran(), command);
                        addReport(account.getTran(), command,
                                bank, account, card,  bank.getUsers()[i]);
                     }
                 }
             }
         }
     }
 }
 class DeleteCard implements Process {
     @Override
     public void excute(final Bank bank, final Command command) {
        for (int i = 0; i < bank.getUsers().length; i++) {
            for (int j = 0; j < bank.getUsers()[i].getAccounts().size(); j++) {
                Account account = bank.getUsers()[i].getAccounts().get(j);
                for (int k = 0; k < account.getCards().size(); k++) {
                    String num = account.getCards().get(k).getCardNumber();
                    if (num.equals(command.getCardNumber())) {
                        account.getCards().remove(k);
                        ObjectMapper objectMapper = new ObjectMapper();
                        ObjectNode element = objectMapper.createObjectNode();
                          element.put("timestamp",
                                  command.getTimestamp());
                        element.put("description",
                                "The card has been destroyed");
                     element.put("account", account.getIBAN());
                        element.put("card", command.getCardNumber());
                     element.put("cardHolder", command.getEmail());

                        bank.getUsers()[i].getTran().add(element);
                    }
                }
            }
        }
     }

 }
 @Data
@NoArgsConstructor
class DeleteAccount implements Process {
    private boolean success = true;
     public void addTransaction(final ArrayNode tran,
                                final Command command, final Bank bank) {
         ObjectMapper objectMapper = new ObjectMapper();
         ObjectNode element = objectMapper.createObjectNode();
         element.put("description",
                 "Account couldn't be deleted - "
                         + "there are funds remaining");
         element.put("timestamp", command.getTimestamp());

         tran.add(element);
     }
    @Override
    public void excute(final Bank bank, final Command command) {
        for (int i = 0; i < bank.getUsers().length; i++) {
            if (bank.getUsers()[i].getEmail().equals(command.getEmail())) {
                User user = bank.getUsers()[i];
                for (int j = 0; j < user.getAccounts().size(); j++) {
                    if (user.getAccounts().get(j).getIBAN()
                            .equals(command.getAccount())) {
                        if (user.getAccounts().get(j).getBalance() == 0) {
                            user.getAccounts().remove(j);
                            this.success = true;
                        } else  {
                            addTransaction(user.getTran(), command, bank);
                            this.success = false;
                        }
                    }

                }
            }
        }

    }
    public void printMessage(final Command command, final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        // Creează un obiect JSON gol

        // Adăugăm cheia "command"
        objectNode.put("command", command.getCommand());

        // Creăm un sub-obiect pentru "output"
        ObjectNode outputNode = objectMapper.createObjectNode();
        if (this.success) {
            outputNode.put("success", "Account deleted");
        } else {
            outputNode.put("error",
                    "Account couldn't be deleted - see"
                            + " org.poo.transactions for details");
        }
        outputNode.put("timestamp", command.getTimestamp());

        // Adăugăm "output" în obiectul principal
        objectNode.set("output", outputNode);

        // Adăugăm timestamp-ul separat
        objectNode.put("timestamp", command.getTimestamp());

        // Adăugăm obiectul complet la array-ul de output
        output.add(objectNode);
    }
}


