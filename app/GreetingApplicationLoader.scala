
import _root_.controllers.AssetsComponents
import com.softwaremill.macwire._
import play.api.ApplicationLoader.Context
import play.api._
import play.api.i18n._
import play.api.mvc.EssentialFilter
import play.api.routing.Router
import router.Routes

/**
 * Application loader that wires up the application dependencies using Macwire
 */
class GreetingApplicationLoader extends ApplicationLoader {
  def load(context: Context): Application = {
    LoggerConfigurator(context.environment.classLoader).foreach {
      _.configure(context.environment, context.initialConfiguration, Map.empty)
    }
    new GreetingComponents(context).application
  }
}

class GreetingComponents(context: Context) extends BuiltInComponentsFromContext(context)
  with GreetingModule
  with JobModule
  with AssetsComponents
  with I18nComponents 
  with play.filters.HttpFiltersComponents {

  lazy val router: Router = {
    // add the prefix string in local scope for the Routes constructor
    val prefix: String = "/"
    wire[Routes]
  }

  lazy val logFilter: LoggingFilter = wire[LoggingFilter]

  override def httpFilters: Seq[EssentialFilter] = {
    super.httpFilters ++ Seq(logFilter)
  }

  // run the scheduler
  scheduler.run()
}
