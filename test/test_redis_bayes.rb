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

end
