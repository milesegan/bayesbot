package bayesbot

/**
 * Naive Bayesian classifier.
 */
class BayesClassifier private(
  val classes: Map[String,Long],
  val features: Map[String,Long],
  val featureClasses: Map[String,Map[String,Long]],
  val count: Long) {

  /**
   * Adds a sample to the map, returning a new map
   * incorporating it.
   */
  def addSample(feat: Seq[String], klass: String): BayesClassifier = {
    val newFs = for (f <- feat) yield (f, features.getOrElse(f, 0L) + 1L)
    val newCs = (klass, classes.getOrElse(klass, 0L) + 1L)
    val newFCs = for (f <- feat) yield {
      val newFC = featureClasses.getOrElse(f, Map.empty[String,Long])
      val newFCval = (klass, newFC.getOrElse(klass, 0L) + 1L)
      (f, newFC + newFCval)
    }
    new BayesClassifier(
      classes + newCs,
      features ++ newFs,
      featureClasses ++ newFCs,
      count + 1)
  }

  /**
   * Classify a new sample based on prior samples
   *
   * @return A sequence of classes & their probabilities,
   * in order of decreasing likelihood.
   */
  def classify(feat: Seq[String]): Seq[(String,Double)] = {
    val probs = for (c <- classes.keySet) yield (c, probability(feat, c))
    probs.toSeq.sorted
  }

  /**
   * Computes the probability of class klass given
   * the features.
   */
  def probability(feat: Seq[String], klass: String): Double = {
    val probs = for (f <- feat;
                     pC <- classes.get(klass);
                     pF <- features.get(f); 
                     pCFF <- featureClasses.get(f);
                     pCFC <- pCFF.get(klass)) yield {
                       pCFC * pC / pF * math.pow(count, 2) 
                     }
    if (probs.nonEmpty) probs.product
    else 0.1 / count // TODO: optimize this
  }
}

object BayesClassifier {
  def apply() = {
    new BayesClassifier(Map.empty, Map.empty, Map.empty, 0)
  }
}
