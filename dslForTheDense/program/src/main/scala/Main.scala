/**
  * Author: Nathan R.B. Perdijk
  *
  * We still have imports that reference ExternalApi.
  * Preferably we want to lock the DSL user out of accessing these objects altogether, because they are so easily
  * mutated and cheated! We don't want them getting around our DSL, because our DSL is supposed to make their job easier...
  *
  * We should find a way to get rid of these imports!
  */
import ExternalApi.StartingShore
import ExternalApi.DestinationShore

/**
  * Scala: The underscore (_) is Scala's way of performing a package import.
  * (the * used by Java can actually be used in  e.g. method names and is therefore not a suitable wildcard)
  */

import MyDsl.{Boat, Cabbage, Sheep, Wolf, _}

object Main {
  def main(args: Array[String]) : Unit = {
    /**
      * Scala: you don't need ';' to terminate statements. The compiler infers them for you!
      * You also don't need . or () (when defs have only a single parameter) when doing function calls.
      * If the construct becomes too complex though, the compiler may need help parsing instructions in the right order.
      * When writing a DSL, great care must be taken that the compiler won't have reasons for ambiguous interpretation!
      */
    Setup(
      /**
        * Notice the underlining that your editor (or at least IntelliJ IDEA) provides to indicate that something implicit
        * is going on!
        */
     Boat on StartingShore,
     Wolf on StartingShore,
     Sheep on StartingShore,
     Cabbage on StartingShore
    ) execute (
    /**
      * This is no longer imperative!
      * By returning a MoveOrder that holds the required information for the transportation, as well as a function apply()
      * that actually performs it, we can offload the execution of MoveOrders into the execute method that comes with the Setup.
      * Here, we can do something clever, like checking for every move whether the goal has been achieved, without boring
      * the user with repetitive manual checks!
      * The vararg allows the user to give as little or as many move orders as they wish, separated by comma's.
      */
      Boat move Cabbage to StartingShore,
      Boat move Sheep to DestinationShore,
      Boat move None to StartingShore,
      Boat move Cabbage to DestinationShore,
      Boat move Sheep to StartingShore,
      Boat move Wolf to DestinationShore,
      Boat move None to StartingShore,
      Boat move Sheep to DestinationShore
      )
  }
}
