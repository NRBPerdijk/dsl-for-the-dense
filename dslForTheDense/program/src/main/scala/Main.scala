import ExternalApi.CompleteCondition._
import MyDsl._

object Main {
  def main(args: Array[String]) : Unit = {
    Setup(
      Boat on StartingShore,
      Wolf on StartingShore,
      Sheep on StartingShore,
      Cabbage on StartingShore
    ).execute()

    Boat move Cabbage to StartingShore
    Boat move Sheep to DestinationShore
    Boat move None to StartingShore
    Boat move Cabbage to DestinationShore
    Boat move Sheep to StartingShore
    Boat move Wolf to DestinationShore
    Boat move None to StartingShore

    if (goalAchieved(Boat move Sheep to DestinationShore)) {
      printSuccessMessage()
    } else {
      print("oh no, we've failed!")
    }

  }
}

