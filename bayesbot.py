#!/usr/bin/env python
from __future__ import division
import os, sys
from bayesbot.redis_bayes import RedisBayes
from flask import Flask, request
app = Flask(__name__)

rb = None

@app.route("/")
def classify():
    global rb
    rb = rb or RedisBayes()
    features = request.args.getlist("f")
    features = [i.split(RedisBayes.SEP) for i in features]
    features = dict(features)
    c = rb.classify(features)
    return c

if __name__ == "__main__":
    app.run()#debug = True)
