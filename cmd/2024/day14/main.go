package main

import (
	"fmt"
	"os"

	"github.com/spakin/awk"
)

type robot struct {
	x, y int
	v    []int
}

const width = 101
const height = 103
const seconds = 100

func (r *robot) move() {
	r.x += r.v[0]
	if r.x < 0 {
		r.x += width
	} else {
		r.x %= width
	}
	r.y += r.v[1]
	if r.y < 0 {
		r.y += height
	} else {
		r.y %= height
	}
}

func main() {
	inputs := [][]int{}

	s := awk.NewScript()

	s.Begin = func(s *awk.Script) {
		s.SetFPat("[-]?\\d+")
	}

	s.AppendStmt(nil, func(s *awk.Script) {
		inputs = append(inputs, s.FInts())
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		robots := []robot{}
		for _, nums := range inputs {
			robots = append(robots, robot{nums[0], nums[1], nums[2:4]})
		}
		for i := 1; i <= seconds; i++ {
			for n := range robots {
				robots[n].move()
			}
		}
		q1, q2, q3, q4 := 0, 0, 0, 0
		mx, my := (width-1)/2, (height-1)/2
		for _, robot := range robots {
			if robot.x == mx || robot.y == my {
				continue
			}
			if robot.x < mx {
				if robot.y < my {
					q1++
				} else {
					q3++
				}
			} else {
				if robot.y < my {
					q2++
				} else {
					q4++
				}
			}
		}
		part1 = q1 * q2 * q3 * q4

		robots = []robot{}
		for _, nums := range inputs {
			robots = append(robots, robot{nums[0], nums[1], nums[2:4]})
		}
		pts := map[[2]int]bool{}
		steps := 0
		for ; len(pts) < 500; steps++ {
			pts = map[[2]int]bool{}
			for n := range robots {
				robots[n].move()
				pts[[2]int{robots[n].x, robots[n].y}] = true
			}
		}
		part2 = steps

		// fmt.Println(inputs)
		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
