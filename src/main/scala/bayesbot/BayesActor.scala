package bayesbot

import actors.Actor
import msgs._

object BayesActor extends Actor {

  var classifier = BayesClassifier()
  var actors = Set.empty[Actor]

  def act() {
    while (true) {
      receive {
        case RegisterActor(actor) => {
          actors = actors + actor
          actor ! UpdateClassifier(classifier)
        }
        case AddSample(features, klass) => {
          classifier = classifier.addSample(features, klass)
          actors.foreach(a => a ! UpdateClassifier(classifier))
        }
      }
    }
  }
  
}

