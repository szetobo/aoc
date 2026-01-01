package main

import (
	"fmt"
	"io"
	"os"
	"strings"
)

type point = [2]int

func adjacent8(pt point, limit int) []point {
	row, col := pt[0], pt[1]
	offsets := []point{
		{-1, -1}, {-1, 0}, {-1, 1},
		{0, -1}, {0, 1},
		{1, -1}, {1, 0}, {1, 1},
	}
	adj := make([]point, 0)
	for _, offset := range offsets {
		nr, nc := row+offset[0], col+offset[1]
		if 0 <= nr && nr < limit && 0 <= nc && nc < limit {
			adj = append(adj, point{nr, nc})
		}
	}
	return adj
}

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

	p1, p2 := 0, 0

	for i := 0; ; i++ {
		done := p2
		for row, line := range lines {
			for col, ch := range line {
				if ch == "@" {
					cnt := 0
					for _, pt := range adjacent8(point{row, col}, len(lines)) {
						if lines[pt[0]][pt[1]] == "@" {
							cnt++
						}
					}
					if cnt < 4 {
						if i == 0 {
							p1++
						} else {
							line[col] = "x"
							p2++
						}
					}
				}
			}
		}
		if (i > 0) && (done-p2) == 0 {
			break
		}
	}
	fmt.Printf("Part 1: %d\n", p1)
	fmt.Printf("Part 2: %d\n", p2)
}
