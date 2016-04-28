package router

import spray.json.DefaultJsonProtocol
import _root_.dao.Tables.{AppointmentRow, PersonRow}


object UserJsonProtocol extends DefaultJsonProtocol {
  implicit val format = jsonFormat6(PersonRow.apply)
}

//object AppointmentJsonProtocol extends DefaultJsonProtocol {
//  implicit val format = jsonFormat5(AppointmentRow.apply)
//}

