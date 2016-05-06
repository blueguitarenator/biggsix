package router

import spray.json.DefaultJsonProtocol

case class UserDto(
  email: String,

  name: String,

  surname: String,

  password: String )

object UserDto extends DefaultJsonProtocol{
  implicit val userDtoFormat = jsonFormat4(UserDto.apply)
}
