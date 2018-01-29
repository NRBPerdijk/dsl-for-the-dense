import ExternalApi.{Boat => ExternalApiBoat, Cabbage => ExternalApiCabbage, Creature => ExternalApiCreature, Sheep => ExternalApiSheep, Wolf => ExternalApiWolf}
import ExternalApi.{StartingShore => ExternalApiStartingShore}
import ExternalApi.{DestinationShore => ExternalApiDestinationShore}
import ExternalApi.{Shore => ExternalApiShore}
import scala.languageFeature.implicitConversions

import MyDsl._

package object MyDsl extends Implicits {

  trait Shore
  object StartingShore extends Shore
  object DestinationShore extends Shore

  trait Placeable
  case class On[P <: Placeable, S <: Shore](p: P, s: Shore) {
    def apply() = p match {
      case b: Boat => s.boatPresent = true
      case c: Creature => s.placeCreature(c)
    }
  }

  case class ToWord(creature: Either[Creature, Option[Nothing]]) {
    def to(shore: Shore) = creature match {
      case Left(c) => ExternalApiBoat.transport(c, shore)
      case Right(n) => ExternalApiBoat.transport(None, shore)
    }

  }

  case class Setup(boatOnShore: Boat On Shore,
                   wolfOnShore: Wolf On Shore,
                   sheepOnShore: Sheep On Shore,
                   cabbageOnShore: Cabbage On Shore) {

    def execute() = {
      boatOnShore()
      wolfOnShore()
      sheepOnShore()
      cabbageOnShore()
      DestinationShore assertIsSafe()
      StartingShore assertIsSafe()
    }
  }

  trait Boat extends Placeable
  object Boat extends Boat {
    def move(creature: Creature) = ToWord(Left(creature))
    def move(nothing: Option[Nothing]) = ToWord(Right(nothing))
//    def transport(creature: Creature, shore: Shore) = ExternalApiBoat.transport(creature(), shore)
//    def transport(nothing: Option[Nothing], shore: Shore) = ExternalApiBoat.transport(nothing, shore)
  }

  trait Creature extends Placeable {
    def apply(): ExternalApiCreature
  }

  trait Wolf extends Creature
  object Wolf extends Wolf {
    def apply(): ExternalApiCreature = ExternalApiWolf
  }

  trait Sheep extends Creature
  object Sheep extends Sheep {
    override def apply(): ExternalApiCreature = ExternalApiSheep
  }

  trait Cabbage extends Creature
  object Cabbage extends Cabbage {
    override def apply(): ExternalApiCreature = ExternalApiCabbage
  }

}

trait Implicits {

  implicit class BoatOnShore(boat: Boat) {
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

  implicit def creatureToOption(creature: Creature): Option[ExternalApiCreature] = Some(creature())
  implicit def shoreToExternal(shore: Shore): ExternalApiShore = shore match {
    case DestinationShore => ExternalApiDestinationShore
    case StartingShore => ExternalApiStartingShore
  }
}



























//import ExternalApi.{Boat => ExternalApiBoat, Cabbage => ExternalApiCabbage, Creature => ExternalApiCreature, Sheep => ExternalApiSheep, Wolf => ExternalApiWolf}
//import ExternalApi.{StartingShore => ExternalApiStartingShore}
//import ExternalApi.{DestinationShore => ExternalApiDestinationShore}
//import ExternalApi.{Shore => ExternalApiShore}

//
//implicit class WolfOnShore(wolf: Wolf) {
//  def on(shore: Shore): Wolf On Shore = On(wolf, shore)
//}
//
//implicit class SheepOnShore(sheep: Sheep) {
//  def on(shore: Shore): Sheep On Shore = On(sheep, shore)
//}
//
//implicit class CabbageOnShore(cabbage: Cabbage) {
//  def on(shore: Shore): Cabbage On Shore = On(cabbage, shore)
//}
