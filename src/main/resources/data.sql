DROP TABLE IF EXISTS accounts;
CREATE TABLE accounts (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    iban VARCHAR(31) NOT NULL UNIQUE,
    balance NUMERIC(100, 2) DEFAULT 0.00

);

DROP INDEX IF EXISTS accounts_iban_index;
CREATE INDEX accounts_iban_index ON accounts(iban);

DROP TABLE IF EXISTS transactions;
CREATE TABLE transactions (
    reference VARCHAR(36) PRIMARY KEY,
    account_iban VARCHAR(31) NOT NULL,
    transaction_date DATE,
    amount NUMERIC(100, 2) NOT NULL,
    fee NUMERIC(100, 2) DEFAULT 0.00,
    description VARCHAR(128),

    FOREIGN KEY (account_iban) REFERENCES accounts(iban)
);

DROP INDEX IF EXISTS transactions_iban_index;
CREATE INDEX transactions_iban_index ON transactions(account_iban);