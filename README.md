README
Acest proiect reprezintă o aplicație Java care gestionează operațiuni bancare. Este organizat pe mai multe fișiere și clase, fiecare având
o funcționalitate specifică. În această documentație, sunt prezentate principalele componente ale proiectului și modul în care acestea interacționează.

Structura proiectului
1. Pachetul org.poo.PrimaryClasses
Acest pachet conține clasele de bază necesare pentru reprezentarea entităților principale ale aplicației:

Bank: reprezintă banca și gestionează utilizatorii, conturile și comenzile.
User: clasa utilizatorilor băncii, conținând informații precum emailul și conturile asociate.
Account: reprezintă un cont bancar cu proprietăți precum soldul, tipul contului și funcționalități suplimentare.
Transaction: gestionează operațiuni și rapoarte financiare legate de conturi și utilizatori.
2. Pachetul org.poo.ProcessClass
Acest pachet include clase care implementează comenzile și procesele logice:

ProcessCom: clasa principală care gestionează execuția comenzilor. Utilizează metode din alte clase pentru a procesa cererile și a genera răspunsuri.
Cum funcționează aplicația?
1. Procesarea comenzilor
Clasa ProcessCom conține metoda principală, CurrentCommand, care parcurge lista de comenzi (bank.getCommands()) și execută fiecare dintre ele. Comenzile sunt definite printr-o proprietate command și sunt procesate folosind un switch-case pentru a identifica tipul comenzii.

Exemple de comenzi:

printUsers: Afișează utilizatorii existenți.
addAccount: Adaugă un cont nou.
createCard: Creează un card nou.
deleteAccount: Șterge un cont bancar.
spendingsReport: Generează un raport de cheltuieli pentru un anumit cont.
2. Implementarea comenzilor
Fiecare comandă este implementată de o clasă specifică, precum:

CreateAccount, CreateCard pentru operațiuni de creare.
AddFunds, PayOnline, SplitPayment pentru gestionarea fondurilor și plăților.
ChangeInterest, AddInterest pentru administrarea dobânzilor.
Unele comenzi adaugă date în nodul JSON output, utilizând clase din biblioteca Jackson pentru serializarea obiectelor.

Cum se adaugă o comandă nouă?
Creează o clasă nouă pentru implementarea comenzii, implementând o metodă excute(Bank bank, Command command).
Adaugă logica specifică pentru execuția comenzii în această clasă.
Actualizează clasa ProcessCom:
Adaugă un nou case în metoda CurrentCommand.
Asociază-l cu clasa creată la pasul anterior.
Exemplu de utilizare
Un utilizator trimite o listă de comenzi către bancă sub formă de obiect JSON.
Clasa Bank încarcă comenzile în memorie.
Clasa ProcessCom procesează comenzile și generează răspunsuri în format JSON.
Răspunsurile sunt adăugate în nodul output.
Biblioteci utilizate
Jackson: pentru manipularea JSON-ului (crearea și modificarea nodurilor JSON).
Java Collections: pentru gestionarea utilizatorilor și comenzilor.
Cerinte preliminare
Java Development Kit (JDK) 11+.
Dependințe Maven pentru Jackson (com.fasterxml.jackson.core).
Detalii despre implementare
Interfața Process
Interfața Process definește metoda generală excute(Bank bank, Command command), care este implementată de fiecare clasă pentru a executa comenzile asociate. Aceasta asigură un contract uniform pentru toate tipurile de procese din cadrul aplicației.

Clasele implementate
Mai jos sunt detaliate clasele care implementează interfața Process:

CreateAccount
Această clasă gestionează crearea unui cont nou pentru un utilizator:

Metode principale:
excute(Bank bank, Command command): Creează un cont de tip economii (SavingsAccount) sau clasic (ClassicAccount) în funcție de comandă.
addTransaction(ArrayNode tran, Command command): Adaugă un nod în jurnalul tranzacțiilor, specificând că un cont a fost creat.
addReport(ArrayNode tran, Command command, Bank bank): Generează un raport pentru contul nou creat.
AddFunds
Clasa AddFunds permite adăugarea de fonduri într-un cont:

Metoda principală:
excute(Bank bank, Command command): Găsește contul asociat unui utilizator utilizând IBAN-ul și adaugă suma specificată.
CreateCard
Această clasă gestionează crearea unui card bancar clasic:

Metode principale:
excute(Bank bank, Command command): Creează un card asociat unui cont existent, atribuie un număr de card generat, și adaugă cardul în cont.
addTransaction(ArrayNode tran, Command command): Adaugă informații despre cardul nou în jurnalul tranzacțiilor.
addReport(ArrayNode tran, Command command, Bank bank, Account account, Card card, User user): Generează un raport cu detalii despre cardul creat.
CreateOneTimeCard
Clasa CreateOneTimeCard creează carduri de unică folosință:

Similarități cu CreateCard:
Aceleași metode principale.
Diferențe:
Creează obiecte de tip OneTimeCard în loc de NormalCard.
DeleteCard
Clasa DeleteCard se ocupă cu ștergerea unui card:

Metoda principală:
excute(Bank bank, Command command): Găsește cardul asociat IBAN-ului și îl elimină din lista de carduri ale contului. Creează o intrare în jurnal pentru a înregistra ștergerea.
DeleteAccount
Clasa DeleteAccount gestionează ștergerea unui cont bancar:

Metode principale:
excute(Bank bank, Command command): Verifică dacă soldul contului este zero înainte de a-l șterge. Dacă există fonduri, adaugă o tranzacție care specifică eșecul operațiunii.
addTransaction(ArrayNode tran, Command command, Bank bank): Adaugă un mesaj detaliat despre motivul eșecului (ex. "Fonduri rămase în cont").
printMessage(Command command, ArrayNode output): Generează un răspuns JSON pentru a raporta succesul sau eșecul ștergerii contului.
Detalii tehnice și funcționalități
Generarea IBAN-urilor și numerelor de carduri:

Utilizată clasa Utils pentru generarea automată a IBAN-urilor și numerelor de carduri.
Jurnalizare și rapoarte:

Fiecare operațiune adaugă intrări în jurnalul tranzacțiilor utilizatorului sau în rapoartele contului.
Tratarea erorilor:

