package repository

import model.CurrencyRecord
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CurrencyRepositorySpec extends AnyFlatSpec with Matchers {
  it should "create Currency Repository with empty list by default" in {
    val currencyRepository = CurrencyRepository()
    currencyRepository.records shouldBe List.empty
  }

  it should "create Currency Repository with records" in {
    val rates = Map("GBP" -> "1.1233", "PLN" -> "4.2312")
    val record = CurrencyRecord("2018-01-01", rates)
    val records = List(record)
    val currencyRepository = CurrencyRepository(records)
    currencyRepository.records should have size 1
    currencyRepository.records should contain(record)
  }

  it should "return all dates existing in file" in {
    val rates = Map("GBP" -> "1.1233", "PLN" -> "4.2312")
    val record1 = CurrencyRecord("2018-01-01", rates)
    val record2 = CurrencyRecord("2020-10-10", rates)
    val records = List(record1, record2)
    val currencyRepository = CurrencyRepository(records)
    currencyRepository.getAllDates should have size 2
    currencyRepository.getAllDates should contain("2018-01-01")
    currencyRepository.getAllDates should contain("2020-10-10")
  }
}
