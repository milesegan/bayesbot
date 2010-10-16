$: << File.join(File.dirname(__FILE__), "lib")

require 'rubygems'
require 'sinatra'
require 'redis'
require 'bayes'

class Classifier

  @@serial = nil
  @@bc = nil

  def self.get
    @@redis ||= Redis.new
    newserial = @@redis.get "serial"
    if newserial != @@serial
      @@serial = newserial
      @@bc = Bayes.load_from_json(@@redis.get("bc"))
    end
    @@bc
  end

end

get "/" do
  bc = Classifier.get
  feats = params[:f]
  if feats.nil?
    status 400
    "error - no features specified"
  else
    c = bc.classify(feats).first.first
    c
  end
end
