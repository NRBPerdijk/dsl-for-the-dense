
import ExternalApi.{DestinationShore, Shore, StartingShore, Boat => ExternalApiBoat, Cabbage => ExternalApiCabbage, Creature => ExternalApiCreature, Sheep => ExternalApiSheep, Wolf => ExternalApiWolf}
import MyDsl._

package object MyDsl extends Implicits {
trait Placeable

  case class Setup(boatOnShore: Boat On Shore,
                   wolfOnShore: Wolf On Shore,
                   sheepOnShore: Sheep On Shore,
                   cabbageOnShore: Cabbage On Shore){
    def execute() = {
      boatOnShore()
      wolfOnShore()
      sheepOnShore()
      cabbageOnShore()
      StartingShore assertIsSafe()
      DestinationShore assertIsSafe()
    }
  }

  trait Creature extends Placeable {
    def apply(): ExternalApiCreature
  }

  trait Wolf extends Creature
  object Wolf extends Wolf {
    override def apply(): ExternalApiCreature = ExternalApiWolf
  }

  trait Sheep extends Creature
  object Sheep extends Sheep {
    override def apply(): ExternalApiCreature = ExternalApiSheep
  }

  trait Cabbage extends Creature
  object Cabbage extends Cabbage {
    override def apply(): ExternalApiCreature = ExternalApiCabbage
  }

  case class MoveOrder(creature: Creature, shore: Shore)
  case class ToWord(creature: Either[Creature, Option[Nothing]]) {
    def to(shore: Shore) = creature match {
      case Left(creature) => Boat.transport(creature, shore)
      case Right(nothing) => Boat.transport(nothing, shore)
    }
  }

  trait Boat extends Placeable
  object Boat extends Boat {
    def move(creature: Creature) = ToWord(Left(creature))
    def move(nothing: Option[Nothing]) = ToWord(Right(nothing))
    def transport(creature: Creature, shore: Shore) = ExternalApiBoat.transport(creature, shore)
    def transport(nothing: Option[Nothing], shore: Shore) = ExternalApiBoat.transport(nothing, shore)
  }
  case class On[P <: Placeable, S <: Shore](placeable: P, shore: S) {
    def apply() = placeable match {
      case b: Boat => shore.boatPresent =true
      case c: Creature => shore.placeCreature(c)
    }
  }
}

trait Implicits {

  implicit class BoatToShore(boat: Boat) {
    def on(shore: Shore): Boat On Shore = On(boat, shore)
  }


  implicit class WolfOnShore(wolf: Wolf) {
    def on(shore: Shore): Wolf On Shore = On(wolf, shore)
  }

  implicit class SheepOnShore(sheep: Sheep) {
    def on(shore: Shore): Sheep On Shore = On(sheep, shore)
  }

  implicit class CabbageOnShore(cabbage: Cabbage) {
    def on(shore: Shore): Cabbage On Shore = On(cabbage, shore)
  }

  implicit def someToCreature(creature: Creature): Option[ExternalApiCreature] = Some(creature())
}



























//import ExternalApi.{StartingShore => ExternalApiStartingShore}
//import ExternalApi.{DestinationShore => ExternalApiDestinationShore}
//import ExternalApi.{Shore => ExternalApiShore}


