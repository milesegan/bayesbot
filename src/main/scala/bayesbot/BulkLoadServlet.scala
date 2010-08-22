package bayesbot

import java.lang.ThreadLocal
import javax.servlet._
import javax.servlet.http._
import actors.Actor

/**
 * Provides bulk load feature over http post for
 * pre-loading larger datasets.
 */
class BulkLoadServlet extends HttpServlet {

  override
  def doPost(request: HttpServletRequest, response: HttpServletResponse) {
    val out = response.getWriter
    val data = request.getParameter("data")
    if (data == null || data.isEmpty) {
      out.println("no data sent")
      return
    }

    for (point <- data.split("\n")) {
      val parts = point.split("\t")
      val klass = parts.head
      val features = parts.tail
      BayesActor ! msgs.AddSample(features, klass)
    }
  }

}
