package bayesbot

import java.lang.ThreadLocal
import javax.servlet._
import javax.servlet.http._
import actors.Actor

/**
 * Main servlet. Handles both add and classify requests.
 */
class BayesServlet extends HttpServlet {

  lazy val actor = new ThreadLocal[Actor] {
    override def initialValue = synchronized { 
      val w = new WorkerActor()
      w.start
      w
    }
  }

  override def init() = BayesActor.start

  override
  def doGet(request: HttpServletRequest, response: HttpServletResponse) {
    val out = response.getWriter
    val features = request.getParameterValues("f")
    if (features == null || features.isEmpty) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST)
      out.println("no features specified")
    }
    else {
      actor.get() ! msgs.ClassifyRequest(features.toSeq)
      Actor.self.receiveWithin(1000) {
        case msgs.ClassifyResult(results) => {
          out.println(results.head._1)
        }
        case None => {
          response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
          out.println("no response from classifier")
        }
      }
    }
  }

  override
  def doPost(request: HttpServletRequest, response: HttpServletResponse) {
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
