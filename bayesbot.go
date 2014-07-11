package main

import "fmt"
import "log"
import "net/http"

type feature struct {
	name  string
	value string
}

type featureClass struct {
	feature feature
	class   string
}

type writeRequest struct {
	features []featureClass
	response chan string
}

type readRequest struct {
	features []feature
	response chan string
}

type bayesMap struct {
	probabilities map[featureClass]float64
	entries       int64
	classes       map[string]bool
}

func newBayesMap() bayesMap {
	return bayesMap{
		make(map[featureClass]float64),
		0,
		make(map[string]bool),
	}
}

func core(read chan *readRequest, write chan *writeRequest) {
	bmap := newBayesMap()
	for {
		select {
		case r := <-read:
			probs := make(map[string]float64)
			for class, _ := range bmap.classes {
				probs[class] = 1.0
				for _, feature := range r.features {
					fc := featureClass{feature, class}
					probs[class] *= bmap.probabilities[fc] / float64(bmap.entries)
				}
			}
			r.response <- fmt.Sprintf("%v", probs)
		case w := <-write:
			for _, feature := range w.features {
				bmap.probabilities[feature] += 1
				bmap.entries += 1
				bmap.classes[feature.class] = true
			}
			w.response <- fmt.Sprintf("core wrote values")
		}
	}
}

func main() {
	read := make(chan *readRequest, 100)
	write := make(chan *writeRequest, 100)
	go core(read, write)

	readHandler := func(w http.ResponseWriter, req *http.Request) {
		features := []feature{feature{"foo", "bar"}}
		r := &readRequest{features, make(chan string)}
		read <- r
		resp := <-r.response
		fmt.Fprintf(w, resp)
	}

	writeHandler := func(w http.ResponseWriter, req *http.Request) {
		r := &writeRequest{[]featureClass{featureClass{feature{"foo", "bar"}, "yes"}}, make(chan string)}
		write <- r
		resp := <-r.response
		fmt.Fprintf(w, resp)
	}

	http.Handle("/read", http.HandlerFunc(readHandler))
	http.Handle("/write", http.HandlerFunc(writeHandler))
	err := http.ListenAndServe(":8000", nil)
	if err != nil {
		log.Fatal("couldn't start server:", err)
	} else {
		fmt.Println("server started")
	}
}
