package main

import (
	"fmt"
	"os"

	"github.com/spakin/awk"
)

type cell struct {
	row, col int
}

func (c *cell) adjacent8(limit int) []cell {
	checkPoints := []cell{
		{c.row - 1, c.col - 1}, {c.row - 1, c.col}, {c.row - 1, c.col + 1},
		{c.row, c.col - 1}, {c.row, c.col + 1},
		{c.row + 1, c.col - 1}, {c.row + 1, c.col}, {c.row + 1, c.col + 1},
	}
	adj := make([]cell, 0, 8)
	for _, pt := range checkPoints {
		if pt.row >= 0 && pt.row < limit && pt.col >= 0 && pt.col < limit {
			adj = append(adj, pt)
		}
	}
	return adj
}

func (c *cell) get(grid [][]byte) byte {
	return grid[c.row][c.col]
}

func (c *cell) set(grid [][]byte, v byte) {
	grid[c.row][c.col] = v
}

func main() {
	inputs := [][]byte{}

	s := awk.NewScript()
	s.Begin = func(s *awk.Script) {
		s.SetFS("")
	}
	s.AppendStmt(nil, func(s *awk.Script) {
		inputs = append(inputs, []byte(s.F(0).String()))
	})
	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		for i := 0; ; i++ {
			done := part2
			for row := range inputs {
				for col, val := range inputs[row] {
					center := cell{row, col}
					if val == '@' {
						cnt := 0
						for _, c := range center.adjacent8(len(inputs)) {
							if c.get(inputs) == '@' {
								cnt++
							}
						}
						if cnt < 4 {
							if i == 0 {
								part1++
							} else {
								center.set(inputs, 'x')
								part2++
							}
						}
					}
				}
			}
			if i > 0 && done == part2 {
				break
			}
		}

		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
