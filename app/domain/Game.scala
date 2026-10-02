package domain

import java.util.UUID

case class Game(
  id: UUID = UUID.randomUUID(),
  gameDefinitionId: String
)
