package bayesbot

/**
 * Naive Bayesian classifier.
 */
class BayesClassifier private(
  val classes: Map[String,Double],
  val features: Map[String,Double],
  val featureClasses: Map[(String,String),Double],
  val count: Double) {

  /**
   * Increments values in map for every key in keys.
   */
  private def incKeys[A](map: Map[A,Double], keys: Seq[A]) = {
    (map /: keys) { (m,k) => m.updated(k, m.getOrElse(k, 0d) + 1d) }
  }

  /**
   * Adds a sample to the map, returning a new map
   * incorporating it.
   */
  def addSample(feat: Seq[String], klass: String): BayesClassifier = {
    val newFs = incKeys(features, feat)
    val newCs = incKeys(classes, Seq(klass))
    val newFCPairs = for (f <- feat) yield (f, klass)
    val newFCs = incKeys(featureClasses, newFCPairs)
    new BayesClassifier(newCs, newFs, newFCs, count + 1d)
  }

  /**
   * Classify a new sample based on prior samples
   *
   * @return A sequence of classes & their probabilities,
   * in order of decreasing likelihood.
   */
  def classify(feat: Seq[String]): Seq[(String,Double)] = {
    val ranked = for (c <- classes.keySet) yield {
      val probs = for (f <- feat) yield probability(f, c)
      (c, probs.product)
    }
    ranked.toSeq.sortBy(_._2).reverse
  }

  /**
   * Computes the probability of class klass given
   * the feature.
   */
  def probability(f: String, klass: String): Double = {
    val p = for {pC <- classes.get(klass)
                 pF <- features.get(f)
                 pCF <- featureClasses.get(f, klass)} yield pCF * pC / (pF * count)
    p getOrElse 0.1 / count // TODO: optimize this
  }
}

object BayesClassifier {
  def apply() = {
    new BayesClassifier(Map.empty, Map.empty, Map.empty, 0)
  }
}
