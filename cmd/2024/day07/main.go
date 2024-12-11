package main

import (
	"fmt"
	"math"
	"os"

	"github.com/spakin/awk"
)

func canAchieve(nums []int, target int, index int, current int) bool {
	if index == len(nums) {
		return current == target
	}
	return canAchieve(nums, target, index+1, current+nums[index]) ||
		canAchieve(nums, target, index+1, current*nums[index])
}

func concat(a, b int) int {
	digits := int(math.Floor(math.Log10(float64(b)))) + 1
	for i := 0; i < digits; i++ {
		a *= 10
	}
	return a + b
}

func canAchieve2(nums []int, target int, index int, current int) bool {
	if index == len(nums) {
		return current == target
	}
	return canAchieve2(nums, target, index+1, current+nums[index]) ||
		canAchieve2(nums, target, index+1, current*nums[index]) ||
		canAchieve2(nums, target, index+1, concat(current, nums[index]))
}

func main() {
	inputs := [][]int{}

	s := awk.NewScript()

	s.Begin = func(s *awk.Script) {
		s.SetFS("[ :]+")
	}

	s.AppendStmt(nil, func(s *awk.Script) {
		inputs = append(inputs, s.FInts())
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		for _, input := range inputs {
			if canAchieve(input[1:], input[0], 1, input[1]) {
				part1 += input[0]
				part2 += input[0]
			} else if canAchieve2(input[1:], input[0], 1, input[1]) {
				part2 += input[0]
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
