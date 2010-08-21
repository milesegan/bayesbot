import sbt._

class BayesBotProject(info: ProjectInfo) extends DefaultWebProject(info)
{
  val sonatypeJettyRepo = "sonatype repo" at "http://oss.sonatype.org/content/groups/jetty/"

  //override def dependencyPath = webappPath / "WEB-INF" / "lib"

  val scalatest = "org.scalatest" % "scalatest" % "1.2"
  val jetty6 = "org.mortbay.jetty" % "jetty" % "6.1.25" % "test->default"
  val servlet = "javax.servlet" % "servlet-api" % "2.5" % "provided->default" 
  //val log4j = "log4j" % "log4j" % "1.2.16" % "provided->default" 

}

