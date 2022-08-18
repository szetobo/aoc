library(bit64)

input <- scan(file = '~/proj/aoc/resources/2021/day06.txt', sep = ',')

reset_pos <- (1:9 + 6) %% 9 + 1

iterate <- function(freqs, days) {
  for (i in (1:days - 1) %% 9 + 1) {
    n <- reset_pos[i]
    freqs[n] <- freqs[n] + freqs[i]
  }
  return(freqs)
}

init_freqs <- integer64(9)
init_freqs[1:9] <- factor(input, 0:8) |> table() |> as.vector()

part1 <- sum(iterate(init_freqs, 80))
part2 <- sum(iterate(init_freqs, 256))
