package org.poo.PrimaryClasses;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.poo.fileio.ExchangeInput;

import java.util.ArrayList;
@Data
@NoArgsConstructor
public class Exchange {
    private String from;
    private String to;
    private double rate;
    private int timestamp;

    public Exchange(final ExchangeInput exchange) {
        this.from = exchange.getFrom();
        this.to = exchange.getTo();
        this.rate = exchange.getRate();
        this.timestamp = exchange.getTimestamp();
    }
/** @param toFind the currency to search for in the exchange pairs. Must not be {@code null}.
 * @param bank the {@link Bank} object that holds the
 *             list of exchange rates to be searched. Must not be {@code null}.
 * @return an {@link ArrayList} containing all the
 * currencies that are part of an exchange pair with
 *         the specified {@code toFind} currency. If
 *         no matches are found, an empty list is returned.
 * @throws NullPointerException if {@code toFind} or
 * {@code bank} is {@code null}.
 */
    public ArrayList<String> contains(final String toFind, final Bank bank) {
        ArrayList<String> array = new ArrayList<>();
        for (Exchange e : bank.getExchangeRates()) {
            if (e.from.equals(toFind)) {
                array.add(e.to);
            } else {
                if (e.to.equals(toFind)) {
                    array.add(e.from);
                }
            }

        }
        return array;
    }
/** @param value the value to search for in the list. Must not be {@code null}.
 * @param array the {@link ArrayList} in which to search for the value. Must not be {@code null}.
 * @return {@code true} if the value exists in the list; {@code false} otherwise.
 * @throws NullPointerException if {@code value} or {@code array} is {@code null}.
 */
    public boolean exists(final String value, final ArrayList<String> array) {
        for (String str : array) {
            if (str.equals(value)) {
                return true;
            }
        }
        return false;
    }


/** @param bank the {@link Bank} object containing exchange rates
 *               to use for finding the path. Must not be {@code null}.
 * @param start the starting currency. Must not be {@code null}.
 * @param end the target currency. Must not be {@code null}.
 * @param amount the amount to exchange. Currently not used
 *               but could be added for more complex logic.
 * @return an {@link ArrayList} containing the sequence
 * of currencies start {@code start} to {@code to},
 *         or an empty list if no path is found.
 * @throws NullPointerException if {@code bank},
 * {@code start}, or {@code to} is {@code null}.
 */
        public ArrayList<String> goThrough(final Bank bank, final String start,
                                           final String end, final double amount) {
        ArrayList<String> way = new ArrayList<>();
        for (Exchange e : bank.getExchangeRates()) {
            if (start.equals(e.getFrom()) && end.equals(e.getTo())) {
                way.add(start);
                way.add(e.getTo());
                return way;
            }
            if (end.equals(e.getFrom()) && start.equals(e.getTo())) {
                way.add(start);
                way.add(e.getFrom());
                return way;
            }

        }

        ArrayList<String> keepInMind = new ArrayList<>(); // Lista pentru explorare
        ArrayList<String> used = new ArrayList<>(); // Lista nodurilor vizitate

        String current = start; // Începem de la nodul inițial
        keepInMind.add(current); // Adăugăm primul nod pentru explorare
        way.add(current); // Drumul începe de la primul nod

        while (!keepInMind.isEmpty()) { // Cât timp mai avem noduri de explorat
            current = keepInMind.get(keepInMind.size() - 1); // Ultimul nod din explorare

            // Dacă am ajuns la destinație
            if (current.equals(end)) {
                return way; // Returnăm drumul corect
            }

            // Găsim vecinii nodului curent
            ArrayList<String> neighbors = this.contains(current, bank);
            boolean foundUnvisitedNeighbor = false;

            for (String neighbor : neighbors) {
                if (!this.exists(neighbor, used)) { // Dacă vecinul nu a fost vizitat
                    keepInMind.add(neighbor); // Adăugăm vecinul pentru explorare
                    way.add(neighbor); // Îl adăugăm și în drumul final
                    used.add(neighbor); // Marcăm vecinul ca vizitat
                    foundUnvisitedNeighbor = true;
                    break; // Continuăm cu următorul nod
                }
            }

            // Dacă nu există vecini nevizitați, ne întoarcem
            if (!foundUnvisitedNeighbor) {
                keepInMind.remove(keepInMind.size() - 1); // Eliminăm nodul din explorare
                way.remove(way.size() - 1); // Eliminăm nodul din drumul final
            }
        }

        // Dacă nu am găsit o cale, returnăm o listă goală
        return new ArrayList<>();
    }
    /** @param bank the {@link Bank} object containing
     *               the exchange rates used for the conversion.
     *               Must not be {@code null}.
     * @param amount the amount of money to be converted. Must be
     *               greater than or equal to zero.
     * @param way a list of currencies representing the path of
     *            conversions. Each consecutive pair of currencies
     *            in the list represents an exchange from one to the
     *            other. Must not be {@code null} and should contain at
     *            least two elements.
     * @return the final converted amount after applying all exchange
     * rates along the specified path.
     *         If the exchange path is valid, the returned value will
     *         be a positive double.
     * @throws NullPointerException if {@code bank}, {@code way} is
     * {@code null}, or if any element in {@code way} is {@code null}.
     * @throws IllegalArgumentException if {@code amount} is negative
     * or if {@code way} contains fewer than two currencies.
     */
    public double convert(final Bank bank, final double amount,
                          final ArrayList<String> way) {
        double result = amount;
        for (int i = 0; i < way.size() - 1; i++) { // Parcurgem
            // fiecare pereche consecutivă de noduri
            String start = way.get(i);
            String end = way.get(i + 1);

            boolean foundExchange = false;

            for (Exchange e : bank.getExchangeRates()) {
                if (start.equals(e.getFrom()) && end.equals(e.getTo())) {
                    result = result * e.getRate();
                    foundExchange = true;
                    break; // Ieșim din buclă deoarece am găsit rata
                } else if (start.equals(e.getTo()) && end.equals(e.getFrom())) {
                    result = result * (1 / e.getRate());
                    foundExchange = true;
                    break; // Ieșim din buclă deoarece am găsit rata inversă
                }
            }


        }
        return result;
    }
}
