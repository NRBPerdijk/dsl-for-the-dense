object Main {
  def main(args: Array[String]) : Unit = {







  }
}


trait BoatOccupant
trait Creature

object Wolf extends BoatOccupant with Creature
object Sheep extends BoatOccupant with Creature
object Cabbage extends BoatOccupant with Creature
object EmptySeat extends BoatOccupant

object Boat {
  var occupants: BoatOccupant = EmptySeat
}

object LeftShore {
  var creatures: List[Creature] = List.empty
}

object RightShort {
  var creatures: List[Creature] = List.empty
}