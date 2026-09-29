package services

import domain.GameDefinition
import javax.inject._

@Singleton
class GameService @Inject()() {

  def createGameDefinition(gameDefinition: GameDefinition): Unit = ()
}
