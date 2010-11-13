require 'lib/datafile'
require 'test/unit'

class DataFileTest < Test::Unit::TestCase

  def test_it_loads
    d = DataFile.new("data/mushroom-train.csv")
    assert_equal d.names.first, "cap-shape"
    count = 0
    d.each do |p|
      assert ['e', 'p'].include?(p.first)
      if count == 0
        assert_equal p.last['cap-shape'], 'x'
      end
      count += 1
    end
    assert_equal count, 6124
  end

end
