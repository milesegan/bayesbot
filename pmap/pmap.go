package pmap

type Feature struct {
	Name  string
	Value string
}

type featureClass struct {
	feature Feature
	class string
}

type Map struct {
	probabilities map[featureClass]float64
	entries       int64
	classes       map[string]bool
}

func New() *Map {
	return &Map{
		make(map[featureClass]float64),
		0,
		make(map[string]bool),
	}
}

func (m *Map) Train(features []Feature, class string) {
	for _, feature := range features {
		fc := featureClass{feature, class}
		m.probabilities[fc] += 1
		m.entries += 1
		m.classes[class] = true
	}
}

type Prediction map[string]float64

func (m *Map) Predict(features []Feature) Prediction {
	pm := make(Prediction)
	for class, _ := range m.classes {
		pm[class] = 1.0;
		for _, feature := range features {
			fc := featureClass{feature, class}
			pm[class] *= m.probabilities[fc] / float64(m.entries)
		}
	}
	return pm
}
