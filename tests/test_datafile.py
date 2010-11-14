from bayesbot.datafile import DataFile

import unittest

class DataFileTest(unittest.TestCase):

    def test_it_loads(s):
        d = DataFile("data/mushroom.csv")
        s.assertEqual(d.features[0], "cap-shape")
        for klass, features in d.points():
            s.assertIn(klass, ["p", "e"])
            s.assertIn(features["cap-shape"], ["f", "x", "b", "s", "k", "c"])
        s.assertEqual(d.count, 8124)
