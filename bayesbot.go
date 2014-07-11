package main

import "fmt"
import "log"
import "net/http"

type request struct {
	term     string
	response chan string
}

func core(read chan *request) {
	for {
		if r, ok := <-read; ok {
			r.response <- "core wrote string: " + r.term
		}
	}
}

func main() {
	read := make(chan *request, 100)
	go core(read)

	handler := func(w http.ResponseWriter, req *http.Request) {
		r := &request{"foo", make(chan string)}
		read <- r
		resp := <-r.response
		fmt.Fprintf(w, resp)
	}

	http.Handle("/", http.HandlerFunc(handler))
	err := http.ListenAndServe(":8000", nil)
	if err != nil {
		log.Fatal("couldn't start server:", err)
	} else {
		fmt.Println("server started")
	}
}
