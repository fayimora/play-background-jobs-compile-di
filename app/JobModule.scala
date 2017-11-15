import akka.actor.{ ActorRef, ActorSystem, Props }
import play.api.Configuration

import scala.concurrent.ExecutionContext

trait JobModule {
  import com.softwaremill.macwire._

  def actorSystem: ActorSystem
  def configuration: Configuration
  implicit def executionContext: ExecutionContext

  lazy val helloActor: ActorRef = actorSystem.actorOf(Props(wire[HelloJobActor]))
  lazy val scheduler: HelloScheduler = wire[HelloScheduler]
}
