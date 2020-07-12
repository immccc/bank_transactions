# Bank Transaction Handling Code Challenge

## How to use
1. `mvn package`
2. `java -jar bank-0.0.1-SNAPSHOT.jar`

## Account

Given that there is a requirement for transactions to not be created when they make
account amount to be negative, account entity plus API for handling it has been created.
- `GET /accounts/[IBAN]` to find accounts by IBAN or returning empty if provided one does not exist.
- `POST /accounts` to create account from payload
~~~
{
    "iban": "ESXXXXXXX...",
    "balance": 2.0
}
~~~

- As not a strict part of the requirements, really basic API has been provided.
- I consider this necessary to properly test transaction creation, so that it is not allowed when account
balance gets negative.  
- There are no validations on IBAN format.
- There are no validations on correct initial balance, either.

## Transactions

### Search by IBAN
~~~
    GET /accounts/iban/[IBAN](?sort=ASCENDING|DESCENDING)
~~~

- IBAN path parameter is mandatory.
- sorting is optional. By default, it is ascending.

### Create transaction

~~~
    POST /accounts/

   {
        "reference": "945abb0d-c094-4c36-b663-ebec8694ca05",
        "accountIban": "ES33XXXX",
        "date": "2020-07-12T00:00:00+02:00",
        "amount": 2.00,
        "fee": 0.00
        "description": "some optional description"
   }

~~~

- When no reference is provided, UUID reference is created.
- For simplicity and YANGNI, no validations on reference format.
- Balance in account is updated with amount provided in transaction.
- If successful, returns `HTTP 201`.
- It can return `HTTP 412` if:
    - There is already an existing transaction with same ID. 
    - There is no enough balance in account for a debit transaction (negative amount)
- Note that there is a mutation inside this operation (balance). Probably a more elegant
way to tackle this is with a reactive repository and doing some backpressure.

## Transaction status
~~~
GET /transactions/status
{
    "reference": "reference",
    "channel": "INTERNAL"
}
~~~
- When channel is not provided, it is by default `CLIENT`.
- When there is no stored transaction with provided reference, returned Http is `HTTP 404`,
plus specified body in acceptance criteria with channel `INVALID`. 

## Further improvements
- More elegant error responses for transaction and account specific endpoints.
- Logging for proper tracing.
- Reactive endpoints and repository for non blocking REST calls..
