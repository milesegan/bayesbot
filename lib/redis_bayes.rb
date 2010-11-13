require 'redis'

class RedisBayes

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
        features[[f, v, pklass].join("__")] += 1
      end
    end
    conn.hmset "classes", *(classes.to_a.flatten)
    conn.hmset "features", *(features.to_a.flatten)
  end

  def count
    classes = conn.hgetall "classes"
    counts = classes.values.collect(&:to_i)
    counts.inject(0) { |sum, p| sum + p }
  end

  def classify(features)
  end

end
