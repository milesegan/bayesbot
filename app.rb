$: << File.join(File.dirname(__FILE__), "lib")

require 'rubygems'
require 'sinatra'
require 'redis'
require 'redis_bayes'

class Classifier

  @bc = nil

  def self.get
    @bc ||= RedisBayes.new
  end

end

get "/" do
  bc = Classifier.get
  feats = params[:f].collect { |i| i.split(RedisBayes::SEPARATOR) }
  feats = Hash[feats]
  c = bc.classify(feats)
  c
end
