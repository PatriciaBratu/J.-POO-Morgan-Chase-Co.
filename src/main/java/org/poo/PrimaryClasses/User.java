package org.poo.PrimaryClasses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.poo.fileio.UserInput;

import java.util.ArrayList;


@Data
@NoArgsConstructor
public class User {
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private ArrayList<Alias> alias;
    @JsonIgnore
    private ArrayNode tran;
    private ArrayList<Account> accounts;
    /** @param account the {@link Account} object to be added. Must not be {@code null}.
     * @throws IllegalArgumentException if the provided account is {@code null}.
     */
    public void addAccount(final Account account) {
        this.accounts.add(account);
    }

/** @param user the {@link UserInput} object containing
 *               the necessary information to initialize the user.
 *             Must not be {@code null}.
 * @throws IllegalArgumentException if the provided {@code user}
 * is {@code null}.
 */
    public User(final UserInput user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.accounts = new ArrayList<Account>();
        this.alias = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        this.tran = objectMapper.createArrayNode();

    }
    /** @param command the {@link Command} object
     *                 containing the command details (name and timestamp).
     *                Must not be {@code null}.
     * @param bank the {@link Bank} object containing the
     *            list of users whose details need to be printed.
     *             Must not be {@code null}.
     * @param output the {@link ArrayNode} where the resulting
     *              output will be added.
     *               The output will include the command details
     *               and a list of users.
     *               Must not be {@code null}.
     * @throws IllegalArgumentException if {@code command},
     * {@code bank}, or {@code output} are {@code null}.
     */
    public void printUser(final Command command, final Bank bank, final ArrayNode output) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        // Adăugăm comanda și timestamp-ul
        objectNode.put("command", command.getCommand());
        objectNode.put("timestamp", command.getTimestamp());

        // Creăm un array JSON pentru utilizatori
        ArrayNode usersArray = objectMapper.createArrayNode();

        // Iterăm prin utilizatorii din bancă
        for (int i = 0; i < bank.getUsers().length; i++) {
            User user = bank.getUsers()[i];
            usersArray.add(objectMapper.valueToTree(user)); // Adăugăm utilizatorul în array
        }

        // Setăm array-ul de utilizatori în cheia "output"
        objectNode.set("output", usersArray);

        // Adăugăm obiectul rezultat în array-ul final
        output.add(objectNode);
    }
    /** @param account the IBAN of the account for which the
     *                  user needs to be found.
     *                Must not be {@code null}.
     * @param bank the {@link Bank} in which the search for
     *             the user should be performed.
     *             Must not be {@code null}.
     * @return the {@link User} associated with the
     * given IBAN, or {@code null} if no such user exists.
     * @throws IllegalArgumentException if
     * {@code account} or {@code bank} is {@code null}.
     */
    public User findUser(final String account, final Bank bank) {
        for (User user: bank.getUsers()) {
            for (Account acc:user.getAccounts()) {
                if (acc.getIBAN().equals(account)) {
                    return user;
                }
            }
        }
        return null;
    }
}
