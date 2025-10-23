import java.util.HashMap;
import java.util.Map;

// Custom exception for negative deposits
class InvalidDepositException extends Exception {
    private final double amount;

    public InvalidDepositException(int accountNo, double amount) {
        super("Invalid deposit: cannot deposit " + amount + " into account " + accountNo);
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}

// Custom exception for invalid account access
class AccountNotFoundException extends Exception {
    private final int accountNo;

    public AccountNotFoundException(int accountNo) {
        super("Account not found: " + accountNo);
        this.accountNo = accountNo;
    }

    public int getAccountNo() {
        return accountNo;
    }
}

// Simple Account class
class Account {
    private final int accountNumber;
    private double balance;

    public Account(int accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    // Throws InvalidDepositException for negative amounts
    public void deposit(double amount) throws InvalidDepositException {
        if (amount < 0) {
            throw new InvalidDepositException(accountNumber, amount);
        }
        balance += amount;
    }
}

// Bank holds accounts and provides operations that throw the custom exceptions
class Bank {
    private final Map<Integer, Account> accounts = new HashMap<>();

    public void addAccount(Account acc) {
        accounts.put(acc.getAccountNumber(), acc);
    }

    // deposit throws AccountNotFoundException if account missing,
    // or InvalidDepositException if amount invalid
    public void deposit(int accountNo, double amount)
            throws AccountNotFoundException, InvalidDepositException {
        Account acc = accounts.get(accountNo);
        if (acc == null) {
            throw new AccountNotFoundException(accountNo);
        }
        acc.deposit(amount);
        System.out.println("Deposited " + amount + " into account " + accountNo
                + ". New balance: " + acc.getBalance());
    }
}

// Main program
public class BankSystem {
    public static void main(String[] args) {
        Bank bank = new Bank();
        // create one valid account
        bank.addAccount(new Account(101, 5000.0));

        // 1) Attempt to deposit a negative amount into an existing account.
        try {
            System.out.println("Attempt 1: deposit -100 into account 101");
            bank.deposit(101, -100.0); // should throw InvalidDepositException
        } catch (InvalidDepositException ide) {
            System.out.println("Handled InvalidDepositException: " + ide.getMessage());
        } catch (AccountNotFoundException anfe) {
            System.out.println("Handled AccountNotFoundException: " + anfe.getMessage());
        }

        System.out.println(); // separator

        // 2) Attempt to deposit into a non-existent account.
        try {
            System.out.println("Attempt 2: deposit 200 into account 999");
            bank.deposit(999, 200.0); // should throw AccountNotFoundException
        } catch (InvalidDepositException ide) {
            System.out.println("Handled InvalidDepositException: " + ide.getMessage());
        } catch (AccountNotFoundException anfe) {
            System.out.println("Handled AccountNotFoundException: " + anfe.getMessage());
        }

        System.out.println();
        System.out.println("Program finished normally.");
    }
}
