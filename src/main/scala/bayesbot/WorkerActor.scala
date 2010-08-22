package bayesbot

import actors.Actor
import msgs._

/**
 * Runs in servlet thread to answer classification
 * requests.
 */
class WorkerActor extends Actor {

  var classifier = BayesClassifier()

  def act() {

    BayesActor ! RegisterActor(this)

    while (true) {
      receive {
        case AddSample(features, klass) => {
          BayesActor ! AddSample(features, klass)
        }
        case UpdateClassifier(newc) => {
          this.classifier = newc
        }
        case Classify(features) => {
          sender ! classifier.classify(features)
        }
      }
    }
  }
}

