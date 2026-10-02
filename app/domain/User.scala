package domain

import java.util.UUID

case class User(
                 id: UUID = UUID.randomUUID(),
                 name: String,
                 icon: String
               )