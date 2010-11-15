# datafile loading library

class DataFile
  include Enumerable
  attr_reader :points, :names, :size
  
  SEP = /\s*,\s*/

  def initialize(path)
    @file = open(path)
    while (line = @file.gets.strip) =~ /\s*#/
      # skip comments
    end
    klass, *@names = line.split(SEP)
  end

  def each
    while (line = @file.gets)
      klass, *parts = line.strip.split(SEP)
      next unless parts.size == @names.size
      features = Hash[@names.zip(parts)]
      yield [klass, features]
    end
  end

  def split(fraction, shuffle = false)
    all = to_a
    all = all.shuffle if shuffle
    split = all.size / fraction
    [all[0..split], all[split..-1]]
  end

end
