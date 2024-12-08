package main

import (
	"fmt"
	"os"

	"github.com/spakin/awk"
)

func adjacent8(center [2]int, limit int) [][][2]int {
	row, col := center[0], center[1]
	checkPoints := [][][2]int{
		{{row - 1, col - 1}, {row - 2, col - 2}, {row - 3, col - 3}},
		{{row - 1, col}, {row - 2, col}, {row - 3, col}},
		{{row - 1, col + 1}, {row - 2, col + 2}, {row - 3, col + 3}},
		{{row, col - 1}, {row, col - 2}, {row, col - 3}},
		{{row, col + 1}, {row, col + 2}, {row, col + 3}},
		{{row + 1, col - 1}, {row + 2, col - 2}, {row + 3, col - 3}},
		{{row + 1, col}, {row + 2, col}, {row + 3, col}},
		{{row + 1, col + 1}, {row + 2, col + 2}, {row + 3, col + 3}},
	}
	ret := make([][][2]int, 0, 8)
	for _, pts := range checkPoints {
		checked := true
		for _, pt := range pts {
			if pt[0] < 0 || pt[0] >= limit || pt[1] < 0 || pt[1] >= limit {
				checked = false
				break
			}
		}
		if checked {
			ret = append(ret, pts)
		}
	}
	return ret
}

func findXmas(graph [][]string, pts [][2]int) bool {
	return graph[pts[0][0]][pts[0][1]] == "M" &&
		graph[pts[1][0]][pts[1][1]] == "A" &&
		graph[pts[2][0]][pts[2][1]] == "S"
}

func adjacent4(center [2]int, limit int) [][2]int {
	row, col := center[0], center[1]
	adjacents := [][2]int{
		{row - 1, col - 1}, {row - 1, col + 1}, {row + 1, col - 1}, {row + 1, col + 1},
	}
	for _, pt := range adjacents {
		if pt[0] < 0 || pt[0] >= limit || pt[1] < 0 || pt[1] >= limit {
			adjacents = [][2]int{}
			break
		}
	}
	return adjacents
}

func findX(graph [][]string, pts [][2]int) bool {
	return len(pts) == 4 &&
		((graph[pts[0][0]][pts[0][1]] == "M" && graph[pts[3][0]][pts[3][1]] == "S") ||
			(graph[pts[0][0]][pts[0][1]] == "S" && graph[pts[3][0]][pts[3][1]] == "M")) &&
		((graph[pts[1][0]][pts[1][1]] == "M" && graph[pts[2][0]][pts[2][1]] == "S") ||
			(graph[pts[1][0]][pts[1][1]] == "S" && graph[pts[2][0]][pts[2][1]] == "M"))
}

func main() {
	inputs := [][]string{}

	s := awk.NewScript()

	s.Begin = func(s *awk.Script) {
		s.SetFS("")
	}

	s.AppendStmt(nil, func(s *awk.Script) {
		inputs = append(inputs, s.FStrings())
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		for row := range inputs {
			for col, val := range inputs[row] {
				switch val {
				case "X":
					for _, pts := range adjacent8([2]int{row, col}, 140) {
						if findXmas(inputs, pts) {
							part1++
						}
					}
				case "A":
					pts := adjacent4([2]int{row, col}, 140)
					if findX(inputs, pts) {
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
