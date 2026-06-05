# 🏦 JDBC Banking System

## Week 2 — Assignment 2: JDBC & Transaction Management

A console-based **Banking System** built in Java using JDBC for MySQL database integration. Supports full CRUD operations on customer accounts and implements transaction management with commit/rollback for deposits, withdrawals, and fund transfers.

---

## 📌 About

This application connects to a MySQL database and performs banking operations through an interactive console menu. All financial transactions (deposit, withdraw, transfer) use proper transaction management — if any step fails, the entire operation is rolled back to maintain data integrity.

---

## 🚀 Features

| Feature | Description |
|---|---|
| Create Account | Opens a new SAVINGS or CURRENT account |
| View Account | Retrieves account details by ID |
| View All Accounts | Lists all customer accounts from the database |
| Search by Name | Partial match search using SQL LIKE |
| Update Account | Modifies customer name and email |
| Delete Account | Removes an account with confirmation |
| Deposit | Adds funds with transaction commit/rollback |
| Withdraw | Deducts funds with insufficient balance check |
| Transfer Funds | Moves money between accounts in a single transaction |

---

## 🧠 Concepts Demonstrated

### 1. JDBC (Java Database Connectivity)
- **DriverManager** — Establishes MySQL connection
- **PreparedStatement** — Parameterized queries to prevent SQL injection
- **ResultSet** — Reading query results and mapping to objects
- **Statement.RETURN_GENERATED_KEYS** — Retrieving auto-generated account IDs

### 2. CRUD Operations
- **Create** — `INSERT INTO accounts ...`
- **Read** — `SELECT * FROM accounts WHERE ...`
- **Update** — `UPDATE accounts SET ... WHERE ...`
- **Delete** — `DELETE FROM accounts WHERE ...`

### 3. MySQL Database Integration
- Database: `banking_db`
- Table: `accounts` with `account_id`, `customer_name`, `email`, `account_type`, `balance`, `created_at`
- SQL setup script included (`database_setup.sql`)

### 4. Transaction Management
- **setAutoCommit(false)** — Starts a manual transaction
- **conn.commit()** — Commits when all operations succeed
- **conn.rollback()** — Rolls back on any failure
- **Transfer** — Debit and credit happen in a single transaction; if either fails, both are rolled back
- **try-catch-finally** — Ensures connection cleanup

### 5. Custom Exceptions
- `AccountNotFoundException` — Account ID does not exist
- `InsufficientFundsException` — Withdrawal/transfer exceeds balance
- `InvalidAmountException` — Negative or zero amount provided

---

## 📁 Project Structure

```
Week2_Assignment2/
├── README.md
├── database_setup.sql                              # MySQL setup script
└── src/
    └── main/
        └── java/
            └── com/
                └── banking/
                    ├── BankingApp.java               # Main application
                    ├── model/
                    │   └── Account.java              # Account entity
                    ├── dao/
                    │   └── AccountDAO.java            # JDBC CRUD operations
                    ├── service/
                    │   └── BankingService.java        # Business logic + transactions
                    ├── exception/
                    │   ├── AccountNotFoundException.java
                    │   ├── InsufficientFundsException.java
                    │   └── InvalidAmountException.java
                    └── util/
                        └── DBConnection.java          # Database connection utility
```

---

## ⚙️ Prerequisites & Setup

### 1. Install MySQL
- Download and install MySQL Server
- Make sure MySQL is running on `localhost:3306`

### 2. Run the SQL Setup Script

```bash
mysql -u root -p < database_setup.sql
```

Or open MySQL Workbench and execute `database_setup.sql` manually.

### 3. Configure Database Credentials

Open `DBConnection.java` and update if needed:
```java
private static final String URL = "jdbc:mysql://localhost:3306/banking_db";
private static final String USERNAME = "root";
private static final String PASSWORD = "root";   // Change to your MySQL password
```

### 4. Download MySQL Connector JAR

Download `mysql-connector-j-8.x.x.jar` from:
https://dev.mysql.com/downloads/connector/j/

Place it in the project directory.

### 5. Compile and Run

```bash
# Compile
javac -cp mysql-connector-j-8.x.x.jar -d out \
    src/main/java/com/banking/exception/*.java \
    src/main/java/com/banking/model/*.java \
    src/main/java/com/banking/util/*.java \
    src/main/java/com/banking/dao/*.java \
    src/main/java/com/banking/service/*.java \
    src/main/java/com/banking/BankingApp.java

# Run
java -cp out:mysql-connector-j-8.x.x.jar com.banking.BankingApp
```

> **Windows users:** Replace `:` with `;` in the classpath.

---

## 🖥️ Sample Output

```
======================================
      JDBC BANKING SYSTEM
      Week 2 - Advanced Java
======================================

---------- MENU ----------
1. Create Account
2. View Account
3. View All Accounts
4. Search by Name
5. Update Account
6. Delete Account
7. Deposit
8. Withdraw
9. Transfer Funds
0. Exit

Enter choice: 7
Account ID: 1
Deposit Amount: 5000
Deposited 5000.00 to Account 1. New Balance: 55000.00

Enter choice: 9
From Account ID: 1
To Account ID: 2
Transfer Amount: 10000
Transferred 10000.00 from Account 1 to Account 2.
Account 1 Balance: 45000.00
Account 2 Balance: 85000.00
```

---

## 🛠️ Technologies Used

- **Language:** Java 8+
- **Database:** MySQL 8.0
- **JDBC:** mysql-connector-j
- **Concepts:** PreparedStatement, Transaction Management, DAO Pattern
- **Version Control:** Git & GitHub

---

## 👤 Author

**Gonuguntala Jaikar Ramu**

---
