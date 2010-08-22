package bayesbot

object Logger {

  private def logger = org.apache.log4j.Logger.getRootLogger

  def info(msg: String) = System.err.println(msg) // TODO: fix this
  
}
