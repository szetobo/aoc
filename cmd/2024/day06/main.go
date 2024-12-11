package main

import (
	"fmt"
	"os"
	"slices"

	"github.com/spakin/awk"
)

type point struct {
	row, col int
	ch       string
}

func nextPos(board [][]string, pt point) point {
	nextPt := point{}
	switch pt.ch {
	case "^":
		nextPt = point{pt.row - 1, pt.col, ""}
	case ">":
		nextPt = point{pt.row, pt.col + 1, ""}
	case "v":
		nextPt = point{pt.row + 1, pt.col, ""}
	case "<":
		nextPt = point{pt.row, pt.col - 1, ""}
	default:
		panic("invalid cursor")
	}
	if nextPt.row >= 0 && nextPt.row < len(board) && nextPt.col >= 0 && nextPt.col < len(board[0]) {
		nextPt.ch = board[nextPt.row][nextPt.col]
	}
	return nextPt
}

func turnRight(pt *point) {
	switch pt.ch {
	case "^":
		pt.ch = ">"
	case ">":
		pt.ch = "v"
	case "v":
		pt.ch = "<"
	case "<":
		pt.ch = "^"
	}
}

func run(board [][]string, pt point, markFn func(pt point) bool) bool {
	cur := point{pt.row, pt.col, pt.ch}
	res := false
	for cur.ch != "" {
		if markFn(cur) {
			res = true
			break
		}
		nxt := nextPos(board, cur)
		switch nxt.ch {
		case "#":
			turnRight(&cur)
		case ".":
			nxt.ch = cur.ch
			cur = nxt
		case "":
			cur = nxt
		}
	}
	return res
}

func main() {
	inputs := [][]string{}
	startPt := point{}

	s := awk.NewScript()

	s.Begin = func(s *awk.Script) {
		s.SetFS("")
	}

	s.AppendStmt(nil, func(s *awk.Script) {
		line := s.FStrings()
		inputs = append(inputs, line)
		if n := slices.Index(line, "^"); n != -1 {
			inputs[s.NR-1][n] = "."
			startPt = point{s.NR - 1, n, "^"}
		}
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		res1 := map[[2]int]bool{}
		run(inputs, startPt, func(pt point) bool {
			res1[[2]int{pt.row, pt.col}] = true
			return false
		})
		part1 = len(res1)

		for i, line := range inputs {
			for j, ch := range line {
				if ch == "." {
					// fmt.Println(i, j, part2)

					res2 := map[point]bool{}
					inputs[i][j] = "#"
					stucked := run(inputs, startPt, func(pt point) bool {
						if _, exists := res2[pt]; !exists {
							res2[pt] = true
							return false
						}
						return true
					})
					inputs[i][j] = "."
					if stucked {
						part2++
					}
				}
			}
		}

		// fmt.Println(inputs)
		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
