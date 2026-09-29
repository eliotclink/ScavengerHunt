package domain

import java.util.UUID

enum GameType:
  case MURDER_MYSTERY, TREASURE_HUNT

enum GameInputType:
  case QR_SCAN, OBJECT_DETECT, TEXT

case class GameDefinition(
                           id: UUID = UUID.randomUUID(),
                           title: String,
                           definition: String,
                           gameType: GameType
                         )


case class Step(
                 clue: String,
                 solution: String,
                 hint: String,
                 hintIcon: String,
                 inputType: GameInputType,
                 location: Location
               )
