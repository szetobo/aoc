package main

import (
	"fmt"
	"io"
	"os"
	"strings"
)

func main() {
	data, err := io.ReadAll(os.Stdin)
	if err != nil {
		panic(err)
	}
	G := map[string][]string{}
	for _, line := range strings.Split(string(data), "\n") {
		if len(line) == 0 {
			continue
		}
		parts := strings.Split(line, ":")
		G[parts[0]] = strings.Split(strings.TrimSpace(parts[1]), " ")
	}

	p1, p2 := 0, 0

	var f1 func(string) int
	f1 = func(x string) int {
		if x == "out" {
			return 1
		}
		sum := 0
		for _, n := range G[x] {
			sum += f1(n)
		}
		return sum
	}
	p1 = f1("you")

	cache := map[string]int{}
	var f2 func(string, bool, bool) int
	f2 = func(x string, fft, dac bool) int {
		if res, ok := cache[fmt.Sprintf("%v,%v,%v", x, fft, dac)]; ok {
			return res
		}
		if x == "out" {
			if fft && dac {
				return 1
			}
			return 0
		}
		sum := 0
		for _, n := range G[x] {
			sum += f2(n, fft || n == "fft", dac || n == "dac")
		}
		cache[fmt.Sprintf("%v,%v,%v", x, fft, dac)] = sum
		return sum
	}
	p2 = f2("svr", false, false)

	fmt.Printf("Part 1: %d\n", p1)
	fmt.Printf("Part 2: %d\n", p2)

}
