require 'redis'

class RedisBayes

  SEPARATOR = "__"

  def self.conn
    @conn ||= Redis.new
  end

  def conn; RedisBayes.conn; end

  def clear
    conn.flushall
  end

  def train(points)
    classes = Hash.new(0)
    features = Hash.new(0)
    points.each do |pklass,pfeatures|
      classes[pklass] += 1
      pfeatures.each do |f,v|
        features[[f, v, pklass].join(SEPARATOR)] += 1
      end
    end
    conn.hmset "classes", *(classes.to_a.flatten)
    conn.hmset "features", *(features.to_a.flatten)
  end

  def classes
    c = conn.hgetall "classes"
    c.each { |k,v| c[k] = v.to_i }
    c
  end

  def count
    sum(classes.values)
  end

  def classify(features)
    allc = classes
    total = sum(allc.values)
    probs = {}
    allc.keys.each do |c|
      fvs = features.collect { |f,v| [f,v,c].join(SEPARATOR) }
      values = conn.hmget "features", *fvs
      p = values.inject(1) { |prod,p| prod * p.to_f / total }
      probs[c] = p
    end
    probs.to_a.sort { |a,b| a.last <=> b.last }.last.first
  end

  def sum(values)
    values.inject(0) { |sum,p| sum + p }
  end

end
