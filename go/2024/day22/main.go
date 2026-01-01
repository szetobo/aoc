package main

import (
	"fmt"
	"maps"
	"os"
	"slices"

	"github.com/spakin/awk"
)

func main() {
	inputs := []int{}

	s := awk.NewScript()

	s.AppendStmt(nil, func(s *awk.Script) {
		inputs = append(inputs, s.F(1).Int())
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		res := map[[4]int]int{}
		for _, num := range inputs {
			digits := make([]int, 2001)
			digits[0] = num % 10
			for i := range 2000 {
				num = process(num)
				digits[i+1] = num % 10
			}
			part1 += num

			diffs := make([]int, 2000)
			for i := range 2000 {
				diffs[i] = digits[i+1] - digits[i]
			}
			seen := map[[4]int]bool{}
			for i := range 2000 - 4 {
				pat := [4]int{}
				copy(pat[:], diffs[i:i+4])
				if _, exists := seen[pat]; !exists {
					seen[pat] = true
					res[pat] += digits[i+4]
				}
			}
		}
		for _, v := range slices.Collect(maps.Values(res)) {
			if v > part2 {
				part2 = v
			}
		}

		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}

func process(x int) int {
	x = (x<<6 ^ x) & 0xFFFFFF
	x = (x>>5 ^ x) & 0xFFFFFF
	x = (x<<11 ^ x) & 0xFFFFFF
	return x
}
