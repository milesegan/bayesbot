package bayesbot

import actors.Actor
import msgs._

/**
 * Runs in servlet thread to answer classification
 * requests.
 */
class WorkerActor extends Actor {

  var classifier: BayesClassifier = (BayesActor !? RegisterActor(this)).asInstanceOf[BayesClassifier]

  def act() {
    loop {
      react {
        case AddSample(features, klass) => {
          BayesActor ! AddSample(features, klass)
        }
        case UpdateClassifier(newc) => {
          classifier = newc
        }
        case ClassifyRequest(features) => {
          sender ! ClassifyResult(classifier.classify(features))
        }
      }
    }
  }
}

