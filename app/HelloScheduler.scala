import akka.actor.{ Actor, ActorRef, ActorSystem }
import play.api.{ Configuration, Logger }

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._


class HelloJobActor(implicit ec: ExecutionContext) extends Actor {
  override def receive: Receive = {
    case s: String =>
      Logger.info(s"Hello $s!")
  }
}

class HelloScheduler (val system: ActorSystem,
                      val schedulerActor: ActorRef,
                      configuration: Configuration)(implicit ec: ExecutionContext) extends Scheduler {

  def run(): Unit = {
    val frequency: Int =  configuration.get[Int]("schedulling.hello.frequency")
    val delay: Int =  configuration.get[Int]("schedulling.hello.initialDelay")
    system.scheduler.schedule(delay.microseconds, frequency.seconds, schedulerActor, "Fayi")
  }
}


trait Scheduler {
  def run(): Unit
}