require 'lib/redis_bayes'
require 'lib/datafile'
require 'test/unit'

class TestRedisBayes < Test::Unit::TestCase

  def test_it_trains
    d = DataFile.new("data/mushroom-train.csv")
    b = RedisBayes.new
    b.clear
    b.train(d)
    assert_equal b.count, 6124
  end

  def test_it_classifies
    train = DataFile.new("data/mushroom-train.csv")
    test = DataFile.new("data/mushroom-test.csv")
    b = RedisBayes.new
    b.train(train)
    count = 0
    total = 0
    test.each do |klass,features|
      c = b.classify(features)
      total += 1
      count += 1 if klass == c
    end
    assert count.to_f / total > 0.9, "low classification accuracy"
  end

end
