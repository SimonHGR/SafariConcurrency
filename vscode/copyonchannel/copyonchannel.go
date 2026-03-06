package main

import (
	"fmt"
)

type Data struct {
	x, y int
}

func reader(rc <-chan Data, done chan bool) {
	error := false
	x, y := 0, 10
	for d := range rc {
		if x != d.x || y != d.y {
			fmt.Printf("ERROR: data is %d, %d, expected %d, %d\n", d.x, d.y, x, y)
			error = true
		}
		fmt.Printf("RECEIVED: d is %T, value %v, address %p\n", d, d, &d)
		x++
		y--
	}
	fmt.Println("rc closed sending shut down notice")
	done <- true
	if error {
		fmt.Println("**** Errors were encountered")
	}
}

func main() {
	var c chan Data = make(chan Data)
	var sd chan bool = make(chan bool)
	go reader(c, sd)
	for i, j := 0, 10; i < j; i, j = i+1, j-1 {
		d := Data{i, j}
		c <- d
		fmt.Printf("SENT: %T, value %v, address %p\n", d, d, &d)
		d.x++ // write, but it's been copied
		d.y-- // ditto
	}
	close(c)
	fmt.Println("All data sent")
	<-sd
	fmt.Println("Ack received, shutting down")
}
