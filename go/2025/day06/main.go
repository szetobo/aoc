package main

import (
	"fmt"
	"io"
	"os"
	"regexp"
	"strconv"
	"strings"
)

func main() {
	data, err := io.ReadAll(os.Stdin)
	if err != nil {
		panic(err)
	}
	lines := []string{}
	for _, line := range strings.Split(string(data), "\n") {
		if len(line) == 0 {
			continue
		}
		lines = append(lines, line)
	}

	p1, p2 := 0, 0

	N1 := [][]string{}
	for _, line := range lines {
		N1 = append(N1, regexp.MustCompile(`\s+`).Split(line, -1))
	}
	for _, col := range transpose(N1) {
		ints := []int{}
		for _, s := range col[0 : len(col)-1] {
			n, _ := strconv.Atoi(s)
			ints = append(ints, n)
		}
		op := col[len(col)-1]
		R := 0
		if op == "+" {
			for _, n := range ints {
				R += n
			}
		} else {
			R = 1
			for _, n := range ints {
				R *= n
			}
		}
		p1 += R
	}
	N2 := [][]string{}
	for _, line := range lines {
		N2 = append(N2, strings.Split(line, ""))
	}
	N2 = transpose(N2)
	fs, OP, R := true, "", 0
	for i, col := range N2 {
		num, _ := strconv.Atoi(strings.TrimSpace(strings.Join(col[:len(col)-1], "")))
		op := col[len(col)-1]
		if fs && op != " " {
			OP = op
			R = 1
			if OP == "+" {
				R = 0
			}
			fs = false
		}
		if num == 0 {
			fs = true
		} else {
			if OP == "+" {
				R += num
			} else {
				R *= num
			}
		}
		if fs || (i == len(N2)-1) {
			p2 += R
		}
	}
	fmt.Printf("Part 1: %d\n", p1)
	fmt.Printf("Part 2: %d\n", p2)
}

func transpose[T any](m [][]T) [][]T {
	res := make([][]T, len(m[0]))
	for i := range res {
		res[i] = make([]T, len(m))
		for j := range m {
			res[i][j] = m[j][i]
		}
	}
	return res
}
