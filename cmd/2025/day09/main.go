package main

import (
	"fmt"
	"os"

	"github.com/spakin/awk"
)

type point [2]int

func minMax(a, b int) (int, int) {
	return min(a, b), max(a, b)
}

func valid(inputs [][]int, p point) bool {
	cache := make(map[point]bool)
	if res, ok := cache[p]; ok {
		return res
	}
	res := func([][]int, point) bool {
		n := len(inputs)
		cnt := 0
		for i := range n {
			a, b := inputs[i], inputs[(i+1)%n]
			x1, x2 := minMax(a[0], b[0])
			y1, y2 := minMax(a[1], b[1])
			x, y := p[0], p[1]
			if x == x1 && x == x2 && y >= y1 && y <= y2 {
				return true
			}
			if y == y1 && y == y2 && x >= x1 && x <= x2 {
				return true
			}
			if (y1 > y) != (y2 > y) {
				if x < x1+(x2-x1)*(y-y1)/(y2-y1) {
					cnt++
				}
			}
		}
		return cnt%2 == 1
	}(inputs, p)
	cache[p] = res
	return res
}

func intersect(inputs [][]int, rect [2]point) bool {
	rx1, ry1, rx2, ry2 := rect[0][0], rect[0][1], rect[1][0], rect[1][1]
	n := len(inputs)
	for i := range n {
		a, b := inputs[i], inputs[(i+1)%n]
		x1, x2 := minMax(a[0], b[0])
		y1, y2 := minMax(a[1], b[1])
		if x1 == x2 {
			if rx1 < x1 && x1 < rx2 && ry1 < y2 && y1 < ry2 {
				return true
			}
		} else {
			if ry1 < y1 && y1 < ry2 && rx1 < x2 && x1 < rx2 {
				return true
			}
		}
	}
	return false
}

func main() {
	inputs := [][]int{}

	s := awk.NewScript()
	s.Begin = func(s *awk.Script) {
		s.SetFS(",")
	}
	s.AppendStmt(nil,
		func(s *awk.Script) { inputs = append(inputs, s.FInts()) },
	)
	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		n := len(inputs)
		for i := range n {
			for j := i + 1; j < n; j++ {
				x1, x2 := minMax(inputs[i][0], inputs[j][0])
				y1, y2 := minMax(inputs[i][1], inputs[j][1])
				if x1 == x2 || y1 == y2 {
					continue
				}
				area := (x2 - x1 + 1) * (y2 - y1 + 1)
				if area > part1 {
					part1 = area
				}
				if valid(inputs, point{x1, y1}) &&
					valid(inputs, point{x1, y2}) &&
					valid(inputs, point{x2, y1}) &&
					valid(inputs, point{x2, y2}) &&
					!intersect(inputs, [2]point{{x1, y1}, {x2, y2}}) {
					if area > part2 {
						part2 = area
					}

				}
			}
		}

		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
