/**
  * Author: Nathan R.B. Perdijk
  *
  * This file contains the code as written during the JVMCON presentation "Writing a DSL for the Dense with Scala" held by
  * Nathan Perdijk and Jan-Hendrik Kuperus on January 30, 2018.
  * It is a bit messy because it was written on stage in about 30 minutes (and everything written was left in place as-is
  * except for the Disclaimers), but a cleaner solution will be available soon.
  */

/**
  * This import-management allows for the renaming of classes to something of the programmer's choosing. This can be used to
  * "fix" poorly named classes, to resolve naming conflicts between libraries or (in this case) to be able to effectively shadow
  * the originals while still referring them if necessary...
  */
import ExternalApi.{DestinationShore, Shore, StartingShore, Boat => ExternalApiBoat, Cabbage => ExternalApiCabbage, Creature => ExternalApiCreature, Sheep => ExternalApiSheep, Wolf => ExternalApiWolf}

/**
  * We need this import, so that the Implicits trait can use the features provided by the MyDsl package object
  */
import MyDsl._

/**
  * A package object is a convenient way to place a set of traits, classes, objects and
  * implicits that belong together into a single import, even if they are not in a single file!
  */
package object MyDsl extends Implicits {

  /**
    * Scala: a trait is much like a (Java) interface.
    *
    * We need the Placeable trait to mark all instances that are valid input for the class [[On]], such as Boat or Wolf.
    */
  trait Placeable

  /**
    * The Setup case class ENFORCES users of the DSL to create a complete setup before they try their moves.
    * It makes sure they place exactly once a Boat, a Wolf, a Sheep and a Cabbage (in that order) on a Shore of their choice.
    * @param boatOnShore
    * @param wolfOnShore
    * @param sheepOnShore
    * @param cabbageOnShore
    */
  case class Setup(boatOnShore: Boat On Shore,
                   wolfOnShore: Wolf On Shore,
                   sheepOnShore: Sheep On Shore,
                   cabbageOnShore: Cabbage On Shore) {
    /**
      * Scala: a def is a function/method. Defs without parameters can be written without parentheses:
      * it is Scala convention that only parameterless defs
      * with NO SIDE EFFECTS should be called in this matter. If a def has a side effect, it should be declared and called with ().
      *
      * However, remember that the primary function of a DSL is user-friendliness,
      * so there may be reasons to ignore this convention when writing/using a DSL.
      *
      * The execute() performs the actual placement and also checks whether the setup is a legal starting situation (none of the creatures get eaten).
      */
    def execute() = {
      boatOnShore()
      wolfOnShore()
      sheepOnShore()
      cabbageOnShore()
      StartingShore assertIsSafe()
      DestinationShore assertIsSafe()
    }
  }

  /**
    * Scala: the apply() def is a special case. Defining apply with zero or more parameters, allows you to call the particular
    * functionality by referencing the class or object that provides it and then adding ().
    * For example Wolf() would actually be a call to Wolf.apply().
    * apply() should be specified with functionality that does something that makes a kind of intuitive sense.
    * In this case apply provides a way to get the corresponding ExternalApiCreature from a Dsl.Creature
    *
    * A Creature trait that extends Placeable, so that the new Creatures are instances of Placeable and can thereby function
    * as input for an On Shore. We do not, however, want to accidentally pass our own custom objects to the ExternalApi,
    * because it doesn't know how to deal with them (it only checks for its own). This prevents us from simply extending ExternalApi.Creature
    * and mixing in Placeable: the compiler would not warn us that we're inputting objects that the ExternalApi accepts but doesn't recognise.
    */
  trait Creature extends Placeable {
    /**
      * We want an easy way to get e.g. an ExternalApiWolf when we have a Dsl.Wolf, as we need instances of ExternalApiCreature
      * to actually use the ExternalApi
      * @return
      */
    def apply(): ExternalApiCreature
  }

  /**
    * Scala: objects are singleton instances, useful for when you only want one instance of a particular type and a useful way to mimic
    * 'static' functionality you might know from Java.
    *
    * Giving the Wolf object a Wolf trait cleans up the parameter type when asking for the Wolf object.
    * The same goes for the Sheep and the Cabbage.
    * These three objects replace the originals from the ExternalApi, but care must be taken:
    * The ExternalApi explicitly checks for the presence of the original objects, so before calling the ExternalApi,
    * every object must be converted to its original instance...
    *
    * This is what happens in the def apply()
    */
  trait Wolf extends Creature
  object Wolf extends Wolf {
    override def apply(): ExternalApiCreature = ExternalApiWolf
  }

  trait Sheep extends Creature
  case object Sheep extends Sheep {
    override def apply(): ExternalApiCreature = ExternalApiSheep
  }

  trait Cabbage extends Creature
  object Cabbage extends Cabbage {
    override def apply(): ExternalApiCreature = ExternalApiCabbage
  }

  /**
    * Scala: a case class is very much like a class, except that it gives you a lot of functionality for free, such as:
    * - it comes with a free object of the same name, that has an apply() that returns a new instance of the class
    * (in this case:
    *   object MoveOrder {
    *     def apply(creature: Creature, shore: Shore) = new MoveOrder(creature, shore)
    *   }
    * )
    * This allows you to write MoveOrder(creature, shore), without using the keyword 'new'.
    * - every parameter is a public val (and therefore both accessible AND immutable!)
    * - an equals method that compares by structure, not reference (two MoveOrders with the same contents are equal)
    * - and more...
    *
    * The MoveOrder is not actually used yet, because this was when I ran out of time, but it will be used in the solution
    * to make the moving of the boat lazy, which allows us to do things repetitively between moves without cluttering our
    * intructions!
    * @param creature
    * @param shore
    */
  case class MoveOrder(creature: Creature, shore: Shore)


  /**
    * Scala: an Either takes two typed arguments and will contain... either the first (Left) or the second (Right)
    *
    * The ToWord provides a "to(shore: Shore)" method, which allows us to chain another single-parameter method call.
    * @param creature
    */
  case class ToWord(creature: Either[Creature, Option[Nothing]]) {
    /**
      * Scala: this def uses a pattern match rather than an if-statement with instanceof. Scala's pattern match is very powerful
      * and allows you to avoid cumbersome and ugly type casting: you can think of it as a very powerful switch statement.
      * @param shore
      * @return
      */
    def to(shore: Shore) = creature match {
      /**
        * Scala: Notice how the contents of the Left and Right can be accessed through naming them inside the pattern match!
        * (Disclaimer:
        * I have replaced the reference to creature below with c to avoid shadowing the creature in the class definition.
        * This managed to escape my attention on stage.)
        */
      case Left(c) => Boat.transport(c, shore)
      case Right(nothing) => Boat.transport(nothing, shore)
    }
  }

  /**
    * The original ExternalApi.Boat did not even have a trait, making it ugly to require it as a parameter.
    * More importantly, it doesn't support the DSL functionality we want. But now it does! You can give it Creatures without
    * wrapping them in a Some, and you can use the "flow" notation by only giving it a Creature or a None and then calling to(shore: Shore)
    * on the result (see [[ToWord]]).
    */
  trait Boat extends Placeable
  case object Boat extends Boat {
    def move(creature: Creature) = ToWord(Left(creature))
    def move(nothing: Option[Nothing]) = ToWord(Right(nothing))
    def transport(creature: Creature, shore: Shore) = ExternalApiBoat.transport(creature, shore)
    def transport(nothing: Option[Nothing], shore: Shore) = ExternalApiBoat.transport(nothing, shore)
  }

  /**
    * Scala: classes with two argument types can be written with infix notation in type declarations as well.
    * On[Boat, Shore] can actually be written as Boat On Shore. Neat!
    *
    * The On case class takes two typed arguments, allowing infix notation.
    * The apply() method uses a pattern match to decide what it should do with the boat or creature it is given.
    * Until the apply() method is called the Boat On Shore will not actually place the boat on that shore!
    * @param placeable
    * @param shore
    * @tparam P
    * @tparam S
    */
  case class On[P <: Placeable, S <: Shore](placeable: P, shore: S) {
    def apply() = placeable match {
      case b: Boat => shore.boatPresent =true
      case c: Creature => shore.placeCreature(c)
    }
  }
}

