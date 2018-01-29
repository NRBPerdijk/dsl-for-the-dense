import ExternalApi._
import ExternalApi.CompleteCondition._

object Main {
  def main(args: Array[String]) : Unit = {
    StartingShore.boatPresent = true
    StartingShore.creatures = Set(Wolf, Sheep, Cabbage)
    StartingShore.assertIsSafe()
    DestinationShore.assertIsSafe()

    Boat.transport(Some(Cabbage), StartingShore)
    Boat.transport(Some(Sheep), DestinationShore)
    Boat.transport(None, StartingShore)
    Boat.transport(Some(Cabbage), DestinationShore)
    Boat.transport(Some(Sheep), StartingShore)
    Boat.transport(Some(Wolf), DestinationShore)
    Boat.transport(None, StartingShore)

    if (goalAchieved(Boat.transport(Some(Sheep), DestinationShore))) {
      printSuccessMessage()
    } else {
      print("oh no, we've failed!")
    }

  }
}

