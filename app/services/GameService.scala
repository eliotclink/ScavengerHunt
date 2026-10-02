package services

import domain.GameDefinition
import javax.inject.{Inject, Singleton}
import repositories.GameRepository
import scala.concurrent.Future

@Singleton
class GameService @Inject()(gameRepository: GameRepository):

  def createGameDefinition(gameDefinition: GameDefinition): Future[Unit] =
    gameRepository.create(gameDefinition)
