package main

import (
	"fmt"
	"os"
	"strconv"

	"github.com/spakin/awk"
)

func pickK(line string, k int) int {
	n := len(line)
	result := make([]byte, 0, k)

	start := 0
	for remaining := k; remaining > 0; remaining-- {
		bestDigit := byte('0' - 1)
		bestPos := start
		for i := start; i < n-(remaining-1); i++ {
			if line[i] > bestDigit {
				bestDigit = line[i]
				bestPos = i
			}
		}
		result = append(result, bestDigit)
		start = bestPos + 1
	}

	ret, _ := strconv.Atoi(string(result))
	return ret
}

func main() {
	inputs := []string{}

	s := awk.NewScript()
	s.AppendStmt(nil, func(s *awk.Script) {
		inputs = append(inputs, s.F(0).String())
	})
	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0
		for _, x := range inputs {
			part1 += pickK(x, 2)
			part2 += pickK(x, 12)
		}

		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
