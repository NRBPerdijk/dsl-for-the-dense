object Main {
  def main(args: Array[String]) : Unit = {
    LeftShore.boatPresent = true
    LeftShore.creatures = Set(Wolf, Sheep, Cabbage)
    LeftShore.isSafe()
    RightShore.isSafe()

    Boat.transport(Some(Sheep), RightShore)
    Boat.transport(None, LeftShore)
    Boat.transport(Some(Cabbage), RightShore)
    Boat.transport(Some(Sheep), LeftShore)
    Boat.transport(Some(Wolf), RightShore)
    Boat.transport(None, LeftShore)
    Boat.transport(Some(Sheep), RightShore)
  }
}


trait BoatOccupant
trait BoatOperator {
  val canSail: Boolean
}
trait Creature

case object Wolf extends Creature
case object Sheep extends Creature
case object Cabbage extends Creature
object EmptySeat extends Creature

object Boat {
  var currentShore: Shore = LeftShore
  def transport(optionalCreature: Option[Creature], destination: Shore) = {
    currentShore.takeCreature(optionalCreature)
    currentShore.boatPresent = false
    currentShore.isSafe()
    currentShore = destination
    currentShore.boatPresent = true
    currentShore.placeCreature(optionalCreature)
    currentShore.isSafe()
    if (CompleteCondition.areWeDoneYet(currentShore)) CompleteCondition.printSolution()
  }
  var occupant: Option[Creature] = None
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

  def isSafe(): Unit = {
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
  def areWeDoneYet(shore: Shore): Boolean = shore match {
    case LeftShore => false
    case RightShore => shore.boatPresent &&
      shore.creatures.contains(Wolf) &&
      shore.creatures.contains(Sheep) &&
      shore.creatures.contains(Cabbage)
  }

  def printSolution(): Unit = {
    println("We're done here!")
  }
}

case object LeftShore extends Shore {
  override val shoreName: String = "LeftShore"
}

case object RightShore extends Shore {
  override val shoreName: String = "RightShore"
}