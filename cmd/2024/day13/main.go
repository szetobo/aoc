package main

import (
	"fmt"
	"math"
	"os"

	"github.com/spakin/awk"
)

func subsetSum(nums []int, target int) bool {
	dp := make([]bool, target+1)
	dp[0] = true

	for _, num := range nums {
		for sum := num; sum <= target; sum++ {
			dp[sum] = dp[sum] || dp[sum-num]
		}
		// fmt.Println(dp)
	}

	return dp[target]
}

func subsetSumX(nums []int, target int) [][]int {
	dp := make(map[int][][]int)
	dp[0] = [][]int{{}}

	for _, num := range nums {
		for sum := num; sum <= target; sum++ {
			if subsets, exists := dp[sum-num]; exists {
				for _, subset := range subsets {
					newSubset := append([]int{}, subset...)
					newSubset = append(newSubset, num)
					dp[sum] = append(dp[sum], newSubset)
				}
			}
		}
		// fmt.Println(dp)
	}

	return dp[target]
}

func subsetSum2DX(nums [][2]int, target [2]int) [][][2]int {
	dp := make(map[[2]int][][][2]int)
	dp[[2]int{0, 0}] = [][][2]int{{}}

	for _, pair := range nums {
		for sum := pair; sum[0] <= target[0] && sum[1] <= target[1]; {
			if subsets, exists := dp[[2]int{sum[0] - pair[0], sum[1] - pair[1]}]; exists {
				for _, subset := range subsets {
					newSubset := append([][2]int{}, subset...)
					newSubset = append(newSubset, pair)
					dp[sum] = append(dp[sum], newSubset)
				}
			}
			sum[1]++
			if sum[1] > target[1] {
				sum[1] = pair[1]
				sum[0]++
			}
		}
		// fmt.Println(dp)
	}

	return dp[target]
}

func subsetSumN(nums []int, target int) bool {
	n := len(nums)

	dp := make([][]bool, n+1)
	for i := range dp {
		dp[i] = make([]bool, target+1)
		dp[i][0] = true
	}

	for i := 1; i <= n; i++ {
		for j := 1; j <= target; j++ {
			dp[i][j] = dp[i-1][j]
			if j >= nums[i-1] {
				dp[i][j] = dp[i][j] || dp[i][j-nums[i-1]]
			}
		}
		// for idx, line := range dp {
		// 	if idx == 0 {
		// 		fmt.Println("-", line)
		// 	} else {
		// 		fmt.Println(nums[idx-1], line)
		// 	}
		// }
	}

	return dp[n][target]
}

func subsetSumNX(nums []int, target int) [][]int {
	n := len(nums)

	dp := make([][][][]int, n+1)
	for i := range dp {
		dp[i] = make([][][]int, target+1)
		dp[i][0] = [][]int{{}}
	}

	for i := 1; i <= n; i++ {
		for j := 1; j <= target; j++ {
			dp[i][j] = append(dp[i][j], dp[i-1][j]...)
			if j >= nums[i-1] {
				for _, subset := range dp[i][j-nums[i-1]] {
					newSubset := append([]int{}, subset...)
					newSubset = append(newSubset, nums[i-1])
					dp[i][j] = append(dp[i][j], newSubset)
				}
			}
		}
		// for idx, line := range dp {
		// 	if idx == 0 {
		// 		fmt.Println("-", line)
		// 	} else {
		// 		fmt.Println(nums[idx-1], line)
		// 	}
		// }
	}

	return dp[n][target]
}

func max(a, b int) int {
	ret := a
	if b > a {
		ret = b
	}
	return ret
}

func main() {
	inputs := [][]int{}

	s := awk.NewScript()

	s.Begin = func(s *awk.Script) {
		s.SetFPat("\\d+")
		s.SetRS("")
	}

	s.AppendStmt(nil, func(s *awk.Script) {
		inputs = append(inputs, s.FInts())
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		// fmt.Println(subsetSum([]int{3, 34, 4, 12, 5, 2}, 9))
		// fmt.Println(subsetSumX([]int{3, 34, 4, 12, 5, 2}, 9))
		// fmt.Println(subsetSumNX([]int{3, 34, 4, 12, 5, 2}, 9))
		// fmt.Println(subsetSum2DX([][2]int{{1, 2}, {3, 4}, {5, 6}}, [2]int{6, 8}))
		// x := subsetSum2DX([][2]int{{42, 68}, {68, 31}}, [2]int{3126, 2451})
		// x := subsetSum2DX([][2]int{{94, 34}, {22, 67}}, [2]int{8400, 5400})
		// x := subsetSum2DX([][2]int{{17, 86}, {84, 37}}, [2]int{7870, 6450})
		// c1, c2 := 0, 0
		// for _, subsets := range x {
		// 	for _, subset := range subsets {
		// 		if subset == [2]int{17, 86} {
		// 			c1++
		// 		} else {
		// 			c2++
		// 		}
		// 	}
		// }
		// fmt.Println(len(x), len(x[0]), c1, c2)

		// inputs = [][]int{
		// 	{94, 34, 22, 67, 8400, 5400},
		// 	{26, 66, 67, 21, 12748, 12176},
		// 	{17, 86, 84, 37, 7870, 6450},
		// 	{69, 23, 27, 71, 18641, 10279},
		// }
		res := [][]int{}
		for _, nums := range inputs {
			// mt := []int{0, 0}
			ax, ay := float64(nums[0]), float64(nums[1])
			bx, by := float64(nums[2]), float64(nums[3])
			px, py := float64(nums[4]), float64(nums[5])

			i := (px*by/bx - py) / (ax*by/bx - ay)
			j := (px - ax*i) / bx
			x, y := int(math.Round(i)), int(math.Round(j))
			if i > 0 && j > 0 && x*nums[0]+y*nums[2] == nums[4] && x*nums[1]+y*nums[3] == nums[5] {
				res = append(res, []int{x, y})
			}
		}
		// fmt.Println(res)
		for _, v := range res {
			part1 += v[0]*3 + v[1]
		}

		res = [][]int{}
		for _, nums := range inputs {
			nums[4] += 10000000000000
			nums[5] += 10000000000000
			ax, ay := float64(nums[0]), float64(nums[1])
			bx, by := float64(nums[2]), float64(nums[3])
			px, py := float64(nums[4]), float64(nums[5])

			i := (px*by/bx - py) / (ax*by/bx - ay)
			j := (px - ax*i) / bx
			x, y := int(math.Round(i)), int(math.Round(j))
			if i > 0 && j > 0 && x*nums[0]+y*nums[2] == nums[4] && x*nums[1]+y*nums[3] == nums[5] {
				res = append(res, []int{x, y})
			}
		}
		// fmt.Println(res)
		for _, v := range res {
			part2 += v[0]*3 + v[1]
		}

		// fmt.Println(inputs)
		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
