package bayesbot

import java.lang.ThreadLocal
import javax.servlet._
import javax.servlet.http._
import actors.Actor

case object GetMsg
case class SetMsg(msg: String)
case class RegisterActor(a: Actor)

object BayesClassifier extends Actor {

  var actors = List.empty[Actor]
  var nextN = "1"

  def act() {
    while (true) {
      receive {
        case RegisterActor(actor) => {
          actors = actors :+ actor
          actor ! SetMsg(nextN)
        }
        case GetMsg => {
          nextN = util.Random.nextInt(3000).toString
          actors.foreach(a => a ! SetMsg(nextN))
        }
      }
    }
  }
  
}

class ServletActor extends Actor {
  var message = "boo"

  def act() {
    BayesClassifier ! RegisterActor(this)

    while (true) {
      receive {
        case SetMsg(msg) => message = msg
        case GetMsg => sender ! message
      }
    }
  }
}

class Servlet extends HttpServlet {

  BayesClassifier.start
  val actor = new ThreadLocal[Actor] {
    override def initialValue = synchronized { new ServletActor().start }
  }

  override
  def doGet(request: HttpServletRequest, response: HttpServletResponse) = {
    val out = response.getWriter
    if (request.getRequestURI.startsWith("/reset")) {
      out.println("reset")
      BayesClassifier ! GetMsg
    }
    out.println(<h1>hello</h1>)
    out.println(actor.get() !? (1000, GetMsg))
  }

}
