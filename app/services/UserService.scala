package services

import domain.User
import javax.inject._
import scala.concurrent.Future

@Singleton
class UserService @Inject()():

  def createUser(user: User): Future[Unit] = Future.successful(())
