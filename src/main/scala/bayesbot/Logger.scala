package bayesbot

import org.slf4j.LoggerFactory

object Logger {

  lazy val logger = LoggerFactory.getLogger(Logger.getClass)

  def info(msg: String) = logger.info(msg)
  
}
