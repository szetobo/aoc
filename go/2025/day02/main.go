package main

import (
	"fmt"
	"io"
	"os"
	"strconv"
	"strings"
)

func main() {
	data, err := io.ReadAll(os.Stdin)
	if err != nil {
		panic(err)
	}
	lines := strings.Split(string(data), "\n")

	p1, p2 := 0, 0

	for _, line := range lines {
		if len(line) == 0 {
			continue
		}
		for _, p := range strings.Split(line, ",") {
			pairs := []int{}
			for _, s := range strings.Split(p, "-") {
				i, _ := strconv.Atoi(s)
				pairs = append(pairs, i)
			}
			n1, n2 := pairs[0], pairs[1]
			for i := n1; i <= n2; i++ {
				s := strconv.Itoa(i)
				n := len(s)
				// lps := buildLPS(s)
				// longest := lps[n-1]
				// period := n - longest
				// if longest > 0 && n%period == 0 {
				// 	if (longest/period)%2 == 1 {
				// 		p1 += i
				// 	}
				// 	p2 += i
				// }
				if n%2 == 0 {
					n /= 2
					if s[:n] == s[n:] {
						p1 += i
					}
				}
				ss := s + s
				if strings.Contains(ss[1:len(ss)-1], s) {
					p2 += i
				}
			}
		}

	}
	fmt.Printf("Part 1: %d\n", p1)
	fmt.Printf("Part 2: %d\n", p2)
}

// func buildLPS(s string) []int {
// 	n := len(s)
// 	lps := make([]int, n)
// 	for i, j := 1, 0; i < n; i++ {
// 		for j > 0 && s[i] != s[j] {
// 			j = lps[j-1]
// 		}
// 		if s[i] == s[j] {
// 			j++
// 		}
// 		lps[i] = j
// 	}
// 	return lps
// }
