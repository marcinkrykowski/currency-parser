package service

import model.CurrencyRecord
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import repository.CurrencyRepository

class CurrencyServiceSpec extends AnyFlatSpec with Matchers {
  val firstRecord: CurrencyRecord = CurrencyRecord(
    "2020-08-28",
    Map(
      "USD" -> "1.9999",
      "JPY" -> "125.39",
      "BGN" -> "1.9558",
      "CYP" -> "N/A"
    )
  )
  val secondRecord: CurrencyRecord = CurrencyRecord(
    "2020-08-27",
    Map(
      "USD" -> "1.1806",
      "JPY" -> "125.34",
      "BGN" -> "N/A",
      "CYP" -> "N/A"
    )
  )
  val thirdRecord: CurrencyRecord = CurrencyRecord(
    "2020-08-26",
    Map(
      "USD" -> "1.1806",
      "JPY" -> "125.34",
      "BGN" -> "1.9558",
      "CYP" -> "N/A"
    )
  )
  val allRecords = List(firstRecord, secondRecord, thirdRecord)
  val service: CurrencyService = CurrencyService(CurrencyRepository(allRecords))

  it should "calculate average rate for existing currency" in {
    service.averageRate("2020-08-26", "2020-08-27", "USD") shouldBe 1.1806
  }

  it should "return \"Data not available\" for not available rate" in {
    service.averageRate(
      "2020-08-26",
      "2020-08-27",
      "CYP"
    ) shouldBe "Data not available"
  }

  it should "calculate average rate for one missing rate" in {
    service.averageRate(
      "2020-08-26",
      "2020-08-28",
      "BGN"
    ) shouldBe 1.9558
  }

  it should "return \"Data not available\" for not existing currency" in {
    service.averageRate(
      "2020-08-25",
      "2020-08-27",
      "XXX"
    ) shouldBe "Data not available"
  }

  it should "return highest rate for existing currency" in {
    service
      .highestRate("2020-08-26", "2020-08-28", "USD")
      .toDouble shouldBe 1.9999
  }

  it should "return \"N/A\" for not available rates" in {
    service
      .highestRate("2020-08-26", "2020-08-28", "CYP") shouldBe "N/A"
  }

  it should "return correct highest rate for missing rate" in {
    service
      .highestRate("2020-08-26", "2020-08-28", "BGN")
      .toDouble shouldBe 1.9558
  }

  it should "return \"Currency does not exist\" for not existing currency" in {
    service
      .highestRate(
        "2020-08-26",
        "2020-08-28",
        "XXX"
      ) shouldBe "Currency does not exist"
  }

  it should "calculate exchange value correctly" in {
    service.makeExchange("2020-08-27", "JPY", "USD", 1.00) shouldEqual 106.17
  }

  it should "return \"Data not available\" for not existing date" in {
    service.makeExchange(
      "2021-08-27",
      "JPY",
      "USD",
      1.00
    ) shouldEqual "Data not available"
  }

  it should "return 0 for not existing currency" in {
    service.makeExchange(
      "2020-08-27",
      "XXX",
      "USD",
      1.00
    ) shouldEqual 0
  }

  it should "return 0 for negative amount" in {
    service.makeExchange(
      "2020-08-27",
      "JPY",
      "USD",
      -1234.00
    ) shouldEqual 0
  }

  it should "return rates for given dates" in {
    service.currencyRatesWithinDates(
      "2020-08-26",
      "2020-08-27",
      "USD"
    ) should contain theSameElementsAs List("1.1806", "1.1806")
  }

  it should "return empty list for not existing date" in {
    service.currencyRatesWithinDates(
      "2021-08-26",
      "2020-08-27",
      "USD"
    ) should have size 0
  }

  it should "return rates only for given date" in {
    service.ratesForDate("2020-08-26") should contain theSameElementsAs Map(
      "USD" -> "1.1806",
      "JPY" -> "125.34",
      "BGN" -> "1.9558",
      "CYP" -> "N/A"
    )
  }

  it should "return empty map if date does not exist" in {
    service.ratesForDate("2021-12-12") shouldBe Map.empty
  }

}
