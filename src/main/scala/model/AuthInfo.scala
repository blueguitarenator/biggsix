package model

import dao.Tables._

case class AuthInfo(user: UserRow) {
  //Here you should put the logic for permissions associated to users

  def hasPermissions(permission: String): Boolean = true
}
