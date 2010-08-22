package bayesbot

import actors.Actor
import msgs._

/**
 * Maintains master copy of classifier and feeds updated versions
 * to worker actors.
 */
object BayesActor extends Actor {

  var classifier = BayesClassifier()
  var actors = Set.empty[Actor]

  def act() {
    while (true) {
      receive {
        case RegisterActor(actor) => {
          actors = actors + actor
          reply(classifier) // use reply because this message will be synchronous
        }
        case AddSample(features, klass) => {
          classifier = classifier.addSample(features, klass)
          actors.foreach(a => a ! UpdateClassifier(classifier))
        }
      }
    }
  }
  
}

