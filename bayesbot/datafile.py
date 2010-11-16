import random

class DataFile(object):
    """Reads csv files containing class, feature lists. Streams to 
    save memory."""

    SEP = ","

    def __init__(s, path, transform_func = None):
        s.file = open(path, "r")
        s.features = None
        s.count = 0
        s.transform_func = transform_func
        for line in s.file:
            if line.find("#") == 0:
                continue
            parts = line.strip().split(s.__class__.SEP)
            if s.features == None:
                s.features = parts[1:]
                break

    def points(s):
        for line in s.file:
            parts = line.strip().split(s.__class__.SEP)
            klass, parts = parts[0], parts[1:]
            if s.transform_func:
                parts = [s.transform_func(p) for p in parts]
            data = zip(s.features, parts)
            s.count += 1
            yield(klass, dict(data))

    def split(s, fraction, shuffle = False):
        allp = [p for p in s.points()]
        if shuffle:
            random.shuffle(allp)
        split = len(allp) / fraction
        return allp[:split], allp[split:]
        
