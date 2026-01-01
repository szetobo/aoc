package main

import (
	"fmt"
	"io"
	"os"
	"strconv"
	"strings"
)

func main() {
	data, err := io.ReadAll(os.Stdin)
	if err != nil {
		panic(err)
	}
	lines := strings.Split(string(data), "\n")

	p1, p2 := 0, 0
	pos := 50

	for _, line := range lines {
		if len(line) == 0 {
			continue
		}
		d := 1
		if dir := line[0]; dir == 'L' {
			d = -1
		}
		num, _ := strconv.Atoi(line[1:])
		for range num {
			pos = (pos + d) % 100
			if pos == 0 {
				p2++
			}
		}
		if pos == 0 {
			p1++
		}
	}

	fmt.Printf("Part 1: %d\n", p1)
	fmt.Printf("Part 2: %d\n", p2)
}
