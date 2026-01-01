package main

import (
	"fmt"
	"io"
	"os"
	"strconv"
	"strings"
)

func pick(s string, n int) int {
	result := make([]byte, n)
	for i, sta := n, 0; i > 0; i-- {
		p, d := sta, byte('0')
		for j := sta; j < len(s)-i+1; j++ {
			val := s[j]
			if val > d {
				p, d = j, val
			}
		}
		result[n-i] = d
		sta = p + 1
	}
	ret, _ := strconv.Atoi(string(result))
	return ret
}

func main() {
	data, err := io.ReadAll(os.Stdin)
	if err != nil {
		panic(err)
	}
	lines := strings.Split(string(data), "\n")

	p1, p2 := 0, 0

	for _, line := range lines {
		if len(line) == 0 {
			continue
		}
		p1 += pick(line, 2)
		p2 += pick(line, 12)
	}
	fmt.Printf("Part 1: %d\n", p1)
	fmt.Printf("Part 2: %d\n", p2)
}
