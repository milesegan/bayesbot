package bayesbot

import actors.Actor
import msgs._

/**
 * Runs in servlet thread to answer classification
 * requests.
 */
class WorkerActor extends Actor {

  // register with BayesActor and get initial copy of our classifier
  var classifier: BayesClassifier = 
    (BayesActor !? RegisterWorker(this)).asInstanceOf[BayesClassifier]

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
