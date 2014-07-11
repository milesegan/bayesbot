package main

import "fmt"
import "log"
import "net/http"
import "github.com/milesegan/bayesbot/pmap"

type writeRequest struct {
	features []pmap.Feature
	class string
	response chan string
}

type readRequest struct {
	features []pmap.Feature
	response chan string
}

func core(read chan *readRequest, write chan *writeRequest) {
	bmap := pmap.New()
	for {
		select {
		case r := <-read:
			probs := bmap.Predict(r.features)
			r.response <- fmt.Sprintf("%v", probs)
		case w := <-write:
			bmap.Train(w.features, w.class)
			w.response <- fmt.Sprintf("core wrote values")
		}
	}
}

func main() {
	read := make(chan *readRequest, 100)
	write := make(chan *writeRequest, 100)
	go core(read, write)

	readHandler := func(w http.ResponseWriter, req *http.Request) {
		features := []pmap.Feature{pmap.Feature{"foo", "bar"}}
		r := &readRequest{features, make(chan string)}
		read <- r
		resp := <-r.response
		fmt.Fprintf(w, resp)
	}

	writeHandler := func(w http.ResponseWriter, req *http.Request) {
		feats := []pmap.Feature{pmap.Feature{"foo", "bar"}}
		r := &writeRequest{feats, "yes", make(chan string)}
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
