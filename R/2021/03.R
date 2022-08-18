input <- readLines("~/proj/aoc/resources/2021/day03.txt")

bits <- strsplit(input, "") |> lapply(as.integer) |>  do.call(what = rbind)

bin2num <- function(x) { sum(x * 2^((length(x) - 1):0)) }

nr <- nrow(bits)
gamma <- (colSums(bits) >= nr / 2) |>  bin2num()
epsilon <- (colSums(bits) < nr / 2) |>  bin2num()

part1 <- gamma * epsilon

rating <- function(op, table, index = 1) {
  nr = nrow(table)
  if (nr == 1)
    table
  else {
    col <- table[, index]
    rating(op, table[col == op(sum(col), nr / 2), ,drop = FALSE], index + 1)
  }
}

o2 <- rating(`>=`, bits) |> bin2num()
co2 <- rating(`<`, bits) |> bin2num()
part2 <- o2 * co2
