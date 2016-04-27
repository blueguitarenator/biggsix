package service

import com.github.t3hnar.bcrypt._
import dao.Tables
import router.UserDto
import slick.dbio.DBIO
import utils.DatabaseConfig._
import utils.DatabaseConfig.profile.api._
import dao.Tables._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}


trait UserService {

//  def userDao: UserDao
//
//  def passwordDao: PasswordDao

  //def currentUser: User

  def add(user: UserDto): Future[Option[User]]

  def getAll(): Future[Seq[User]]

  def get(id: Int): Future[Option[UserRow]]

  def getUser(email: String): Future[Option[(UserRow, PasswordRow)]]

  def delete(id: Int):Future[Int]

  def passwordMatches(passwordRow: PasswordRow, password: String): Boolean

  //def populateUser: UserDto => User = (userDto: UserDto) => User(0, userDto.email, userDto.name, userDto.surname)
}

object UserService extends UserService {

  override def passwordMatches(passwordRow: PasswordRow, password: String): Boolean = {
    passwordRow.salt match {
      case Some(salt) =>
        passwordRow.hashedPassword.contains(password.bcrypt(salt))
      case None =>
        false
    }
  }

  override def add(user: UserDto): Future[Option[User]] = db.run {
    val salt = generateSalt
    for {
      pid <- doAddPassword(PasswordRow(0, Some(user.password.bcrypt(salt)), Some(salt)))
      userId <- doAddUser(UserRow(0, user.email, user.name, user.surname, pid, "now"))
      User <- getUser(userId)
    } yield User
  }

  private def getUser(id: Long): DBIO[Option[UserRow]] = User.filter(_.id === id).result.headOption

  private def doAddPassword(p: PasswordRow): DBIO[Long] = (Password returning Password.map(_.id)) += p
  private def doAddUser(p: UserRow): DBIO[Long] = (User returning User.map(_.id)) += p

  override def getUser(email: String): DBIO[Option[(UserRow, PasswordRow)]] =
    (for {
      user <- User.filter(_.email === email)
      password <- Password.filter(_.id === user.id)
    } yield (user, password)).result.headOption

  //  override val userDao = UserDao
//
//  override val passwordDao = PasswordDao

  //override val currentUser = User

//  def initDb(): Unit = {
//    userDao.create
//    passwordDao.create
//
//    getAll() onSuccess { case users =>
//      if (users.size < 1) insertTestUser
//    }
//  }

//  private def insertTestUser  {
//    println("inserting test user")
//    val user = new UserDto("test1@test.com", Some("test"), Some("tester"), "123434")
//    add(user)
//  }

//  override def add(user: UserDto): Future[Option[User]] = db.run {
//    for {
//      passwordId <- passwordDao.add(UserPassword newWithPassword user.password)
//      userId <- userDao.add(populateUser(user).copy(passwordId = Some(passwordId)))
//      // "This DBMS allows only a single AutoInc"
//      // H2 doesn't allow return the whole user once created so we have to do this instead of returning the object from
//      // the dao on inserting
//      user <- UserDao.get(userId)
//    } yield user
//  }

//  override def getAll(): Future[Seq[User]] = db.run {
//    userDao.getAll
//  }
//
//  override def get(id: Int): Future[Option[User]] = db.run {
//    userDao.get(id)
//  }
//
//  override def get(email: String): Future[Option[(UserRow, PasswordRow)]] = db.run {
//    userDao.get(email)
//  }
//
//  override def delete(id: Int):Future[Int] = db.run {
//    userDao.delete(id)
//  }
}
