package main

import (
	"fmt"
	"os"

	"github.com/spakin/awk"
)

func countIf(f func(int) bool, s []int) int {
	count := 0
	for _, n := range s {
		if f(n) {
			count++
		}
	}
	return count
}

func isSafe(nums []int) bool {
	diffs := []int{}
	for i, num := range nums[1:] {
		diffs = append(diffs, num-nums[i])
	}
	countInc := countIf(func(i int) bool { return i > 0 }, diffs)
	countDiff := countIf(func(i int) bool { return i != 0 && i >= -3 && i <= 3 }, diffs)
	return (countInc == 0 || countInc == len(diffs)) && countDiff == len(diffs)
}

func deleteItem(s []int, i int) []int {
	ret := make([]int, 0, len(s)-1)
	ret = append(ret, s[:i]...)
	return append(ret, s[i+1:]...)
}

func main() {
	inputs := [][]int{}

	s := awk.NewScript()

	s.AppendStmt(nil, func(s *awk.Script) {
		inputs = append(inputs, s.FInts())
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		for _, nums := range inputs {
			if isSafe(nums) {
				part1++
				part2++
			} else {
				for i := range nums {
					if isSafe(deleteItem(nums, i)) {
						part2++
						break
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
