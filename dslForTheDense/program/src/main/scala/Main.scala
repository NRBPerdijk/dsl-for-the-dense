import ExternalApi._
import ExternalApi.CompleteCondition._
import MyDsl.{Boat, Cabbage, Sheep, Wolf, _}

object Main {
  def main(args: Array[String]) : Unit = {
    Setup(
     Boat on StartingShore,
     Wolf on StartingShore,
     Sheep on StartingShore,
     Cabbage on StartingShore
    ) execute()
//    StartingShore creatures = Set(Wolf, Sheep, Cabbage)


    Boat move Cabbage to StartingShore
    Boat move Sheep to DestinationShore
    Boat move None to StartingShore
    Boat move Cabbage to DestinationShore
    Boat move Sheep to StartingShore
    Boat move Wolf to DestinationShore
    Boat move None to StartingShore

    if (goalAchieved(Boat transport(Sheep, DestinationShore))) {
      printSuccessMessage()
    } else {
      print("oh no, we've failed!")
    }

  }
}
