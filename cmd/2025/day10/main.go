package main

import (
	"cmp"
	"fmt"
	"math"
	"math/bits"
	"os"
	"slices"
	"strconv"
	"strings"

	"github.com/spakin/awk"
)

func main() {
	part1, part2 := 0, 0

	s := awk.NewScript()
	s.AppendStmt(
		nil,
		// func(s *awk.Script) bool { return s.NR == 1 },
		func(s *awk.Script) {
			ins := s.FStrings()
			N := len(ins)
			light := 0
			for i, ch := range ins[0][1 : len(ins[0])-1] {
				if ch == '#' {
					light += 1 << i
				}
			}
			// fmt.Println(light)
			joltages := []int{}
			for _, s := range strings.Split(ins[N-1][1:len(ins[N-1])-1], ",") {
				n, _ := strconv.Atoi(s)
				joltages = append(joltages, n)
			}
			// fmt.Println(joltages)
			buttons := []int{}
			BJ := [][]int{}
			for _, b := range ins[1 : len(ins)-1] {
				v := 0
				bj := make([]int, len(joltages))
				for _, s := range strings.Split(b[1:len(b)-1], ",") {
					n, _ := strconv.Atoi(s)
					v ^= 1 << n
					bj[n]++
				}
				buttons = append(buttons, v)
				BJ = append(BJ, bj)
			}
			// fmt.Println(buttons)
			// fmt.Println(BJ)

			// f1 := func(buttons []int, light int) int {
			// 	for i := 1; ; i++ {
			// 		for _, b := range CombinationsBitmask(len(buttons), i) {
			// 			v := 0
			// 			for _, j := range b {
			// 				v ^= buttons[j]
			// 			}
			// 			if v == light {
			// 				return i
			// 			}
			// 		}
			// 	}
			// }
			// part1 += f1(buttons, light)

			var cacheF1 = map[int][][]int{}
			f1 := func(buttons []int, light int) [][]int {
				ret := [][]int{}
				if ret, ok := cacheF1[light]; ok {
					return ret
				}
				for i := range 1<<len(buttons) + 1 {
					res, pressed := 0, []int{}
					for btn := range len(buttons) {
						if i>>btn&1 == 1 {
							res ^= buttons[btn]
							pressed = append(pressed, btn)
						}
					}
					if res == light {
						ret = append(ret, pressed)
					}
				}
				return ret
			}
			part1 += len(slices.MinFunc(f1(buttons, light), func(a, b []int) int { return cmp.Compare(len(a), len(b)) }))

			var cacheF2 = map[string]int{}
			var f2 func([]int, []int, []int) int
			f2 = func(buttons []int, joltages []int, target []int) int {
				if ret, ok := cacheF2[fmt.Sprintf("%v", joltages)]; ok {
					return ret
				}
				if slices.Equal(joltages, target) {
					return 0
				}
				pressed := []int{math.MaxInt}
				light := 0
				for i, j := range joltages {
					if j&1 == 1 {
						light += 1 << i
					}
				}
				for _, prePressed := range f1(buttons, light) {
					newJoltages := make([]int, len(joltages))
					copy(newJoltages, joltages)
					for _, btn := range prePressed {
						for i, j := range BJ[btn] {
							newJoltages[i] -= j
						}
					}
					valid := true
					for i := range newJoltages {
						newJoltages[i] >>= 1
						if newJoltages[i] < 0 {
							valid = false
							break
						}
					}
					if valid {
						if p := f2(buttons, newJoltages, target); p != math.MaxInt {
							pressed = append(pressed, len(prePressed)+2*p)
						}
					}
				}
				ret := slices.Min(pressed)
				cacheF2[fmt.Sprintf("%v", joltages)] = ret
				return ret
			}
			part2 += f2(buttons, joltages, make([]int, len(joltages)))
		},
	)
	s.End = func(s *awk.Script) {
		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}

func CombinationsBitmask(n, r int) [][]int {
	if r < 0 || r > n {
		return nil
	}
	var ret [][]int
	for mask := range 1 << n {
		if bits.OnesCount(uint(mask)) == r {
			var combo []int
			for i := range n {
				if (mask>>i)&1 == 1 {
					combo = append(combo, i)
				}
			}
			ret = append(ret, combo)
		}
	}
	return ret
}

func GenerateCombinationsBitmask[T any](items []T, n int) [][]T {
	N := len(items)
	if n < 0 || n > N {
		return nil
	}
	var ret [][]T
	for mask := range 1 << N {
		if bits.OnesCount(uint(mask)) == n {
			var combo []T
			for i := range N {
				if (mask>>i)&1 == 1 {
					combo = append(combo, items[i])
				}
			}
			ret = append(ret, combo)
		}
	}
	return ret
}

func GenerateCombinations[T any](items []T, n int) [][]T {
	if n < 0 {
		return nil
	}
	var ret [][]T
	var recurse func(startIndex int, combo []T)

	// 'startIndex' controls which element we choose next, preventing duplicate orderings.
	recurse = func(startIndex int, combo []T) {
		if len(combo) == n {
			x := make([]T, n)
			copy(x, combo)
			ret = append(ret, x)
			return
		}

		for i := startIndex; i < len(items); i++ {
			recurse(i, append(combo, items[i]))
		}
	}
	recurse(0, []T{})
	return ret
}
