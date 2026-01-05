package main

import (
	"cmp"
	"fmt"
	"io"
	"maps"
	"os"
	"slices"
	"strconv"
	"strings"
)

type Point3D [3]int
type Edge struct {
	A Point3D
	B Point3D
	W int
}

type DisjointSet[T comparable] map[T]T

func main() {
	data, err := io.ReadAll(os.Stdin)
	if err != nil {
		panic(err)
	}
	lines := []Point3D{}
	for _, line := range strings.Split(string(data), "\n") {
		if len(line) == 0 {
			continue
		}
		ints := []int{}
		for _, s := range strings.Split(line, ",") {
			n, _ := strconv.Atoi(s)
			ints = append(ints, n)
		}
		lines = append(lines, Point3D{ints[0], ints[1], ints[2]})
	}
	edges := []Edge{}
	for i, a := range lines {
		for _, b := range lines[i+1:] {
			ax, ay, az := a[0], a[1], a[2]
			bx, by, bz := b[0], b[1], b[2]
			w := (ax-bx)*(ax-bx) + (ay-by)*(ay-by) + (az-bz)*(az-bz)
			edges = append(edges, Edge{a, b, w})
		}
	}
	slices.SortFunc(edges, func(a, b Edge) int { return cmp.Compare(a.W, b.W) })

	p1, p2 := 0, 0

	n := len(lines)
	dsu1 := make(DisjointSet[Point3D])
	iter := n
	if n == 20 {
		iter = 10
	}
	for i := range iter {
		dsu1.Union(edges[i].A, edges[i].B)
	}
	groups := map[Point3D]int{}
	for _, pt := range lines {
		root := dsu1.Find(pt)
		groups[root] += 1
	}
	values := slices.Collect(maps.Values(groups))
	slices.SortFunc(values, func(a, b int) int { return b - a })
	p1 = 1
	for i := range 3 {
		p1 *= values[i]
	}

	dsu2 := make(DisjointSet[Point3D])
	circuits := n
	for _, edge := range edges {
		if dsu2.Union(edge.A, edge.B) {
			circuits--
			if circuits == 1 {
				p2 = edge.A[0] * edge.B[0]
				break
			}
		}
	}

	fmt.Printf("Part 1: %d\n", p1)
	fmt.Printf("Part 2: %d\n", p2)
}

func (ds DisjointSet[T]) Find(x T) T {
	res, ok := ds[x]
	if !ok {
		ds[x] = x
	} else if res != x {
		res = ds.Find(res)
		ds[x] = res
	}
	return ds[x]
}

func (ds DisjointSet[T]) Union(a, b T) bool {
	rootI, rootJ := ds.Find(a), ds.Find(b)
	if rootI != rootJ {
		ds[rootI] = rootJ
		return true
	}
	return false
}
