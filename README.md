A simple open banking API that provides next functions:

GET - gives the current balance of an account. Examples:
http://localhost:8080/api/accounts/{accountIban}/balance

GET - gives latest 10 transactions made by an account. Examples:
http://localhost:8080/api/accounts/{accountIban}/transactions

POST - Initiates a transaction. The request is IBAN-IBAN payment with amount and currency. Examples:
http://localhost:8080/api/account/{accountIban}/balance
