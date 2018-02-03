/**
  * Author: Nathan R.B. Perdijk
  *
  * We still have imports that reference ExternalApi.
  * Preferably we want to lock the DSL user out of accessing these objects altogether, because they are so easily
  * mutated and cheated! We don't want them getting around our DSL, because our DSL is supposed to make their job easier...
  *
  * We should find a way to get rid of these imports!
  */
import ExternalApi._
import ExternalApi.CompleteCondition._

/**
  * Scala: The underscore (_) is Scala's way of performing a package import.
  * (the * used by Java can actually be used in  e.g. method names and is therefore not a suitable wildcard)
  */

import MyDsl.{Boat, Cabbage, Sheep, Wolf, _}

object Main {
  def main(args: Array[String]) : Unit = {
    Setup(
      /**
        * Notice the underlining that your editor (or at least IntelliJ IDEA) provides to indicate that something implicit
        * is going on!
        */
     Boat on StartingShore,
     Wolf on StartingShore,
     Sheep on StartingShore,
     Cabbage on StartingShore
    ) execute()
    /**
      * Scala: you don't need ';' to terminate statements. The compiler infers them for you!
      * You also don't need . or () (when defs have only a single parameter) when doing function calls.
      * If the construct becomes too complex though, the compiler may need help parsing instructions in the right order.
      * When writing a DSL, great care must be taken that the compiler won't have reasons for ambiguous interpretation!
      */

//    StartingShore creatures = Set(Wolf, Sheep, Cabbage)

    /**
      * This is still imperative!
      * Not really a set of instructions, but instead their immediate execution, which prevents us from doing something
      * smart like checking whether the goal has been achieved after every move... Unless you wrap every execution in
      * goalAchieved(), but that looks rather ugly and is definitely not user-friendly. We should fix that!
      */
    Boat move Cabbage to StartingShore
    Boat move Sheep to DestinationShore
    Boat move None to StartingShore
    Boat move Cabbage to DestinationShore
    Boat move Sheep to StartingShore
    Boat move Wolf to DestinationShore
    Boat move None to StartingShore

    /**
      * The ugly old call to Boat.transport still works, we should fix that!
      */
    if (goalAchieved(Boat transport(Sheep, DestinationShore))) {
      printSuccessMessage()
    } else {
      print("oh no, we've failed!")
    }

  }
}
