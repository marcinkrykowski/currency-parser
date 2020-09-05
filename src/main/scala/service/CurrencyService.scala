package service

import repository.CurrencyRepository

case class CurrencyService(currencyRepository: CurrencyRepository) {

  def ratesWithinDates(
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

    val res =
      afterStart
        .intersect(beforeEnd)
        .map(rec => rec.rates(currency))
//        .map(rec => rec.rates.getOrElse(currency, "No such currency"))
    res
  }
  def averageRate(
      start: String,
      finish: String,
      currency: String
  ): BigDecimal = {
    val odp =
      ratesWithinDates(start, finish, currency).map(x => BigDecimal(x.toDouble))
    odp.sum / odp.length

//    val rates = ratesWithinDates(start, finish, currency)
//    val rate = rates.map(x => toDouble(x).getOrElse())
  }

  def toDouble(s: String): Option[Double] = {
    try {
      Some(s.toDouble)
    } catch {
      case e: NumberFormatException => None
    }
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
