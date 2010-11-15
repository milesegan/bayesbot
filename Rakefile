$: << File.dirname(__FILE__)

require 'shoulda'

task :default => [:test]

task :test do
  Dir["test/test*.rb"].sort.each { |test| load test }
end
