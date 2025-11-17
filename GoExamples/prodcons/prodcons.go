package main

import (
	"fmt"
	"math/rand/v2"
	"time"
)

func producer(data_channel chan<- [2]int, control_channel chan<- bool) {
	count := rand.IntN(9_000) + 2000
	fmt.Printf("producer starting, will produce %d values\n", count)
	for c := 0; c < int(count); c++ {
		data := [2]int{0, c}
		if c < 1000 {
			time.Sleep(1 * time.Millisecond)
		}
		data[0] = c
		data_channel <- data
	}
	fmt.Println("producer completed, informing controller")
	control_channel <- true
}

func consumer(data_channel <-chan [2]int, control_channel chan bool) {
	fmt.Println("Consumer starting")
	c := 0
outer:
	for ; ; c++ {
		select {
		case data := <-data_channel:
			if data[0] != data[1] || data[0] != c {
				fmt.Printf("**** Error at index %d, data is %s\n", c, data)
			}
			if c > 8500 {
				time.Sleep(1 * time.Millisecond)
			}
		case <-control_channel:
			break outer
		}
	}
	fmt.Printf("Consumer completed after processing %d items\n", c)
	control_channel <- true
}

func main() {
	data_channel := make(chan [2]int)
	producer_finished_channel := make(chan bool)
	shutdown_consumer_channel := make(chan bool)
	go producer(data_channel, producer_finished_channel)
	go consumer(data_channel, shutdown_consumer_channel)
	<-producer_finished_channel
	fmt.Println("Producer finished, informing consumer")
	shutdown_consumer_channel <- true
	fmt.Println("Consumer informed, awaiting acknowledgement of completion")
	<-shutdown_consumer_channel
	fmt.Println("main exiting")
}
