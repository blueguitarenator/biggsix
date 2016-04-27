package router

import spray.json.DefaultJsonProtocol
import dao.Tables.UserRow

/**
  * Created by rich.johnson on 4/27/16.
  */
object UserJsonProtocol extends DefaultJsonProtocol {
  implicit val format = jsonFormat6(UserRow.apply)
}
