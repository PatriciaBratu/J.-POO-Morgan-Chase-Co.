This project is a Java application that manages banking operations. It is organized into multiple files and classes, each with a specific functionality. Below is a detailed description of the main components and how they interact.

Project Structure
1. Package org.poo.PrimaryClasses
This package contains the core classes representing the main entities of the application:
Bank: Represents the bank and manages users, accounts, and commands.
User: Represents a bank user, storing information such as email and associated accounts.
Account: Represents a bank account with properties like balance, account type, and additional functionalities.
Transaction: Handles account operations and financial reporting for users.
2. Package org.poo.ProcessClass
This package contains classes that implement commands and logical processes:
ProcessCom: The main class responsible for executing commands. It uses methods from other classes to process requests and generate responses.
How the Application Works
1. Command Processing
ProcessCom contains the main method, CurrentCommand, which iterates through the list of commands (bank.getCommands()) and executes each one. Commands are identified using a command property and processed through a switch-case statement.
Example Commands:
printUsers: Displays existing users.
addAccount: Adds a new account.
createCard: Creates a new card.
deleteAccount: Deletes a bank account.
spendingsReport: Generates a spending report for a specific account.
2. Command Implementation
Each command is implemented in a specific class:
CreateAccount, CreateCard → for creating accounts and cards.
AddFunds, PayOnline, SplitPayment → for managing funds and payments.
ChangeInterest, AddInterest → for interest management.
Some commands update the JSON output node using Jackson classes for object serialization.
Adding a New Command
Create a new class implementing the method execute(Bank bank, Command command).
Add the logic for executing the command in this class.
Update ProcessCom:
Add a new case in the CurrentCommand method.
Associate it with the new class created in step 1.
Example Usage
A user sends a list of commands to the bank in JSON format.
The Bank class loads the commands into memory.
ProcessCom processes the commands and generates JSON responses.
Responses are added to the output node.
Libraries Used
Jackson: for JSON manipulation (creating and updating JSON nodes).
Java Collections: for managing users and commands.
Prerequisites
Java Development Kit (JDK) 11 or higher.
Maven dependencies for Jackson (com.fasterxml.jackson.core).
Implementation Details
Interface Process
The Process interface defines the general method execute(Bank bank, Command command), which is implemented by each class to execute associated commands. This ensures a uniform contract for all processes within the application.
Key Classes Implementing Process
CreateAccount
Handles the creation of new accounts:
execute(Bank bank, Command command): Creates either a SavingsAccount or ClassicAccount depending on the command.
addTransaction(ArrayNode tran, Command command): Adds a transaction log entry for account creation.
addReport(ArrayNode tran, Command command, Bank bank): Generates a report for the new account.
AddFunds
Adds funds to an account:
execute(Bank bank, Command command): Finds the account by IBAN and adds the specified amount.
CreateCard
Manages the creation of classic bank cards:
execute(Bank bank, Command command): Creates a card linked to an existing account, generates a card number, and adds it to the account.
addTransaction(ArrayNode tran, Command command): Adds a transaction log entry.
addReport(ArrayNode tran, Command command, Bank bank, Account account, Card card, User user): Generates a detailed card creation report.
CreateOneTimeCard
Creates one-time-use cards (similar to CreateCard) but generates OneTimeCard objects instead of normal cards.
DeleteCard
Handles card deletion:
execute(Bank bank, Command command): Finds the card by IBAN and removes it from the account, creating a transaction log entry.
DeleteAccount
Manages account deletion:
execute(Bank bank, Command command): Ensures the account balance is zero before deletion; logs failure if funds remain.
addTransaction(ArrayNode tran, Command command, Bank bank): Adds detailed messages for failed deletions.
printMessage(Command command, ArrayNode output): Generates a JSON response indicating success or failure.
Technical Details & Features
IBAN and Card Number Generation: Uses the Utils class for automatic generation.
Logging and Reports: Every operation adds entries to the user’s transaction log or account reports.
Error Handling: Commands validate actions and provide detailed feedback in JSON.
