# bayesbot

Bayesbot is a naive bayesian classifier exposed as a simple
web service. It is implemented as a Python/Flask web service
using Redis as the back-end.

## web api

The web api is very simple and supports only one function,
classification.

To classify a sample, send a get request to / in a format like this:

    get /?f=feature1&f=feature2&f=feature3

Bayes bot will respond with a single text line containing the class
that maps to the given features with the highest probability.

## training

Training is accomplished with the bin/train script provided. It
expects as input files containing one sample per line, with the class
of each sample provided as the first field in each line. The first
non-comment line of the file should be a comma separated list of the
names of the features specified in the file. Sample training files are
provided in the data directory.

## running bayesbot

Bayesbot is a Flask web service. To run bayesbot, first make sure
redis is running on the target host and install redis and flask via
easy_install/pip. Then start bayesbot directly as:

    python bayesbot.py

Bayesbot currently expects redis to be running on localhost on the
standard port but support for more flexible configuations will be
implemented shortly.

To verify that your instance of bayesbot is running correctly,
issue the following commands from the bayesbot root directory

    bin/train data/mushroom.csv
    bin/test data/mushroom.csv

You should see a series of string pairs. The left corresponds to the
known correct class for the test sample and the right corresponds to
the class predicted by bayesbot. At the end of the list of classes
the test script will print the percentage accuracy of bayesbot, which
should be > 0.95 for the mushroom sample.
