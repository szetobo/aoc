package main

import (
	"fmt"
	"io"
	"os"
	"strconv"
	"strings"
)

type Point [2]int

func main() {
	data, err := io.ReadAll(os.Stdin)
	if err != nil {
		panic(err)
	}
	lines := []Point{}
	for _, line := range strings.Split(string(data), "\n") {
		if len(line) == 0 {
			continue
		}
		ints := []int{}
		for _, s := range strings.Split(line, ",") {
			n, _ := strconv.Atoi(s)
			ints = append(ints, n)
		}
		lines = append(lines, Point{ints[0], ints[1]})
	}

	p1, p2 := 0, 0

	for i, a := range lines[:len(lines)-2] {
		for _, b := range lines[i+1:] {
			x1, x2 := minMax(a[0], b[0])
			y1, y2 := minMax(a[1], b[1])
			if x1 == x2 || y1 == y2 {
				continue
			}
			area := (x2 - x1 + 1) * (y2 - y1 + 1)
			p1 = max(p1, area)

			if area <= p2 {
				continue
			}
			if contains(lines, Point{x1, y1}) &&
				contains(lines, Point{x1, y2}) &&
				contains(lines, Point{x2, y1}) &&
				contains(lines, Point{x2, y2}) &&
				!intersect(lines, Point{x1, y1}, Point{x2, y2}) {
				p2 = max(p2, area)
			}

		}
	}

	fmt.Printf("Part 1: %d\n", p1)
	fmt.Printf("Part 2: %d\n", p2)
}

func minMax(a, b int) (int, int) {
	return min(a, b), max(a, b)
}

func contains(pts []Point, p Point) bool {
	cache := make(map[Point]bool)
	if res, ok := cache[p]; ok {
		return res
	}
	res := func([]Point, Point) bool {
		n := len(pts)
		cnt := 0
		for i := range n {
			a, b := pts[i], pts[(i+1)%n]
			x1, x2 := minMax(a[0], b[0])
			y1, y2 := minMax(a[1], b[1])
			x, y := p[0], p[1]
			if x == x1 && x == x2 && y >= y1 && y <= y2 {
				return true
			}
			if y == y1 && y == y2 && x >= x1 && x <= x2 {
				return true
			}
			if (y1 > y) != (y2 > y) {
				if x < x1+(x2-x1)*(y-y1)/(y2-y1) {
					cnt++
				}
			}
		}
		return cnt%2 == 1
	}(pts, p)
	cache[p] = res
	return res
}

func intersect(pts []Point, a, b Point) bool {
	ax, ay, bx, by := a[0], a[1], b[0], b[1]
	n := len(pts)
	for i := range n {
		c, d := pts[i], pts[(i+1)%n]
		x1, x2 := minMax(c[0], d[0])
		y1, y2 := minMax(c[1], d[1])
		if x1 == x2 {
			if ax < x1 && x1 < bx && ay < y2 && y1 < by {
				return true
			}
		} else {
			if ay < y1 && y1 < by && ax < x2 && x1 < bx {
				return true
			}
		}
	}
	return false
}
