from __future__ import division

from collections import Counter
import redis

class RedisBayes(object):
    "Implements a naive bayes classifier with a redis backend."

    SEP = "__"

    def __init__(s):
        s._conn = None

    @property
    def conn(s):
        "Returns active redis connection."
        s._conn = s._conn or redis.Redis()
        return s._conn

    def clear(s):
        "Clears all data from the redis store."
        s.conn.flushall

    @property
    def classes(s):
        "Returns all observed classes and their counts as a dict."
        c = s.conn.hgetall("classes")
        for k, v in c.items():
            c[k] = int(v)
        return c

    @property
    def count(s):
        return sum(s.classes.values())
        
    def train(s, points, clear_data = False):
        """Builds the classifier from the supplied points. Erases old
        data if clear_data is specified."""
        if clear_data:
            s.clear()
        pc = Counter()
        pfc = Counter()
        for klass, features in points:
            pc[klass] += 1
            for f, v in features.items():
                key = s.__class__.SEP.join([f, v, klass])
                pfc[key] += 1
        s.conn.hmset("classes", pc)
        s.conn.hmset("features", pfc)

    def classify(s, features):
        "Classifies point with features based on trained data."
        allc = s.classes
        total = sum(allc.values())
        probs = {}
        for c in allc:
            p = 1
            fkeys = [s.__class__.SEP.join([f,v,c]) for f,v in features.items()]
            fkeys = s.conn.hmget("features", fkeys)
            for f in fkeys:
                if f:
                    p *= int(f) / total
                else:
                    p *= 0.01 / total # TODO: tune this
            probs[c] = p
        probs = sorted(probs.items(), lambda a,b: cmp(a[1], b[1]))
        return probs[-1][0]
