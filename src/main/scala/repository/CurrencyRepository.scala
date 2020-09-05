package repository

import model.CurrencyRecord

case class CurrencyRepository(records: List[CurrencyRecord] = List.empty) {
  def getAllDates: List[String] = {
    records.map(record => record.date)
  }
}
