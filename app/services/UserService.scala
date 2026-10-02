package services

import domain.User
import javax.inject.{Inject, Singleton}
import repositories.UserRepository
import scala.concurrent.Future

@Singleton
class UserService @Inject()(userRepository: UserRepository):

  def createUser(user: User): Future[Unit] =
    userRepository.create(user)
