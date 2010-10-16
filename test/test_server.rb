require 'bayesapp'
require 'lib/bayes'
require 'test/unit'
require 'rack/test'

ENV['RACK_ENV'] = 'test'

class HelloWorldTest < Test::Unit::TestCase
  include Rack::Test::Methods

  def app
    Sinatra::Application
  end

  def setup
    r = Redis.new
    bc = Bayes.load_from_datafile(DataFile.new("data/mushroom-train.csv"))
    r.set "bc", bc.to_json
    r.set "serial", 1
  end

  def test_it_handles_bad_requests
    get '/'
    assert last_response.client_error?
  end

  def test_it_says_hello_world
    get '/', :f => ["f-a", "f-b", "f-c"]
    assert last_response.ok?
    assert_equal 'e', last_response.body
  end

end
