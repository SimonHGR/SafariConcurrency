package main

import "fmt"

func main() {
	a()
}

func a() {
	var x = 10
	var y = "He"
	msg := b(x, y+"llo")
	fmt.Println(msg)
}

func b(p int, q string) string {
	return fmt.Sprintf(
		"%s repeated %d times\n", q, p)
}
