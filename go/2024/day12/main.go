package main

import (
	"container/heap"
	"fmt"
	"math"
	"os"
	"sort"

	"github.com/spakin/awk"
)

type cell struct {
	row, col int
}

type pqItem struct {
	node     cell
	distance int
}

type queue []pqItem

func (pq queue) Len() int            { return len(pq) }
func (pq queue) Less(i, j int) bool  { return pq[i].distance < pq[j].distance }
func (pq queue) Swap(i, j int)       { pq[i], pq[j] = pq[j], pq[i] }
func (pq *queue) Push(x interface{}) { *pq = append(*pq, x.(pqItem)) }
func (pq *queue) Pop() interface{} {
	old := *pq
	n := len(old)
	item := old[n-1]
	*pq = old[:n-1]
	return item
}

func buildEdge(board [][]string, node cell) []pqItem {
	row, col := node.row, node.col
	ret := []pqItem{}
	for _, edge := range []cell{{row - 1, col}, {row, col + 1}, {row + 1, col}, {row, col - 1}} {
		if edge.row >= 0 && edge.row < len(board) && edge.col >= 0 && edge.col < len(board[0]) {
			if board[edge.row][edge.col] == board[row][col] {
				ret = append(ret, pqItem{edge, 1})
			}
		}
	}
	return ret
}

func main() {
	inputs := [][]string{}

	s := awk.NewScript()

	s.Begin = func(s *awk.Script) {
		s.SetFS("")
	}

	s.AppendStmt(nil, func(s *awk.Script) {
		inputs = append(inputs, s.FStrings())
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		graph := map[cell]map[cell]int{}
		for row, line := range inputs {
			for col := range line {
				node := cell{row, col}
				if _, exists := graph[node]; !exists {
					graph[node] = map[cell]int{}
				}
				for _, edge := range buildEdge(inputs, node) {
					graph[node][edge.node] = edge.distance
				}
			}
		}

		visited := map[cell]bool{}
		plots := map[cell][]cell{}
		for row, line := range inputs {
			for col := range line {
				node := cell{row, col}

				if visited[node] {
					continue
				}

				pq := &queue{{node, 0}}
				heap.Init(pq)

				distances := map[cell]int{}
				for pq.Len() > 0 {
					curr := heap.Pop(pq).(pqItem)
					if visited[curr.node] {
						continue
					}
					visited[curr.node] = true

					for edge, dist := range graph[curr.node] {
						if visited[edge] {
							continue
						}
						newDist := curr.distance + dist
						curDist := math.MaxInt
						if _, exists := distances[edge]; !exists {
							distances[edge] = curDist
						}
						if newDist < distances[edge] {
							distances[edge] = newDist
							heap.Push(pq, pqItem{edge, newDist})
						}
					}
				}
				plots[node] = []cell{node}
				for k := range distances {
					plots[node] = append(plots[node], k)
				}
			}
		}
		for _, nodes := range plots {
			area := len(nodes)
			perimeter := 0
			for _, node := range nodes {
				for edge := range graph[node] {
					if inputs[edge.row][edge.col] != inputs[node.row][node.col] {
						perimeter++
					}
				}
				perimeter += 4 - len(graph[node])
			}
			part1 += area * perimeter

			side := 0

			// count up sides
			edges := []cell{}
			for _, node := range nodes {
				if node.row == 0 || inputs[node.row-1][node.col] != inputs[node.row][node.col] {
					edges = append(edges, node)
				}
			}
			sort.Slice(edges, func(i, j int) bool {
				if edges[i].row == edges[j].row {
					return edges[i].col < edges[j].col
				}
				return edges[i].row < edges[j].row
			})
			prev := cell{-1, 0}
			for _, node := range edges {
				if node.row != prev.row || node.col != prev.col+1 {
					side++
				}
				prev = node
			}

			// count right sides
			edges = []cell{}
			for _, node := range nodes {
				if node.col == len(inputs[0])-1 || inputs[node.row][node.col+1] != inputs[node.row][node.col] {
					edges = append(edges, node)
				}
			}
			sort.Slice(edges, func(i, j int) bool {
				if edges[i].col == edges[j].col {
					return edges[i].row < edges[j].row
				}
				return edges[i].col < edges[j].col
			})
			prev = cell{0, len(inputs[0])}
			for _, node := range edges {
				if node.col != prev.col || node.row != prev.row+1 {
					side++
				}
				prev = node
			}

			// count down sides
			edges = []cell{}
			for _, node := range nodes {
				if node.row == len(inputs)-1 || inputs[node.row+1][node.col] != inputs[node.row][node.col] {
					edges = append(edges, node)
				}
			}
			sort.Slice(edges, func(i, j int) bool {
				if edges[i].row == edges[j].row {
					return edges[i].col < edges[j].col
				}
				return edges[i].row < edges[j].row
			})
			prev = cell{len(inputs), 0}
			for _, node := range edges {
				if node.row != prev.row || node.col != prev.col+1 {
					side++
				}
				prev = node
			}

			// count left sides
			edges = []cell{}
			for _, node := range nodes {
				if node.col == 0 || inputs[node.row][node.col-1] != inputs[node.row][node.col] {
					edges = append(edges, node)
				}
			}
			sort.Slice(edges, func(i, j int) bool {
				if edges[i].col == edges[j].col {
					return edges[i].row < edges[j].row
				}
				return edges[i].col < edges[j].col
			})
			prev = cell{0, -1}
			for _, node := range edges {
				if node.col != prev.col || node.row != prev.row+1 {
					side++
				}
				prev = node
			}
			part2 += area * side
		}

		// fmt.Println(inputs)
		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