/**
  * Scala: implicits...
  * This is very powerful stuff that can make your life as a programmer or DSL user much easier...
  * Or harder if you use it irresponsibly!
  *
  * Implicits are things the scala compiler can use to make your code work, even if you haven't explicitly provided
  * everything it actually needs.
  * This sounds very scary, but it's doesn't have to be.
  *
  * If the compiler cannot completely parse the code, it will look in scope for implicits that can make up the missing pieces.
  * You can provide these implicits by importing them. The code below provides implicit classes and an implicit def,
  * but there are other kinds of implicits as well (notably: implicit arguments).
  *
  * The purpose of implicits is to save the programmer from repetitive and code-cluttering statements.
  * In that spirit they make excellent additions to DSL's, where the user would otherwise be burdened...
  *
  * Don't go overboard though: if you can use explicit conversion without hindering your user, you should!
  * If you have too many implicits in scope, you can make debugging quite difficult.
  */
trait Implicits {

  /**
    * Scala: An implicit class is a neat way to conveniently add a method to a class that doesn't have it... by implicitly converting
    * it into something else!
    *
    * When calling the def on(shore: Shore) on something of type Boat, the compiler notices that Boat doesn't have that def...
    * But it also notices this implicit class that takes a boat as a parameter and provides such an on(shore: Shore) def. How convenient!
    * @param boat
    */
  implicit class BoatToShore(boat: Boat) {
    /**
      * Now that we have both a Boat (class parameter) and a Shore (method parameter), we can make a Boat On Shore (see [[On]])
      * by calling the generated companion object's apply(boat: Boat, shore: Shore) of On
      *
      * @param shore
      * @return Boat On Shore
      */
    def on(shore: Shore): Boat On Shore = On(boat, shore)
  }

