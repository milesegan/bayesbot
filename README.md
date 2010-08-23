# bayesbot

Bayesbot is a naive bayesian classifier exposed as a simple
web service.

## api

The api supports three functions:

* classify
* add sample
* bulk-load

### adding samples
To load a new sample, send a post request to / with post data like this:

    c=class
    f=feature1
    f=feature2
    ...

### classifying samples
To classify a sample, send a get request to / in a format like this:

    get /?f=feature1&f=feature2&f=feature3

Bayes bot will respond with a single text line containing the class
that maps to the given features with the highest probability.

### bulk load
Loading one at a time over individual posts can be slow so bayesbot
also supports a bulk-load function. To bulk-load data post to /bulk-load/
with a data parameter containing the contents of a file formatted like this:

    class  feature1  feature2  feature3
    class2 feature1  feature2  feature3

Where all fields are tab separated.
