## This is not needed for Thin > 1.0.0
ENV['RACK_ENV'] = "production"

$: << File.dirname(__FILE__)

require 'bayesapp'

run Sinatra::Application
