package bayesbot

import javax.servlet._
import javax.servlet.http._

class Servlet extends HttpServlet {

  override
  def doGet(request: HttpServletRequest, response: HttpServletResponse) = {
    val out = response.getWriter
    out.println("hello cleveland!!!")
  }

}
