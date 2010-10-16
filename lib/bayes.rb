require 'json'

class Bayes

  attr_reader :count, :pc, :pf, :pcf

  def self.load_from_json(json)
    json = JSON.load(json)
    Bayes.new(json)
  end

  def self.load_from_datafile(file)
    b = Bayes.new
    file.points.each do |klass,features|
      b.add_sample(klass, features)
    end
    b
  end

  def initialize(attrs = {})
    @count = 0
    @pc = {}
    @pf = {}
    @pcf = {}
    attrs.each do |k,v|
      instance_variable_set k, v
    end
  end

  def add_sample(klass, features)
    @count += 1
    @pc[klass] ||= 0
    @pc[klass] += 1
    features.each do |f|
      @pf[f] ||= 0
      @pf[f] += 1
      @pcf[f] ||= {}
      @pcf[f][klass] ||= 0
      @pcf[f][klass] += 1
    end
  end

  def classify(features)
    ranked = @pc.keys.collect do |c|
      probs = features.collect do |f|
        @pcf.fetch(f, {}).fetch(c, 0.05).to_f / @count
      end
      [c, probs.inject { |a,b| a * b } * @pc.fetch(c, 0).to_f / @count]
    end
    ranked.sort { |a,b| b.last <=> a.last }
  end

  def to_json(*a)
    h = {}
    instance_variables.each do |i|
      h[i] = instance_variable_get(i)
    end
    h.to_json(*a)
  end

end
