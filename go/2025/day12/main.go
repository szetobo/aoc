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
	p1, p2 := 0, 0

	data, err := io.ReadAll(os.Stdin)
	if err != nil {
		panic(err)
	}
	S := [][][]string{}
	T := []int{}
	for _, line := range strings.Split(string(data), "\n") {
		if len(line) == 0 {
			continue
		}
		if len(line) == 2 {
			S = append(S, [][]string{})
			T = append(T, 0)
		} else if regexp.MustCompile(`[#.]+`).MatchString(line) {
			S[len(S)-1] = append(S[len(S)-1], strings.Split(line, ""))
			for _, v := range strings.Split(line, "") {
				if v == "#" {
					T[len(T)-1] += 1
				}
			}
		} else {
			parts := regexp.MustCompile(`\d+`).FindAllString(line, -1)
			ints := []int{}
			for _, v := range parts {
				n, _ := strconv.Atoi(v)
				ints = append(ints, n)
			}
			width, height, rest := ints[0], ints[1], ints[2:]
			ttlT := width * height
			minP := (width / 3) * (height / 3)
			cntP, cntT := 0, 0
			for i, v := range rest {
				cntP += v
				cntT += v * T[i]
			}
			if (cntP <= minP) && (cntT <= ttlT) {
				p1++
			}
		}
	}

	fmt.Printf("Part 1: %d\n", p1)
	fmt.Printf("Part 2: %d\n", p2)
}