  /**
    * We provide an implicit class for every type of creature, rather than one for all Creatures, because we want to force the DSL user
    * to provide one of each kind in the setup, rather than accidentally placing a Sheep twice and doing no Cabbage placement...
    * With type safety, we can prevent this! :-)
    * @param wolf
    */
  implicit class WolfOnShore(wolf: Wolf) {
    def on(shore: Shore): Wolf On Shore = On(wolf, shore)
  }

  implicit class SheepOnShore(sheep: Sheep) {
    def on(shore: Shore): Sheep On Shore = On(sheep, shore)
  }

  implicit class CabbageOnShore(cabbage: Cabbage) {
    def on(shore: Shore): Cabbage On Shore = On(cabbage, shore)
  }

  /**
    * Scala: implicit defs fall under "advanced language features", which is basically a HANDLE WITH CARE-notice that the compiler provides.
    * You can import scala.language.implicitConversions to let the compiler know that you (think you) know what you're doing.
    *
    * An implicit def can be used to transform something in place to something different if that is what the compiler expects.
    * In this case, the compiler expects an Option[ExternalApiCreature], but you only provide a Creature... Luckily, if you import
    * this def, the compiler can do that conversion for you, by parsing the creature into this method and then using the result
    * instead! What it does is that it calles the apply() def on Creature (which returns an ExternalApiCreature) and then wraps
    * it in a Some (which is the type of option that contains something).
    * (Disclaimer: I renamed the method to actually describe what it does, it claimed to be doing the inverse, whoops!)
    *
    * @param creature
    * @return Option[ExternalApiCreature]
    */
  implicit def creatureToSome(creature: Creature): Option[ExternalApiCreature] = Some(creature())

}


/**
  * Renamed imports I didn't get around to using in the talk.
  */
//import ExternalApi.{StartingShore => ExternalApiStartingShore}
//import ExternalApi.{DestinationShore => ExternalApiDestinationShore}
//import ExternalApi.{Shore => ExternalApiShore}


