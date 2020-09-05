import parser.FileParser
import repository.CurrencyRepository
import service.CurrencyService

//in some case logic from Main class might be moved to controller, depending if needed
object Main extends App {
  //in that case data is taken directly from local file, but if needed might also be fetched from URL
  val sourceFile: String = "./src/main/resources/eurofxref-hist.csv"
  val records = FileParser.parseCurrencyRecords(sourceFile)
  val service = CurrencyService(CurrencyRepository(records))

  //example service calls

  //fetch reference rate data for a given Date for all available Currencies
  val date =
    "2018-01-02" // I assume that given date exists in CSV file, same for start and end dates below
  val rates = service.ratesForDate(date)
  println(s"Rates for $date are: $rates")

  //calculate the Amount given converted from the first to the second Currency as it would have been on that Date (assuming zero fees)
  val amount = 50.0
  val sourceCurrency = "JPY"
  val targetCurrency = "GBP"
  val convertedAmount =
    service.makeExchange(date, sourceCurrency, targetCurrency, amount)
  println(s"Converted amount is: $convertedAmount")

  //get the highest reference exchange rate that the Currency achieved for the period
  val startDate = "2018-01-02"
  val endDate = "2018-01-03"
  val currency = "USD"
  val highestExchangeRate = service.highestRate(startDate, endDate, currency)
  println(s"Highest exchange rate for given period is: $highestExchangeRate")

  //calculate the average reference exchange rate of that Currency for the period
  val averageRate = service.averageRate(startDate, endDate, currency)
  println(s"Average exchange rate for given period is: $averageRate")
}
