#!/usr/bin/env python

from __future__ import division
import os, sys
import redis
from flask import Flask, request
app = Flask(__name__)

class DB(object):
    __conn = None
    @classmethod 
    def conn(cls):
        cls.__conn = cls.__conn or redis.Redis()
        return cls.__conn

@app.route("/")
def classify():
    r = DB.conn()
    features = request.args.getlist("f")
    classes = r.hgetall("pc")
    total = sum([int(i) for i in classes.values()])
    ranked = []
    for klass in classes.keys():
        p = 1.0
        fcs = [f + "__" + klass for f in features]
        fcounts = r.hmget("pfc", fcs)
        for count in fcounts:
            if count:
                p *= int(count) / total
            else:
                p *= 0.01 / total # TODO: adjust this
        ranked.append((p * int(classes[klass]), klass))
    ranked.sort()
    return ranked[-1][1]

if __name__ == "__main__":
    app.run(debug = True)
