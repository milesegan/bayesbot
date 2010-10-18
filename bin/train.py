#!/usr/bin/env python
# bulk-loads a dataset from a text file

import json
import redis
import string,sys

class Bayes(object):

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

    def to_json(s):
        return json.dumps({ 'count': s.count, 
                            'pc': s.pc, 
                            'pf': s.pf, 
                            'pcf': s.pcf })

file = open(sys.argv[1])
points = []
features = file.readline().split(",")[1:]
b = Bayes()
for f in file:
    if f.find("#") == 0:
        continue
    fields = f.split(",")
    klass = fields[0]
    fields = zip(features, fields[1:])
    b.add_sample(klass, [string.join(i, "__") for i in fields])

r = redis.Redis()
r.set("bc", b.to_json())
r.set("serial", 1)

"""    
file = DataFile.new(ARGV.first)
b = Bayes.load_from_datafile(file)

r = Redis.new
serial = r.get "serial"
r.set "bc", JSON.dump(b)
r.set "serial", serial.to_i + 1
"""

