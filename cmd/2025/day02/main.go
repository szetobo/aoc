package main

import (
	"fmt"
	"strconv"
	// "math"
	"os"

	"github.com/spakin/awk"
)

func BuildLPS(s string) []int {
	n := len(s)
	lps := make([]int, n)

	length := 0 // length of current longest prefix-suffix
	i := 1

	for i < n {
		if s[i] == s[length] {
			length++
			lps[i] = length
			i++
		} else {
			if length != 0 {
				length = lps[length-1] // fallback
			} else {
				lps[i] = 0
				i++
			}
		}
	}
	return lps
}

func main() {
	inputs := [][]int{}

	s := awk.NewScript()
	s.Begin = func(s *awk.Script) {
		s.SetFPat(`\d+`)
		s.SetRS(",")
	}
	s.AppendStmt(nil, func(s *awk.Script) {
		inputs = append(inputs, s.FInts())
	})
	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0
		for _, x := range inputs {
			for i := x[0]; i <= x[1]; i++ {
				str := strconv.Itoa(i)
				n := len(str)
				lps := BuildLPS(str)
				longest := lps[n-1]
				period := n - longest
				if longest > 0 && n%period == 0 {
					if (longest/period)%2 == 1 {
						part1 += i
					}
					part2 += i
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
