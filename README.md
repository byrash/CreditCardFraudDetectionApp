# CreditCardFraudDetectionApp
Consider the following credit card fraud detection algorithm:
A credit card transaction is comprised of the following elements;
hashed credit card number
timestamp - of format 'year-month-dayThour:minute:second'
price - of format 'dollars.cents'
Transactions are to be received as a comma separated string of elements eg. '10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-29T13:15:54, 10.00'
A credit card will be identified as fraudulent if the sum of prices for a unique hashed credit card number, for a given day, exceeds the price threshold T.
Write a method on a class, which, when given a list transactions, a date and a price threshold T, returns a list of hashed credit card numbers that have been identified as fraudulent for that day.
