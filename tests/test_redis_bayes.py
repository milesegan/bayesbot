from __future__ import division
from bayesbot.redis_bayes import RedisBayes
from bayesbot.datafile import DataFile

import unittest

class TestRedisBayes(unittest.TestCase):
    
    def test_it_trains(s):
        d = DataFile("data/mushroom.csv")
        b = RedisBayes()
        b.train(d.points(), True)
        s.assertEqual(b.classes, {"p": 3916, "e": 4208})
        s.assertEqual(b.count, 8124)

    def test_it_classifies(s):
        d = DataFile("data/mushroom.csv")
        test, train = d.split(4)
        b = RedisBayes()
        b.train(train, True)
        correct = 0
        total = 0
        for klass, features in test:
            c = b.classify(features)
            total += 1
            if klass == c:
                print("{} == {}".format(klass, c))
                correct += 1
            else:
                print("{} <> {}".format(klass, c))
        print(correct / total)
        s.assertGreater(correct / total, 0.9)
