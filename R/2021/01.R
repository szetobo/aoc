input <- scan(file = '~/proj/aoc/resources/2021/day01.txt')

part1 <- sum(diff(input) > 0)
part2 <- sum(diff(rowSums(embed(input, 3))) > 0)
