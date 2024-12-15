package main

import (
	"fmt"
	"math"
	"os"

	"github.com/spakin/awk"
)

func main() {
	inputs := []int{}

	s := awk.NewScript()

	s.AppendStmt(awk.Auto(1), func(s *awk.Script) {
		inputs = s.FInts()
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		stones := make([]int, len(inputs))
		copy(stones, inputs)

		blinks := 25
		for ; blinks > 0; blinks-- {
			res := []int{}
			for _, val := range stones {
				if val == 0 {
					res = append(res, 1)
				} else {
					digits := int(math.Floor(math.Log10(float64(val)))) + 1
					if digits%2 == 0 {
						factor := 1
						for k := 0; k < digits/2; k++ {
							factor *= 10
						}
						left := val / factor
						res = append(res, left)
						res = append(res, val-(left*factor))
					} else {
						res = append(res, val*2024)
					}
				}
			}
			stones = res
		}
		part1 = len(stones)

		results := map[int]int{}
		caches := map[int][]int{}
		for _, v := range inputs {
			results[v]++
		}

		blinks = 75
		for ; blinks > 0; blinks-- {
			stones := map[int]int{}
			for val, cnt := range results {
				if _, exists := caches[val]; !exists {
					res := []int{}
					if val == 0 {
						res = append(res, 1)
					} else {
						digits := int(math.Floor(math.Log10(float64(val)))) + 1
						if digits%2 == 0 {
							factor := 1
							for k := 0; k < digits/2; k++ {
								factor *= 10
							}
							left := val / factor
							res = append(res, left)
							res = append(res, val-(left*factor))
						} else {
							res = append(res, val*2024)
						}
					}
					caches[val] = res
				}
				for _, v := range caches[val] {
					stones[v] += cnt
				}
			}
			results = stones
		}
		for _, v := range results {
			part2 += v
		}

		// fmt.Println(inputs)
		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
