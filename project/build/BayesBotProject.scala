import sbt._

class BayesBotProject(info: ProjectInfo) extends DefaultWebProject(info)
{
  override def pomPath = "pom.xml" // put pom in root

  // repositories
  val sonatypeJettyRepo = "sonatype repo" at "http://oss.sonatype.org/content/groups/jetty/"
  val mavenRepo = "maven repo" at "http://repo2.maven.org/maven2"

  val scalatest = "org.scalatest" % "scalatest" % "1.2" % "test"
  val jetty6 = "org.mortbay.jetty" % "jetty" % "6.1.25" % "test->default"
  val servlet = "javax.servlet" % "servlet-api" % "2.5" % "provided->default" 
  val slf4jApi = "org.slf4j" % "slf4j-api" % "1.6.1"
  val slf4jSimple = "org.slf4j" % "slf4j-simple" % "1.6.1"
}

