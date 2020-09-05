import parser.FileParser
import repository.CurrencyRepository
import service.CurrencyService

object Main extends App {
  val sourceFile: String = "./src/main/resources/eurofxref-hist.csv"
  val records = FileParser.parseCurrencyRecords(sourceFile)
  val service = CurrencyService(CurrencyRepository(records))

  //example service calls
  println(service.ratesForDate("2018-01-02"))

  println(service.makeExchange("2018-01-02", "JPY", "GBP", 50.0))

  println(service.highestRate("2018-01-02", "2018-01-03", "USD"))

  println(service.averageRate("2018-01-02", "2018-01-03", "USD"))
}
