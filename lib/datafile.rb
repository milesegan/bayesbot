# datafile loading library

class DataFile
  attr_reader :count

  def initialize(path)
    @file = open(path)
    @sep = /\s*,\s*/
    klass, *@names = @file.readline.strip.split(@sep)
    @count = 0
  end

  def each_sample
    @file.each_line do |f|
      @count += 1
      klass, *parts = f.strip.split(@sep)
      next unless parts.size == @names.size
      features = @names.zip(parts).collect { |i| i.join("__") }
      yield [klass, features]
    end
  end

end
