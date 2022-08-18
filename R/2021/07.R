input <- scan(file = '~/proj/aoc/resources/2021/day07.txt', sep = ',')

part1 <- sum(abs(input - median(input)))

m <- mean(input)
costs <- sapply(
  (floor(m)):(ceiling(m)),
  \(x) {
    n <- abs(input - x)
    sum(n * (n + 1) / 2)
  }
)

part2 <- min(costs)