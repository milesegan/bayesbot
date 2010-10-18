# simple bayesian classifier in python

from __future__ import division
import random
import os, re, sys

class Classifier(object):

    def __init__(s):
        s.pc = {}
        s.pf = {}
        s.pcf = {}
        s.count = 0

    def add_sample(s, klass, feats):
        s.count += 1
        s.pc.setdefault(klass, 0)
        s.pc[klass] += 1
        for f in feats:
            s.pf.setdefault(f, 0)
            s.pf[f] += 1
            s.pcf.setdefault(f, dict())
            s.pcf[f].setdefault(klass, 0)
            s.pcf[f][klass] += 1

    def classify(s, features):
        ranked = []
        for c in s.pc.keys():
            p = 1
            for f in features:
                pfckey = f + (c,)
                if s.pfc.has_key(pfckey):
                    p *= s.pfc.get(pfckey) / s.count
                else:
                    p *= 0.01 / s.count # TODO: adjust this
            ranked.append((p * s.pc.get(c, 0.0), c))
        ranked.sort()
        return ranked[-1][1]

    def to_json(s):
        return json.dumps({ 'count': s.count, 
                            'pc': s.pc, 
                            'pf': s.pf, 
                            'pcf': s.pcf })
