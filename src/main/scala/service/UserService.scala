package service

import router.UserDto
import slick.dbio.DBIO
import utils.DatabaseConfig._
import utils.DatabaseConfig.profile.api._
import dao.Tables._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.github.t3hnar.bcrypt.{ Password, generateSalt }

trait UserService {

  def add(user: UserDto): Future[Option[UserRow]]

  def getAll(): Future[Seq[UserRow]]

  def get(id: Long): Future[Option[UserRow]]

  def getUser(email: String): Future[Option[(UserRow, PasswordRow)]]

  def delete(id: Long):Future[Int]

  def passwordMatches(passwordRow: PasswordRow, password: String): Boolean

  //def populateUser: UserDto => User = (userDto: UserDto) => User(0, userDto.email, userDto.name, userDto.surname)
}

object UserService extends UserService {

  def filterQuery(id: Long): Query[User, UserRow, Seq] =
    User.filter(_.id === id)

  override def delete(id: Long):Future[Int] = {
    import slick.driver.PostgresDriver.api._
    db.run(filterQuery(id).delete)
  }

  override def getAll(): Future[Seq[UserRow]] = db.run {
      getAllUser
  }

  override def passwordMatches(passwordRow: PasswordRow, password: String): Boolean = {
    passwordRow.salt match {
      case Some(salt) =>
        passwordRow.hashedPassword.contains(password.bcrypt(salt))
      case None =>
        false
    }
  }

  override def add(user: UserDto): Future[Option[UserRow]] = db.run {
    val salt = generateSalt
    for {
      pid <- doAddPassword(PasswordRow(0, Some(user.password.bcrypt(salt)), Some(salt)))
      userId <- doAddUser(UserRow(0, user.email, user.name, user.surname, pid, "now"))
      user <- getUser(userId)
    } yield user
  }

  override def get(id: Long): Future[Option[UserRow]] = db.run {
    for {
      user <- getUser(id)
    } yield (user)
  }

  override def getUser(email: String): Future[Option[(UserRow, PasswordRow)]] = db.run {
    (for {
      user <- dao.Tables.User.filter(_.email === email)
      password <- dao.Tables.Password.filter(_.id === user.id)
    } yield (user, password)).result.headOption
  }

  private def getAllUser(): DBIO[Seq[UserRow]] = User.result
  private def getUser(id: Long): DBIO[Option[UserRow]] = User.filter(_.id === id).result.headOption
  private def getUserRow(id: Long): DBIO[Option[UserRow]] = User.filter(_.id === id).result.headOption

  private def doAddPassword(p: PasswordRow): DBIO[Long] = (dao.Tables.Password returning dao.Tables.Password.map(_.id)) += p
  private def doAddUser(p: UserRow): DBIO[Long] = (dao.Tables.User returning dao.Tables.User.map(_.id)) += p

}
