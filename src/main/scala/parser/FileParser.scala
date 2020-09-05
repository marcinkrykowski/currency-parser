package parser

import model.CurrencyRecord

import scala.io.Source

object FileParser {

  // TODO extract currency names based on file
  val currenciesNames = List(
    "USD",
    "JPY",
    "BGN",
    "CYP",
    "CZK",
    "DKK",
    "EEK",
    "GBP",
    "HUF",
    "LTL",
    "LVL",
    "MTL",
    "PLN",
    "ROL",
    "RON",
    "SEK",
    "SIT",
    "SKK",
    "CHF",
    "ISK",
    "NOK",
    "HRK",
    "RUB",
    "TRL",
    "TRY",
    "AUD",
    "BRL",
    "CAD",
    "CNY",
    "HKD",
    "IDR",
    "ILS",
    "INR",
    "KRW",
    "MXN",
    "MYR",
    "NZD",
    "PHP",
    "SGD",
    "THB",
    "ZAR"
  )

  def parseCurrencyRecords(fileName: String): List[CurrencyRecord] = {
    parseCurrency(parseFile(fileName))
  }

  def parseFile(fileName: String): List[String] = {
    val source = Source.fromFile(fileName)
    val lines = source.getLines.filter(_.length > 0).toList
    source.close()
    lines.tail
  }

  def parseCurrency(currencyString: List[String]): List[CurrencyRecord] = {
    currencyString.map { record =>
      val params = record.split(",")
      val date = params.head
      val rates = params.tail.toList
      CurrencyRecord(date, currenciesNames.zip(rates).toMap)
    }
  }
}
