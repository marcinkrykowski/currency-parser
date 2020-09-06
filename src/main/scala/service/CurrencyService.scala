package service

import repository.CurrencyRepository

case class CurrencyService(currencyRepository: CurrencyRepository) {

  def averageRate(
      start: String,
      finish: String,
      currency: String
  ): Any = {
    val rates: List[BigDecimal] =
      currencyRatesWithinDates(start, finish, currency)
        .flatMap(rate => toDouble(rate))
        .map(rateValue => BigDecimal(rateValue))

    // below line prevents wrong calculation
    // when for example if some currency misses data from one day
    // we do not want to calculate missing values as 0
    val length = rates.count(x => x != BigDecimal(0.00))

    rates match {
      case Nil => "Data not available"
      case _ =>
        (rates.sum / length).setScale(4, BigDecimal.RoundingMode.HALF_UP)
    }
  }

  def highestRate(
      start: String,
      finish: String,
      currency: String
  ): String = {
    currencyRatesWithinDates(start, finish, currency).max
  }

  def makeExchange(
      date: String,
      sourceCurrency: String,
      targetCurrency: String,
      amount: BigDecimal
  ): Any = {
    val ratesForGivenDate = ratesForDate(date)
    val rateForSourceCurrency = BigDecimal(
      ratesForGivenDate.getOrElse(sourceCurrency, "0").toDouble
    )
    val rateForTargetCurrency = BigDecimal(
      ratesForGivenDate.getOrElse(targetCurrency, "0").toDouble
    )

    try {
      val result: BigDecimal =
        (amount * rateForSourceCurrency) / rateForTargetCurrency
      List(
        result.setScale(2, BigDecimal.RoundingMode.HALF_UP),
        BigDecimal(0.00)
      ).max //to prevent negative amount calculation
    } catch {
      case _: Exception => "Data not available"
    }

  }

  def toDouble(s: String): Option[Double] = {
    try {
      Some(s.toDouble)
    } catch {
      case _: NumberFormatException => None
    }
  }

  def currencyRatesWithinDates(
      start: String,
      finish: String,
      currency: String
  ): List[String] = {
    val afterStart =
      currencyRepository.records
        .sortBy(_.date)
        .dropWhile(rec => rec.date != start)

    val beforeEnd =
      currencyRepository.records
        .sortBy(_.date)
        .reverse
        .dropWhile(rec => rec.date != finish)

    afterStart
      .intersect(beforeEnd)
      .map(rec => rec.rates.getOrElse(currency, "Currency does not exist"))
  }

  def ratesForDate(date: String): Map[String, String] = {
    val rates = currencyRepository.records
      .filter(record => record.date.equals(date))
      .map(record => record.rates)
    rates match {
      case Nil => Map.empty
      case _   => rates.head
    }
  }
}
