import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import bayesbot.BayesClassifier

class BayesClassifierSpec extends FlatSpec with ShouldMatchers {
  
  var bc = BayesClassifier()
  val samples = List(
    "gnome" -> List("short", "stout", "jolly"),
    "elf" -> List("tall", "slender", "solemn"),
    "orc" -> List("tall", "stout", "fierce"),
    "pixie" -> List("short", "slender", "playful"))

  for ((klass, features) <- samples) {
    bc = bc.addSample(features, klass)
  }

  "a BayesClassifier" should "add samples" in {
    bc.classes.size should be === 4
  }

  it should "calculate probability correctly" in {
    bc.probability("short", "gnome") should be === 0.125
    bc.probability("short", "orc") should be === 0.025
    bc.probability("short", "pixie") should be === 0.125
  }

  it should "classify samples" in {
    var classified = bc.classify(List("short", "stout", "jolly"))
    classified.map(_._1) should be === Seq("gnome", "pixie", "orc", "elf")

    classified = bc.classify(List("tall", "stout", "fierce"))
    classified.map(_._1) should be === Seq("orc", "elf", "gnome", "pixie")
  }
  
}
