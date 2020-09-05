package model

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CurrencyRecordSpec extends AnyFlatSpec with Matchers {
  it should "create Currency Record with empty parameters" in {
    val currencyRecord = CurrencyRecord("", Map.empty)
    currencyRecord.date should equal("")
    currencyRecord.rates shouldBe empty
  }

  it should "create Currency Record with all parameters" in {
    val rates = Map("GBP" -> "1.1233", "PLN" -> "4.2312")
    val currencyRecord = CurrencyRecord("2018-01-01", rates)
    currencyRecord.date should equal("2018-01-01")
    currencyRecord.rates shouldBe rates
  }
}
