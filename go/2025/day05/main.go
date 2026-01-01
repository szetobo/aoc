package main

import (
	"cmp"
	"fmt"
	"os"
	"slices"

	"github.com/spakin/awk"
)

func main() {
	ranges := [][]int{}
	inputs := []int{}

	s := awk.NewScript()
	s.Begin = func(s *awk.Script) {
		s.SetFPat(`\d+`)
	}
	s.AppendStmt(
		func(s *awk.Script) bool { return s.NF > 0 },
		func(s *awk.Script) {
			if s.NF == 2 {
				ranges = append(ranges, s.FInts())
			} else {
				inputs = append(inputs, s.F(1).Int())
			}
		},
	)
	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		slices.SortFunc(ranges, func(a, b []int) int {
			return cmp.Or(
				cmp.Compare(a[0], b[0]),
				cmp.Compare(a[1], b[1]),
			)
		})
		last, merged := 0, ranges[:1]
		for i := 1; i < len(ranges); i++ {
			prev, cur := merged[last], ranges[i]
			if cur[0] <= prev[1] {
				if cur[1] > prev[1] {
					prev[1] = cur[1]
				}
			} else {
				merged = append(merged, cur)
				last++
			}
		}
		for _, x := range inputs {
			for _, r := range merged {
				if x >= r[0] && x <= r[1] {
					part1++
					break
				}
			}
		}
		for _, r := range merged {
			part2 += r[1] - r[0] + 1
		}

		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
