calls <- scan(file = '~/proj/aoc/resources/2021/day04.txt', sep = ',', nlines = 1)
cells <- scan(file = '~/proj/aoc/resources/2021/day04.txt', integer(), skip = 2)
cards <- array(cells, dim = c(5, 5, length(cells) / 25))

call_idx <- array(match(cards, calls), dim = dim(cards))

bingo_call_idx <- pmin(apply(call_idx, c(1, 3), max) |> apply(2, min),
                       apply(call_idx, c(2, 3), max) |> apply(2, min))

winning_card <- which.min(bingo_call_idx)
winning_call_idx <- min(bingo_call_idx)

part1 <-
  calls[winning_call_idx] *
  sum(setdiff(cards[, , winning_card], calls[1:winning_call_idx]))

losing_card <- which.max(bingo_call_idx)
losing_call_idx <- max(bingo_call_idx)

part2 <-
  calls[losing_call_idx] *
  sum(setdiff(cards[, , losing_card], calls[1:losing_call_idx]))
