package main

import (
	"cmp"
	"fmt"
	"os"
	"slices"

	"github.com/spakin/awk"
)

type DisjointSet []int

func NewDisjointSet(n int) DisjointSet {
	x := make([]int, n)
	ds := DisjointSet(x)
	for i := range n {
		ds[i] = i
	}
	return ds
}

func (ds DisjointSet) Find(i int) int {
	if ds[i] == i {
		return i
	}
	ds[i] = ds.Find(ds[i]) // path compression
	return ds[i]
}

func (ds DisjointSet) Union(i, j int) {
	rootI := ds.Find(i)
	rootJ := ds.Find(j)

	if rootI != rootJ {
		ds[rootI] = rootJ
	}
}

func main() {
	inputs := [][]int{}

	s := awk.NewScript()
	s.Begin = func(s *awk.Script) {
		s.SetFS(",")
	}
	s.AppendStmt(nil,
		func(s *awk.Script) { inputs = append(inputs, s.FInts()) },
	)
	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		n := len(inputs)
		edges := make([][]int, 0)
		for i := range n {
			for j := i + 1; j < n; j++ {
				edges = append(edges, []int{i, j})
			}
		}
		dist := func(a, b []int) int {
			sum := 0
			for i := range 3 {
				diff := a[i] - b[i]
				sum += diff * diff
			}
			return sum
		}
		slices.SortFunc(edges, func(a, b []int) int {
			return cmp.Compare(dist(inputs[a[0]], inputs[a[1]]), dist(inputs[b[0]], inputs[b[1]]))
		})

		ds := NewDisjointSet(n)
		iter := 1000
		if n == 20 {
			iter = 10
		}
		for _, e := range edges[:iter] {
			ds.Union(e[0], e[1])
		}
		groups := make(map[int]int)
		for i := range len(ds) {
			groups[ds.Find(i)]++
		}
		size := make([]int, 0, len(groups))
		for _, v := range groups {
			size = append(size, v)
		}
		slices.SortFunc(size, func(a, b int) int { return cmp.Compare(b, a) })
		part1 = size[0] * size[1] * size[2]

		circuits := n
		ds = NewDisjointSet(n)
		for _, e := range edges {
			if ds.Find(e[0]) == ds.Find(e[1]) {
				continue
			}
			ds.Union(e[0], e[1])
			circuits--
			if circuits == 1 {
				part2 = inputs[e[0]][0] * inputs[e[1]][0]
				break
			}
		}

		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
