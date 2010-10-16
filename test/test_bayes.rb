require 'test/unit'
require 'rack/test'
require 'bayesapp'
require 'lib/bayes'
require 'lib/datafile'

ENV['RACK_ENV'] = 'test'

class BayesTest < Test::Unit::TestCase

  include Rack::Test::Methods

  def app
    Sinatra::Application
  end

  def test_it_adds_a_sample
    bc = Bayes.new
    bc.add_sample("class", ["f-a", "f-b", "f-c"])
    assert bc.count == 1
  end

  def test_it_loads_from_datafile
    d = DataFile.new("data/mushroom-train.csv")
    b = Bayes.load_from_datafile(d)
    assert_equal b.count, 6124
  end

  def test_it_loads_from_json
    d = open("test/bc.json")
    b = Bayes.load_from_json(d)
    assert_equal b.count, 6124
  end

  def test_it_classifies
    d = DataFile.new("data/mushroom-train.csv")
    b = Bayes.load_from_datafile(d)
    c = b.classify(["cap-color__u", "ring-type__f", "stalk-color-above-ring__n"])
    assert_equal c.first.first, "e"
  end

end
