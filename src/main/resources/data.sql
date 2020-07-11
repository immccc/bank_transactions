CREATE TABLE accounts (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    iban VARCHAR(31) NOT NULL UNIQUE,
    balance NUMERIC(100, 2) DEFAULT 0.00

);

CREATE INDEX accounts_iban_index ON accounts(iban)