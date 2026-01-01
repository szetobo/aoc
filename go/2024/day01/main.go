package main

import (
	"fmt"
	"os"
	"sort"

	"github.com/spakin/awk"
)

func main() {
	inputs := []int{}
	frequencies := [2]map[int]int{{}, {}}

	s := awk.NewScript()

	s.AppendStmt(nil, func(s *awk.Script) {
		nums := s.FInts()
		inputs = append(inputs, nums[0])
		inputs = append(inputs, nums[1])
		frequencies[0][nums[0]]++
		frequencies[1][nums[1]]++
	})

	s.End = func(s *awk.Script) {
		state := [2][]int{}

		for i, num := range inputs {
			n := i % 2
			state[n] = append(state[n], num)
		}

		sort.Ints(state[0])
		sort.Ints(state[1])

		part1, part2 := 0, 0

		for i, num := range state[0] {
			abs := num - state[1][i]
			if abs < 0 {
				abs = -abs
			}
			part1 += abs
		}

		for key, val := range frequencies[0] {
			part2 += key * val * frequencies[1][key]
		}

		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
