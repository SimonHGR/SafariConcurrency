package main

import (
	"fmt"
	"sync"
)

var counter int64
var wait_group sync.WaitGroup

// before correcting, run with:
// go run --race readmodifywrite/readmodifywrite.go
// var l sync.Mutex

func incrementer() {
	defer wait_group.Done()
	for x := 0; x < 100_000_000; x++ {
		// l.Lock()
		counter++
		// l.Unlock()
	}
}

func main() {
	wait_group.Add(2)
	go incrementer()
	go incrementer()
	wait_group.Wait()
	fmt.Println("value of counter is", counter)
}
