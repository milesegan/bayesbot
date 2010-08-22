package bayesbot

import java.lang.ThreadLocal
import javax.servlet._
import javax.servlet.http._
import actors.Actor

class Servlet extends HttpServlet {

  BayesActor.start

  val actor = new ThreadLocal[Actor] {
    override def initialValue = synchronized { 
      val w = new WorkerActor()
      w.start
      w
    }
  }

  override
  def doGet(request: HttpServletRequest, response: HttpServletResponse) = {
    val out = response.getWriter
    val features = request.getParameterValues("f")
    if (features == null || features.isEmpty) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST)
      out.println("no features specified")
    }
    else {
      val classes = (actor.get() !? (1000, msgs.Classify(features.toSeq)))
      classes match {
        case Some(cs:Seq[(String,Double)]) => {
          for ((c,prob) <- cs) {
            Logger.info(c + " " + prob)
            out.println(c + " " + prob)
          }
        }
      }
    }
  }

  override
  def doPost(request: HttpServletRequest, response: HttpServletResponse) = {
    val out = response.getWriter
    val klass = request.getParameter("c")
    val features = request.getParameterValues("f")

    if (klass == null || features == null || features.isEmpty) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST)
      out.println("missing class or features")
    }
    else {
      actor.get() ! msgs.AddSample(features.toSeq, klass)
    }
  }

}
