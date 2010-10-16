$: << File.dirname(__FILE__)
$: << File.join(File.dirname(__FILE__), "lib")

puts $:
task :default do
  # just run tests, nothing fancy
  Dir["test/*.rb"].sort.each { |test| load test }
end
