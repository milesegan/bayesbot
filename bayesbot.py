#!/usr/bin/env python

import redis

from flask import Flask, request, g
app = Flask(__name__)

@app.before_request
def connect():
    if g.redis == None:
        g.redis = redis.Redis()

@app.route("/")
def classify():
    f = request.args.getlist("f")
    return "hello" + str(f)

if __name__ == "__main__":
    app.run(debug = True)
