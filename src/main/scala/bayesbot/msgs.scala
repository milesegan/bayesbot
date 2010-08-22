package bayesbot

import actors.Actor

object msgs {

  sealed abstract class Message
  case class RegisterActor(actor:Actor) extends Message
  case class AddSample(features: Seq[String], klass: String) extends Message
  case class UpdateClassifier(classifier: BayesClassifier) extends Message
  case class Classify(features: Seq[String]) extends Message

}

