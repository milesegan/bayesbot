package main

import "fmt"
import "log"
import "net/http"

type writeRequest struct {
	term     string
	value    float32
	response chan string
}

type readRequest struct {
	term     string
	response chan string
}

func core(read chan *readRequest, write chan *writeRequest) {
	terms := make(map[string]float32)
	for {
		select {
		case r := <-read:
			if value, ok := terms[r.term]; ok {
				r.response <- fmt.Sprintf("core read value: %s -> %f", r.term, value)
			} else {
				r.response <- fmt.Sprintf("term %s not found", r.term)
			}
		case w := <-write:
			terms[w.term] = w.value
			w.response <- fmt.Sprintf("core wrote value: %s -> %f", w.term, w.value)
		}
	}
}

func main() {
	read := make(chan *readRequest, 100)
	write := make(chan *writeRequest, 100)
	go core(read, write)

	readHandler := func(w http.ResponseWriter, req *http.Request) {
		r := &readRequest{"foo", make(chan string)}
		read <- r
		resp := <-r.response
		fmt.Fprintf(w, resp)
	}

	writeHandler := func(w http.ResponseWriter, req *http.Request) {
		r := &writeRequest{"foo", 1.0, make(chan string)}
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
