package bayesbot

import javax.servlet._
import javax.servlet.http._
import actors.Actor

case object GetMsg
case class SetMsg(msg: String)
case class RegisterActor(a: Actor)

object BayesClassifier extends Actor {

  var actors = List.empty[Actor]

  def act() {
    while (true) {
      receive {
        case RegisterActor(actor) => actors = actors :+ actor
        case GetMsg => actors.foreach(a => a ! SetMsg(util.Random.nextInt(3000).toString))
      }
    }
  }
  
}

class ServletActor(boss: Actor) extends Actor {
  var message = "boo"

  def act() {
    boss ! RegisterActor(this)

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
  val actor = new ServletActor(BayesClassifier)
  actor.start

  override
  def doGet(request: HttpServletRequest, response: HttpServletResponse) = {
    val out = response.getWriter
    if (request.getRequestURI.startsWith("/reset")) {
      out.println("reset")
      BayesClassifier ! GetMsg
    }
    out.println(<h1>hello</h1>)
    //out.println("hello")
    out.println(actor !? (1000, GetMsg))
  }

}
