package service

import router.UserDto
import slick.dbio.DBIO
import utils.DatabaseConfig._
import utils.DatabaseConfig.profile.api._
import _root_.dao.Tables._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.github.t3hnar.bcrypt.{Password, generateSalt}
import org.joda.time.LocalDateTime

trait UserService {

  def add(user: UserDto): Future[Option[PersonRow]]

  def getAll(): Future[Seq[PersonRow]]

  def get(id: Long): Future[Option[PersonRow]]

  def getPerson(email: String): Future[Option[(PersonRow, PasswordRow)]]

  def delete(id: Long):Future[Int]

  def passwordMatches(passwordRow: PasswordRow, password: String): Boolean

  //def populatePerson: UserDto => Person = (userDto: UserDto) => Person(0, userDto.email, userDto.name, userDto.surname)
}

object UserService extends UserService with TimestampHelper {
//  private val builder = DateTimeFormat.forPattern("MM/dd/yyyy hh:mm:ss a")
  def filterQuery(id: Long): Query[Person, PersonRow, Seq] =
    Person.filter(_.id === id)

  override def delete(id: Long):Future[Int] = {
    import slick.driver.PostgresDriver.api._
    db.run(filterQuery(id).delete)
  }

  override def getAll(): Future[Seq[PersonRow]] = db.run {
      getAllPerson
  }

  override def passwordMatches(passwordRow: PasswordRow, password: String): Boolean = {
    passwordRow.salt match {
      case Some(salt) =>
        passwordRow.hashedPassword.contains(password.bcrypt(salt))
      case None =>
        false
    }
  }

  override def add(user: UserDto): Future[Option[PersonRow]] = db.run {
    val salt = generateSalt
    val localDateTime = new LocalDateTime();
    for {
      pid <- doAddPassword(PasswordRow(0, Some(user.password.bcrypt(salt)), Some(salt)))
      userId <- doAddPerson(PersonRow(0, user.email, user.name, user.surname, pid, getTimestamp()))
      user <- getPerson(userId)
    } yield user
  }

  override def get(id: Long): Future[Option[PersonRow]] = db.run {
    for {
      user <- getPerson(id)
    } yield (user)
  }

  override def getPerson(email: String): Future[Option[(PersonRow, PasswordRow)]] = db.run {
    (for {
      user <- dao.Tables.Person.filter(_.email === email)
      password <- dao.Tables.Password.filter(_.id === user.id)
    } yield (user, password)).result.headOption
  }

  private def getAllPerson(): DBIO[Seq[PersonRow]] = Person.result
  private def getPerson(id: Long): DBIO[Option[PersonRow]] = Person.filter(_.id === id).result.headOption
  private def getPersonRow(id: Long): DBIO[Option[PersonRow]] = Person.filter(_.id === id).result.headOption

  private def doAddPassword(p: PasswordRow): DBIO[Long] = (dao.Tables.Password returning dao.Tables.Password.map(_.id)) += p
  private def doAddPerson(p: PersonRow): DBIO[Long] = (dao.Tables.Person returning dao.Tables.Person.map(_.id)) += p

}
