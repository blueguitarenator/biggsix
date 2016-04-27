package utils

import dao.Tables._
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterEach
import slick.jdbc.meta.MTable
import utils.DatabaseConfig._
import utils.DatabaseConfig.profile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object DatabaseSupportSpec {

  lazy val passwords = Seq(
    PasswordRow(1, Some("$2a$10$U3gBQ50FY5qiQ5XeQKgWwO6AADKjaGqh/6l3RzWitAWelWCQxffUC"), Option("$2a$10$U3gBQ50FY5qiQ5XeQKgWwO")),
    PasswordRow(2, Some("$2a$10$U3gBQ50FY5qiQ5XeQKgWwO6AADKjaGqh/6l3RzWitAWelWCQxffUC"), Option("$2a$10$U3gBQ50FY5qiQ5XeQKgWwO")),
    PasswordRow(3, Some("$2a$10$U3gBQ50FY5qiQ5XeQKgWwO6AADKjaGqh/6l3RzWitAWelWCQxffUC"), Option("$2a$10$U3gBQ50FY5qiQ5XeQKgWwO"))
  )
  lazy val users = Seq(
    UserRow(1, "test1@test.com", "name1", "surname1", 1, "now"),
    UserRow(2, "test2@test.com", "name2", "surname2", 2, "now"),
    UserRow(3, "test3@test.com", "name3", "surname3", 3, "now")
  )
}

trait SpecSupport extends Specification with BeforeAfterEach {

//  def createSchema = {
//    val dropAll = (PasswordDao.passwords.schema ++ UserDao.users.schema).drop
//
//    val createAll =
//      DBIO.seq(
//        (PasswordDao.passwords.schema ++ UserDao.users.schema).create,
//        PasswordDao.passwords ++= DatabaseSupportSpec.passwords,
//        UserDao.users ++= DatabaseSupportSpec.users
//      )
//
//    val results = db.run(MTable.getTables).flatMap {
//      tables => if (tables.toList.size > 1) {
//        db.run(dropAll).flatMap(_ => db.run(createAll))
//      } else db.run(createAll)
//    }
//
//    Await.result(results, Duration.Inf)
//  }

  override def before: Unit= {
    //createSchema
  }

  override def after: Unit= { }
}

