package main

import (
	"fmt"
	"io"
	"os"
	"strings"
)

type cell [2]int

func main() {
	data, err := io.ReadAll(os.Stdin)
	if err != nil {
		panic(err)
	}
	lines := [][]string{}
	for _, line := range strings.Split(string(data), "\n") {
		if len(line) == 0 {
			continue
		}
		lines = append(lines, strings.Split(line, ""))
	}
	ways := map[cell]int{}

	p1, p2 := 0, 0

	n := len(lines)
	for row, line := range lines[:n-1] {
		for col, ch := range line {
			if ch == "|" || ch == "S" {
				cnt, ok := ways[cell{row, col}]
				if !ok {
					cnt = 1
				}
				nextRow := lines[row+1]
				char := nextRow[col]
				if char == "^" {
					nextRow[col-1] = "|"
					ways[cell{row + 1, col - 1}] += cnt
					nextRow[col+1] = "|"
					ways[cell{row + 1, col + 1}] += cnt
					p1++
				}
				if char == "." || char == "|" {
					nextRow[col] = "|"
					ways[cell{row + 1, col}] += cnt
				}
			}
		}
	}
	for col, ch := range lines[n-1] {
		if ch == "|" {
			p2 += ways[cell{n - 1, col}]
		}
	}

	fmt.Printf("Part 1: %d\n", p1)
	fmt.Printf("Part 2: %d\n", p2)
}
