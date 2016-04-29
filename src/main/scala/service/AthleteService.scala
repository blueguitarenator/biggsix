package service

import dao.Tables._
import scala.concurrent.Future
import slick.dbio.DBIO
import utils.DatabaseConfig._
import utils.DatabaseConfig.profile.api._

trait AthleteService {
  def getAll(): Future[Seq[AppointmentRow]]
  def get(): Future[Option[AppointmentRow]]
}

object AthleteService extends AthleteService with TimestampHelper {
  override def getAll(): Future[Seq[AppointmentRow]] = db.run {
    getAllAppointment
  }

  override def get(): Future[Option[AppointmentRow]] = db.run {
    getAppointment(1)
  }

  private def getAllAppointment(): DBIO[Seq[AppointmentRow]] = Appointment.result
  private def getAppointment(id: Long): DBIO[Option[AppointmentRow]] = Appointment.filter(_.id === id).result.headOption
}