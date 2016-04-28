package router


import java.sql.Timestamp

import spray.json.{DefaultJsonProtocol, DeserializationException, JsNumber, JsValue, JsonFormat}
import _root_.dao.Tables.{AppointmentRow, PersonRow}


object UserJsonProtocol extends DefaultJsonProtocol {
  implicit object TimestampFormat extends JsonFormat[Timestamp] {
    def write(obj: Timestamp) = JsNumber(obj.getTime)

    def read(json: JsValue) = json match {
      case JsNumber(time) => new Timestamp(time.toLong)

      case _ => throw new DeserializationException("Date expected")
    }
  }
  implicit val format = jsonFormat6(PersonRow.apply)
}

object AppointmentJsonProtocol extends DefaultJsonProtocol {
  implicit object TimestampFormat extends JsonFormat[Timestamp] {
    def write(obj: Timestamp) = JsNumber(obj.getTime)

    def read(json: JsValue) = json match {
      case JsNumber(time) => new Timestamp(time.toLong)

      case _ => throw new DeserializationException("Date expected")
    }
  }
  implicit val format = jsonFormat5(AppointmentRow.apply)
}

