# Euro foreign exchange reference rates parser
## Purpose
Repository created to keep track of requested task implementation.

## Problem
For problem requirements check [description](Task_Description.pdf).

## Solution
Parser is a console application, but its logic can be extracted to controller logic if needed. Only pure Scala with ScalaTest is used to solve given problem.

## Algorithm
Flow is as follows:
1. Load CSV file to memory.
2. Parse file so each record represents daily view of Currency Exchange where for one date we can see all currency exchange rates.
3. Load records to memory (in-memory repository us used).
4. Make necessary calculations to serve requested operations.

## Operations: 
1. Allows an API caller to retrieve the reference rate data for a given Date for all available Currencies.
2. Given a Date, source Currency (eg. JPY), target Currency (eg. GBP), and an Amount, returns the Amount given converted from the first to the second Currency as it would have been on that Date (assuming zero fees).
3. Given a start Date, an end Date and a Currency, return the highest reference exchange rate that the Currency achieved for the period.
4. Given a start Date, an end Date and a Currency, determine and return the average reference exchange rate of that Currency for the period.