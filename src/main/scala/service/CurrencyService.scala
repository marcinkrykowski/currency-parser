package service

import repository.CurrencyRepository

case class CurrencyService(currencyRepository: CurrencyRepository) {

  def ratesWithinDates(
      start: String,
      finish: String,
      currency: String
  ): List[String] = {
    //TODO what if start date or finish date does not exist
    val afterStart =
      currencyRepository.records
        .sortBy(_.date)
        .dropWhile(rec => rec.date != start)

    val beforeEnd =
      currencyRepository.records
        .sortBy(_.date)
        .reverse
        .dropWhile(rec => rec.date != finish)

    //TODO what if currency does not exist
    val res =
      afterStart
        .intersect(beforeEnd)
        .map(rec => rec.rates(currency))
    res
  }
  def averageRate(start: String, finish: String, currency: String): Any = {
    val odp =
      ratesWithinDates(start, finish, currency).map(x => BigDecimal(x.toDouble))
    odp.sum / odp.length
  }

  def highestRate(
      start: String,
      finish: String,
      currency: String
  ): String = {
    val odp = ratesWithinDates(start, finish, currency).max
    odp
  }

  def makeExchange(
      date: String,
      sourceCurrency: String,
      targetCurrency: String,
      amount: BigDecimal
  ): BigDecimal = {
    val ratesForGivenDate = ratesForDate(date)
    val rateForSourceCurrency = BigDecimal(
      ratesForGivenDate(sourceCurrency).toDouble
    )
    val rateForTargetCurrency = BigDecimal(
      ratesForGivenDate(targetCurrency).toDouble
    )
    //TODO what if there will be no rate eg "N/A"
    val res: BigDecimal = amount * rateForSourceCurrency / rateForTargetCurrency
    res.setScale(2, BigDecimal.RoundingMode.HALF_UP)
  }

  def ratesForDate(date: String): Map[String, String] = {
    val res = currencyRepository.records
      .filter(record => record.date.equals(date))
      .map(record => record.rates)
    res match {
      case Nil => Map.empty
      case _   => res.head
    }
  }
}
