package org.poo.ProcessClass;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.PrimaryClasses.*;
import org.poo.PrimaryClasses.Bank;

public class ProcessCom {
        public final void CurrentCommand(final Bank bank,
                                         final ArrayNode output) {
                for (Command command : bank.getCommands()) {

                        switch (command.getCommand()) {
                                case "printUsers":
                                        User user = new User();
                                        user.printUser(command, bank, output);
                                        break;
                                case "addAccount":
                                        CreateAccount transaction1 = new CreateAccount();
                                        transaction1.excute(bank, command);
                                        break;
                                case "createCard":
                                        CreateCard transaction2 = new CreateCard();
                                        transaction2.excute(bank, command);
                                        break;
                                case "addFunds":
                                        AddFunds transaction3 = new AddFunds();
                                        transaction3.excute(bank, command);
                                        break;
                                case "deleteAccount":
                                        DeleteAccount transaction4 = new DeleteAccount();
                                        transaction4.excute(bank, command);
                                        transaction4.printMessage(command, output);
                                        break;
                                case "createOneTimeCard":
                                        CreateOneTimeCard transaction5 = new CreateOneTimeCard();
                                        transaction5.excute(bank, command);
                                        break;
                                case "deleteCard":
                                        DeleteCard transaction6 = new DeleteCard();
                                        transaction6.excute(bank, command);
                                case "setMinimumBalance":
                                        SetMinBalance transaction7 = new SetMinBalance();
                                        transaction7.excute(bank, command);
                                        break;
                                case "payOnline":
                                        PayOnline transaction8 = new PayOnline();
                                        transaction8.excute(bank, command);
                                        if (transaction8.getRootNode() != null) {
                                                output.add(transaction8.getRootNode());
                                        }
                                        break;
                                case "sendMoney" :
                                        Process transaction9 = new SendMoney();
                                        transaction9.excute(bank, command);
                                        break;
                                case "setAlias" :
                                        SetAlias transaction10 = new SetAlias();
                                        transaction10.excute(bank, command);
                                        break;
                                case "printTransactions":
                                        Transaction transaction = new Transaction();

                                        for (User user1 : bank.getUsers()) {
                                                if (user1.getEmail().equals(command
                                                        .getEmail())) {
                                                        transaction.printTransactions(user1,
                                                                command, output);
                                                        break;
                                                }
                                        }
                                        break;
                                case "checkCardStatus":
                                        Process transaction11 = new Status();
                                        transaction11.excute(bank, command);
                                        if (((Status) transaction11).getRootNode() != null) {
                                                output.add(((Status) transaction11)
                                                        .getRootNode());
                                        }
                                        break;
                                case "splitPayment":
                                        SplitPayment transaction12 = new SplitPayment();
                                        transaction12.excute(bank, command);
                                        break;
                                case "report":
                                        Transaction transaction13 = new Transaction();

                                        Account acc = bank
                                                .findAccount(command.getAccount());
                                        if (acc != null) {
                                                transaction13
                                                        .filterTransactionsByAccount(acc,
                                                                output, command);
                                        }
                                        else {
                                                transaction13
                                                        .addAccountNotFoundReport(output,
                                                                command);
                                        }
                                        break;
                                case "spendingsReport":
                                        Transaction transaction14 = new Transaction();
                                        Account acc2 = bank.findAccount(command.
                                                getAccount());

                                        if (acc2 != null) {
                                                if (!acc2.isSavingsAccount()) {
                                                        transaction14
                                                                .spendingsReport(acc2,
                                                                        output, command,
                                                                        bank);
                                                } else {
                                                        ObjectNode out = transaction14.printErrNotFound(command, bank);
                                                        output.add(out);
                                                }
                                        } else {
                                                ObjectMapper mapper = new ObjectMapper();
                                                ObjectNode outt = mapper.createObjectNode();
                                                ObjectNode out = mapper.createObjectNode();
                                                out.put("description","Account not found" );
                                                out.put("timestamp", command.getTimestamp());
                                                outt.put("command", command.getCommand());
                                                outt.set("output", out);
                                                outt.put("timestamp", command.getTimestamp());
                                                output.add(outt);
                                        }
                                        break;
                                case "changeInterestRate":
                                        Process transaction15 = new ChangeInterest();
                                        transaction15.excute(bank, command);
                                        if (((ChangeInterest) transaction15).getRootNode1() != null) {
                                                output.add(((ChangeInterest) transaction15).getRootNode1());
                                        }
                                        break;

                                case "addInterest":
                                        Process transaction16 = new AddInterest();
                                        transaction16.excute(bank, command);
                                        if (((AddInterest) transaction16).getRootNode1() != null) {
                                                output.add(((AddInterest) transaction16)
                                                        .getRootNode1());
                                        }
                                        break;
                                default:
                                        break;
                        }
                }
        }
}
