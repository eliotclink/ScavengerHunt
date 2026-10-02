package services

import domain.{Game, GameDefinition}
import repositories.GameRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

case class GameDefinitionNotFound(gameDefinitionId: String)
  extends Exception(s"GameDefinition not found: $gameDefinitionId")

@Singleton
class GameService @Inject()(gameRepository: GameRepository)(implicit ec: ExecutionContext):

  def createGameDefinition(gameDefinition: GameDefinition): Future[Unit] =
    gameRepository.create(gameDefinition)

  def getGameDefinition(gameDefinitionId: String): Future[Option[GameDefinition]] =
    gameRepository.get(gameDefinitionId)

  def createGame(game: Game): Future[Unit] =
    gameRepository.get(game.gameDefinitionId).flatMap {
      case Some(_) => gameRepository.create(game)
      case None    => Future.failed(GameDefinitionNotFound(game.gameDefinitionId))
    }