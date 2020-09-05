package parser

import model.CurrencyRecord

import scala.io.Source

object FileParser {

  def parseCurrencyRecords(fileName: String): List[CurrencyRecord] = {
    val records = parseFile(fileName)
    val names = parseFirstRow(records.head)
    parseCurrency(records.tail, names)
  }

  def parseFile(fileName: String): List[String] = {
    val source = Source.fromFile(fileName)
    val lines = source.getLines.filter(_.length > 0).toList
    source.close()
    lines
  }

  def parseFirstRow(names: String): List[String] = names.split(",").toList.tail

  def parseCurrency(
      currencyString: List[String],
      currenciesNames: List[String]
  ): List[CurrencyRecord] = {
    currencyString.map { record =>
      val params = record.split(",")
      val date = params.head
      val rates = params.tail.toList
      CurrencyRecord(date, currenciesNames.zip(rates).toMap)
    }
  }
}
