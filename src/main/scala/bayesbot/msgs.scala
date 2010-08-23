package bayesbot

import actors.Actor

object msgs {
  sealed trait Message
  case class RegisterActor(actor:Actor) extends Message
  case class AddSample(features: Seq[String], klass: String) extends Message
  case class UpdateClassifier(classifier: BayesClassifier) extends Message
  case class ClassifyRequest(features: Seq[String]) extends Message
  case class ClassifyResult(classes: Seq[(String,Double)]) extends Message
}

