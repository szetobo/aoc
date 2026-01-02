package main

import (
	"cmp"
	"fmt"
	"io"
	"os"
	"slices"
	"strconv"
	"strings"
)

type Range = [2]int

func main() {
	data, err := io.ReadAll(os.Stdin)
	if err != nil {
		panic(err)
	}
	ranges := []Range{}
	lines := []int{}
	parts := strings.Split(string(data), "\n\n")
	for _, line := range strings.Split(parts[0], "\n") {
		r := Range{}
		for i, str := range strings.Split(line, "-") {
			r[i], _ = strconv.Atoi(str)
		}
		ranges = append(ranges, r)
	}
	for _, line := range strings.Split(parts[1], "\n") {
		n, _ := strconv.Atoi(line)
		lines = append(lines, n)
	}

	p1, p2 := 0, 0

	for _, n := range lines {
		for _, r := range ranges {
			s, e := r[0], r[1]
			if s <= n && n <= e {
				p1++
				break
			}
		}
	}

	slices.SortFunc(ranges, func(a, b [2]int) int {
		return cmp.Or(cmp.Compare(a[0], b[0]), cmp.Compare(a[1], b[1]))
	})

	lst := 0
	for _, r := range ranges {
		s, e := r[0], r[1]
		if e > lst {
			p2 += e - max(s, lst+1) + 1
		}
		lst = max(lst, e)
	}

	fmt.Printf("Part 1: %d\n", p1)
	fmt.Printf("Part 2: %d\n", p2)
}
