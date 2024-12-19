package main

import (
	"fmt"
	"os"

	"github.com/spakin/awk"
)

func main() {
	towels := []string{}
	inputs := []string{}

	s := awk.NewScript()

	s.Begin = func(s *awk.Script) {
		s.SetFS("[, ]")
	}

	s.AppendStmt(awk.Auto("[a-z]+"), func(s *awk.Script) {
		if s.NR == 1 {
			towels = s.FStrings()
		} else {
			inputs = append(inputs, s.F(1).String())
		}
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		dict := buildDict(towels)
		for _, str := range inputs {
			if ok, cnt := canMade(str, dict); ok {
				part1++
				part2 += cnt
			}
		}

		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}

func buildDict(words []string) map[string]bool {
	ret := make(map[string]bool)
	for _, word := range words {
		ret[word] = true
	}
	return ret
}

// func canMade(str string, dict map[string]bool) (bool, [][]string) {
func canMade(str string, dict map[string]bool) (bool, int) {
	n := len(str)
	dp := make([]bool, n+1)
	dp[0] = true
	// paths := make([][][]string, n+1)
	cnts := make([]int, n+1)
	cnts[0] = 1

	for i := 1; i <= n; i++ {
		for j := 0; j < i; j++ {
			if dp[j] && dict[str[j:i]] {
				dp[i] = true
				cnts[i] += cnts[j]
				// if paths[j] == nil {
				// 	paths[j] = [][]string{{}}
				// }
				// for _, p := range paths[j] {
				// 	p = append(p, str[j:i])
				// 	paths[i] = append(paths[i], p)
				// }
			}
		}
	}

	return dp[n], cnts[n]
	// return dp[n], paths[n]
}
