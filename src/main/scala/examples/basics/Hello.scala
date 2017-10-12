package examples.basics

import fluent._
import fluent.FluentActor.Message

class Hello() extends FluentProgram {
  override val name = "hello"
  override val host = "localhost"
  override val port = 8000

  val stdout = new Stdout()

  override val collections = List(stdout)
  override val rules = List()
}

object Hello {
  def main(args: Array[String]) = {
    val hello = new Hello()
    val (system, actor) = hello.run()
    actor ! Message("stdout", Tuple1("Hello, world!"))
  }
}
