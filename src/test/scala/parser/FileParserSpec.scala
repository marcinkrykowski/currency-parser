package parser

import model.CurrencyRecord
import org.scalatest.LoneElement
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class FileParserSpec extends AnyFlatSpec with Matchers with LoneElement {
  val sourceFile: String = "./src/test/resources/example_data.csv"
  val names = List("USD", "JPY", "BGN", "CYP")

  val singleRecord = CurrencyRecord(
    "2020-08-28",
    Map(
      "USD" -> "1.1915",
      "JPY" -> "125.39",
      "BGN" -> "1.9558",
      "CYP" -> "N/A"
    )
  )

  it should "parse all lines in file to list of strings" in {
    val lines = FileParser.parseFile(sourceFile)
    lines should have size 3
  }

  it should "extract only currency names from first row" in {
    val firstRow = "Date,USD,JPY,BGN,CYP,"
    val extractedNames = FileParser.parseFirstRow(firstRow)
    extractedNames should contain theSameElementsAs names
  }

  it should "create correct currency record" in {
    val row = "2020-08-28,1.1915,125.39,1.9558,N/A,"
    val currencyRecords = FileParser.parseCurrency(List(row), names)
    currencyRecords should have size 1
    currencyRecords.loneElement shouldEqual singleRecord
  }

  it should "create Currency Records correctly based on a given file" in {
    val anotherRecord = CurrencyRecord(
      "2020-08-27",
      Map(
        "USD" -> "1.1806",
        "JPY" -> "125.34",
        "BGN" -> "1.9558",
        "CYP" -> "N/A"
      )
    )
    val records = List(singleRecord, anotherRecord)
    val recordsFromFile = FileParser.parseCurrencyRecords(sourceFile)
    recordsFromFile should have size 2
    recordsFromFile should contain theSameElementsAs records
  }
}
