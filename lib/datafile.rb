# datafile loading library

class DataFile
  attr_reader :points

  def initialize(path)
    file = open(path)
    sep = /\s*,\s*/
    klass, *names = file.readline.strip.split(sep)
    @points = []

    file.each_line do |i|
      klass, *parts = i.strip.split(sep)
      next unless parts.size == names.size
      
      features = names.zip(parts).collect { |i| i.join("-") }
      points << [klass, features]
    end
    @points
  end

end
