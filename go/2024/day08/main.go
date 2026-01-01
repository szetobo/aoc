package main

import (
	"fmt"
	"os"

	"github.com/spakin/awk"
	"gonum.org/v1/gonum/stat/combin"
)

type point struct {
	row, col int
}

func abs(a int) int {
	if a < 0 {
		a = -a
	}
	return a
}

func antinodes(board [][]string, a, b point) []point {
	ret := make([]point, 0, 10)
	ar, ac, br, bc := 0, 0, 0, 0
	dr, dc := abs(a.row-b.row), abs(a.col-b.col)
	if a.row < b.row {
		ar = a.row - dr
		br = b.row + dr
	} else {
		ar = a.row + dr
		br = b.row - dr
	}
	if a.col < b.col {
		ac = a.col - dc
		bc = b.col + dc
	} else {
		ac = a.col + dc
		bc = b.col - dc
	}
	if ar >= 0 && ar < len(board) && ac >= 0 && ac < len(board[0]) {
		ret = append(ret, point{ar, ac})
	}
	if br >= 0 && br < len(board) && bc >= 0 && bc < len(board[0]) {
		ret = append(ret, point{br, bc})
	}
	return ret
}

func antinodesX(board [][]string, a, b point) []point {
	ret := make([]point, 0, 10)
	dr, dc := abs(a.row-b.row), abs(a.col-b.col)
	rd, cd := a.row < b.row, a.col < b.col
	for row, col := a.row, a.col; row >= 0 && row < len(board) && col >= 0 && col < len(board[0]); {
		ret = append(ret, point{row, col})
		if rd {
			row -= dr
		} else {
			row += dr
		}
		if cd {
			col -= dc
		} else {
			col += dc
		}
	}
	for row, col := b.row, b.col; row >= 0 && row < len(board) && col >= 0 && col < len(board[0]); {
		ret = append(ret, point{row, col})
		if rd {
			row += dr
		} else {
			row -= dr
		}
		if cd {
			col += dc
		} else {
			col -= dc
		}
	}
	return ret
}

func main() {
	inputs := [][]string{}
	antennas := map[string][]point{}
	res1 := map[point]bool{}
	res2 := map[point]bool{}

	s := awk.NewScript()

	s.Begin = func(s *awk.Script) {
		s.SetFS("")
	}

	s.AppendStmt(nil, func(s *awk.Script) {
		inputs = append(inputs, s.FStrings())
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		for row, line := range inputs {
			for col, ch := range line {
				if ch != "." {
					if _, exists := antennas[ch]; !exists {
						antennas[ch] = make([]point, 0)
					}
					antennas[ch] = append(antennas[ch], point{row, col})
				}
			}
		}
		// for key, antenna := range antennas {
		for _, antenna := range antennas {
			combs := combin.Combinations(len(antenna), 2)
			for _, idx := range combs {
				// if key == "P" {
				// 	fmt.Println(key, antenna[idx[0]], antenna[idx[1]], antinodesX(inputs, antenna[idx[0]], antenna[idx[1]]))
				// }
				for _, antinode := range antinodes(inputs, antenna[idx[0]], antenna[idx[1]]) {
					res1[antinode] = true
				}
				for _, antinode := range antinodesX(inputs, antenna[idx[0]], antenna[idx[1]]) {
					res2[antinode] = true
				}
			}
		}
		part1 += len(res1)
		part2 += len(res2)

		// fmt.Println(inputs)
		// fmt.Println(antennas)
		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
