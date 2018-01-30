

import MyDsl._

package object MyDsl extends Implicits {

}

trait Implicits {

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
