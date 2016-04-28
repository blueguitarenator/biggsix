package service

import dao.Tables._
import scala.concurrent.Future
import slick.dbio.DBIO
import utils.DatabaseConfig._
import utils.DatabaseConfig.profile.api._

trait AthleteService {
  def getAll(): Future[Seq[AppointmentRow]]
}

object AthleteService extends AthleteService with TimestampHelper {
  override def getAll(): Future[Seq[AppointmentRow]] = db.run {
    getAllAppointment
  }

  private def getAllAppointment(): DBIO[Seq[AppointmentRow]] = Appointment.result
}