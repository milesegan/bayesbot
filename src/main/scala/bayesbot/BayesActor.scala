package bayesbot

import actors.Actor
import msgs._

/**
 * Maintains master copy of classifier and feeds updated versions
 * to worker actors.
 */
object BayesActor extends Actor {

  var classifier = BayesClassifier()
  var workers = Set.empty[Actor]

  def act() {
    loop {
      react {
        case RegisterWorker(worker) => {
          workers = workers + worker
          reply(classifier) // use reply because this message will be synchronous
        }
        case AddSample(features, klass) => {
          classifier = classifier.addSample(features, klass)
          workers.foreach(a => a ! UpdateClassifier(classifier))
        }
      }
    }
  }
  
}


