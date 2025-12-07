package main

import (
	"fmt"
	"os"

	"github.com/spakin/awk"
)

type cell [2]int

func main() {
	inputs := [][]byte{}
	ways := make(map[cell]int)

	s := awk.NewScript()
	s.Begin = func(s *awk.Script) {
		s.SetFS("")
	}
	s.AppendStmt(nil,
		func(s *awk.Script) { inputs = append(inputs, []byte(s.F(0).String())) },
	)
	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		lastRow := len(inputs) - 1
		for row := range inputs[:lastRow] {
			for col, val := range inputs[row] {
				if val == '|' || val == 'S' {
					cnt, ok := ways[cell{row, col}]
					if !ok {
						cnt = 1
					}
					val = inputs[row+1][col]
					if val == '^' {
						inputs[row+1][col-1] = '|'
						ways[cell{row + 1, col - 1}] += cnt
						inputs[row+1][col+1] = '|'
						ways[cell{row + 1, col + 1}] += cnt
						part1++
					}
					if val == '.' || val == '|' {
						inputs[row+1][col] = '|'
						ways[cell{row + 1, col}] += cnt
					}
				}
			}
		}
		for col, val := range inputs[lastRow] {
			if val == '|' {
				part2 += ways[cell{lastRow, col}]
			}
		}

		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
