package main

import (
	"container/heap"
	"fmt"
	"math"
	"os"

	"github.com/spakin/awk"
)

type cell struct {
	row, col int
}

type pqItem struct {
	node     cell
	distance int
	path     []cell
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

const ROWS = 70
const COLS = 70

func edges(board [][]string, node cell) []pqItem {
	row, col := node.row, node.col
	ret := []pqItem{}
	if row > 0 && board[row-1][col] == "." {
		ret = append(ret, pqItem{node: cell{row - 1, col}, distance: 1})
	}
	if col < COLS && board[row][col+1] == "." {
		ret = append(ret, pqItem{node: cell{row, col + 1}, distance: 1})
	}
	if row < ROWS && board[row+1][col] == "." {
		ret = append(ret, pqItem{node: cell{row + 1, col}, distance: 1})
	}
	if col > 0 && board[row][col-1] == "." {
		ret = append(ret, pqItem{node: cell{row, col - 1}, distance: 1})
	}
	return ret
}

func main() {
	inputs := [][]int{}

	s := awk.NewScript()

	s.Begin = func(s *awk.Script) {
		s.SetFS(",")
	}

	s.AppendStmt(nil, func(s *awk.Script) {
		inputs = append(inputs, s.FInts())
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, ""

		board := make([][]string, ROWS+1)
		for row := 0; row < ROWS+1; row++ {
			board[row] = make([]string, COLS+1)
			for col := 0; col < COLS+1; col++ {
				board[row][col] = "."
			}
		}
		for i, nums := range inputs {
			col, row := nums[0], nums[1]
			board[row][col] = "#"
			if i >= 1023 {
				visited := map[cell]bool{}
				distances := map[cell]int{}
				results := []pqItem{}
				posE := cell{ROWS, COLS}

				pq := &queue{{cell{0, 0}, 0, []cell{{0, 0}}}}
				heap.Init(pq)

				for pq.Len() > 0 {
					curr := heap.Pop(pq).(pqItem)
					if curr.node == posE {
						results = append(results, curr)
					}
					if visited[curr.node] {
						continue
					} else {
						visited[curr.node] = true
					}
					for _, edge := range edges(board, curr.node) {
						if visited[edge.node] {
							continue
						}
						newDist := curr.distance + edge.distance
						curDist := math.MaxInt
						if _, exists := distances[edge.node]; !exists {
							distances[edge.node] = curDist
						}
						if newDist < distances[edge.node] {
							distances[edge.node] = newDist
							path := append([]cell{}, curr.path...)
							path = append(path, edge.node)
							heap.Push(pq, pqItem{edge.node, newDist, path})
						}
					}
				}
				if i == 1023 {
					part1 = distances[posE]
				}
				if distances[posE] == 0 {
					part2 = fmt.Sprintf("%v,%v", col, row)
					break
				}
			}
		}

		// for _, line := range board {
		// 	fmt.Println(line)
		// }
		// fmt.Println(inputs)
		// fmt.Println(len(inputs))
		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %s\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
