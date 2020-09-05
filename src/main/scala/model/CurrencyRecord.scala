package model

case class CurrencyRecord(
    date: String,
    rates: Map[String, String]
)
