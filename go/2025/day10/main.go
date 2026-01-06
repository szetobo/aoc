package main

import (
	"fmt"
	"io"
	"math"
	"os"
	"slices"
	"strconv"
	"strings"
)

func main() {
	p1, p2 := 0, 0

	data, err := io.ReadAll(os.Stdin)
	if err != nil {
		panic(err)
	}
	for _, line := range strings.Split(string(data), "\n") {
		if len(line) == 0 {
			continue
		}
		parts := []string{}
		for _, p := range strings.Split(line, " ") {
			parts = append(parts, p[1:len(p)-1])
		}
		lights := []int{}
		for _, p := range strings.Split(parts[0], "") {
			if p == "#" {
				lights = append(lights, 1)
			} else {
				lights = append(lights, 0)
			}
		}
		buttons := [][]int{}
		for _, btn := range parts[1 : len(parts)-1] {
			ints := []int{}
			for _, p := range strings.Split(btn, ",") {
				n, _ := strconv.Atoi(p)
				ints = append(ints, n)
			}
			buttons = append(buttons, ints)
		}
		joltages := []int{}
		for _, p := range strings.Split(parts[len(parts)-1], ",") {
			n, _ := strconv.Atoi(p)
			joltages = append(joltages, n)
		}
		solutions := map[string][][]int{}
		for i := range 1 << len(buttons) {
			res := make([]int, len(lights))
			pressed := make([]int, len(buttons))
			for btn, bits := range buttons {
				if (i>>btn)&1 == 1 {
					for _, b := range bits {
						res[b] ^= 1
					}
					pressed[btn] = 1
				}
			}
			str := ToString(res)
			if _, ok := solutions[str]; !ok {
				solutions[str] = [][]int{}
			}
			solutions[str] = append(solutions[str], pressed)
		}
		res := math.MaxInt32
		for _, x := range solutions[ToString(lights)] {
			res = min(res, Sum(x))
		}
		p1 += res

		var fn func([]int) int
		fn = func(target []int) int {
			if Sum(target) == 0 {
				return 0
			}
			pressed := math.MaxInt32
			lights := make([]int, len(target))
			for i, joltage := range target {
				if joltage&1 == 1 {
					lights[i] = 1
				}
			}
			if s, ok := solutions[ToString(lights)]; ok {
				for _, prePressed := range s {
					newTarget := make([]int, len(target))
					copy(newTarget, target)
					for i, v := range prePressed {
						if v == 1 {
							for _, b := range buttons[i] {
								newTarget[b] -= 1
							}
						}
					}
					if slices.ContainsFunc(newTarget, func(n int) bool {
						return n < 0
					}) {
						continue
					}
					for i := range newTarget {
						newTarget[i] >>= 1
					}
					if p := fn(newTarget); p != math.MaxInt32 {
						pressed = min(pressed, Sum(prePressed)+2*p)
					}

				}
			}
			return pressed
		}
		p2 += fn(joltages)
	}

	fmt.Printf("Part 1: %d\n", p1)
	fmt.Printf("Part 2: %d\n", p2)
}

func ToString(xs []int) string {
	var sb strings.Builder
	for _, x := range xs {
		sb.WriteByte('0' + byte(x))
	}
	return sb.String()
}

func Sum(xs []int) int {
	sum := 0
	for _, v := range xs {
		sum += v
	}
	return sum
}
