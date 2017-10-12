package example.basics

import fluent._
import fluent.FluentActor.Message

class Paths() extends FluentProgram {
  override val name = "hello"
  override val host = "localhost"
  override val port = 8000

  // TODO: might be interesting to check out Scala symbols so we don't repeat ourselves for variable and key name
  val link = new Table[Tuple3[String, String, Int]]("link", List("from", "to", "cost"))
  val path = new Table[Tuple4[String, String, String, Int]]("path", List("from", "to", "next", "cost"))

  // TODO: need some concept of key / values to have primary key-like functionality
  // val shortest = new Table[Tuple2[String, String] => Tuple2[String, Cost]]("shortest", List("from", "to", "next", "cost"))

  override val collections = List(link)
  override val rules = {
    import fluent.Rule.Infix
    // TODO: would be nice to address tuples in table by column name
    val rule1 = path += Relation(link).map(t => Tuple4(t._1, t._2, t._2, t._3))

    // TODO: make specifying columns for join easier?
    val rule2 = path += EquiJoin(
      Relation(link),
      (t: Tuple3[String, String, Int]) => t._2,
      Relation(path),
      (t: Tuple4[String, String, String, Int]) => t._1

      // TODO: Damn dude that looks bad
    ).map(t => Tuple4(t._1._1, t._2._2, t._1._2, t._1._3 + t._2._4))

    // TODO: Missing a rule3 which computes shortest path. Depends on key/value.
    List(rule1, rule2)
  }
}

object Paths {
  def main(args: Array[String]) = {
    val paths = new Paths()
    val (system, actor) = paths.run()

    // TODO: right now, we only merge one tuple at a time
    actor ! Message("link", Tuple3("a", "b", 1))
    actor ! Message("link", Tuple3("a", "b", 4))
    actor ! Message("link", Tuple3("b", "c", 1))
    actor ! Message("link", Tuple3("c", "d", 1))
    actor ! Message("link", Tuple3("d", "e", 1))
  }
}
