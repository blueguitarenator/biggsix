package service

import java.sql.Timestamp

import org.joda.time.{DateTimeZone, LocalDateTime}

trait TimestampHelper {


  def getTimestamp(): java.sql.Timestamp = {
    val localDateTime = new LocalDateTime()
    val utc = localDateTime.toDateTime(DateTimeZone.UTC)
    val unixTimeStamp = utc.getMillis
    new Timestamp(unixTimeStamp)
  }
}
