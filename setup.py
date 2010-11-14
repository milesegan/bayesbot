#!/usr/bin/env python

sdict = {
    "name": "bayesbot",
    "version": "0.1",
    "test_suite" : "tests",
    "author": "Miles Egan",
    "author_email": "milesegan@gmail.com",
    "url": "http://github.com/cageface/bayesbot",
    "packages": ["bayesbot"]
}

try:
    from setuptools import setup
except ImportError:
    from distutils.core import setup
    
setup(**sdict)
