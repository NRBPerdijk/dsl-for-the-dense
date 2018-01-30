object ExternalApi {
  trait Creature

  case object Wolf extends Creature
  case object Sheep extends Creature
  case object Cabbage extends Creature

  object Boat {
    var currentShore: Shore = StartingShore
    var occupant: Option[Creature] = None

    def transport(optionalCreature: Option[Creature], destination: Shore): Shore = {
      if (destination != currentShore) {
        currentShore.takeCreature(optionalCreature)
        currentShore.boatPresent = false
        currentShore.assertIsSafe()
        currentShore = destination
        currentShore.boatPresent = true
        currentShore.placeCreature(optionalCreature)
        currentShore.assertIsSafe()
      } else {
        println(s"Warning: superfluous movement, boat is already on ${destination.shoreName}!")
      }
      currentShore
    }
  }

  trait Shore {
    var boatPresent: Boolean = false
    var creatures: Set[Creature] = Set.empty
    val shoreName: String

    def placeCreature(optionalCreature: Option[Creature]): Unit  = optionalCreature match {
      case Some(toBePlacedCreature) =>
        if (!creatures.contains(toBePlacedCreature)) creatures = creatures + toBePlacedCreature
        else throw new IllegalStateException(s"""Creature cannot be placed on shore $shoreName, because it is already there!""")
      case None =>
    }

    def takeCreature(optionalCreature: Option[Creature]): Unit = optionalCreature match {
      case Some(toBeRemovedCreature) =>
        if (creatures.contains(toBeRemovedCreature)) creatures = creatures.filterNot(c => c == toBeRemovedCreature)
        else throw new IllegalStateException(s"""Creature cannot be taken from shore $shoreName, because it is not there!""")
      case None =>
    }

    def assertIsSafe(): Unit = {
      if (!boatPresent) {
        if (creatures.contains(Sheep) && creatures.contains(Wolf)) {
          throw new Exception(s"Poor logic has resulted in the unfortunate demise of one of your creatures... Sheep has been eaten on shore $shoreName, mission failed!")
        }
        if (creatures.contains(Cabbage) && creatures.contains(Sheep)) {
          throw new Exception(s"Poor logic has resulted in the unfortunate demise of one of your creatures... Cabbage has been eaten on shore $shoreName, mission failed!")
        }
      }
    }

  }

  case object CompleteCondition {
    def goalAchieved(shore: Shore): Boolean = shore match {
      case StartingShore => false
      case DestinationShore => shore.boatPresent &&
        shore.creatures.contains(Wolf) &&
        shore.creatures.contains(Sheep) &&
        shore.creatures.contains(Cabbage)
    }

    def printSuccessMessage(): Unit = {
      println("Success!")
    }
  }

  case object StartingShore extends Shore {
    override val shoreName: String = "starting shore"
  }

  case object DestinationShore extends Shore {
    override val shoreName: String = "destination shore"
  }
}
