library(tidyverse)

# import -------------------------------------------------------------------

raw <- readLines('~/proj/aoc/resources/2021/day20.txt')
rules <- strsplit(raw[1], '')[[1]] == '#'
input <- scan(text = raw[c(-1, -2)], what = character(1)) %>% strsplit('') %>% sapply(`==`, '#') %>% t

iterate <- function(state, step_, rules) {
  mat <- state[[1]]
  bg <- as.numeric(state[[2]])
  row <- nrow(mat)
  col <- ncol(mat)
  mat <- cbind(bg, bg, rbind(bg, bg, mat, bg, bg), bg, bg)
  indices <-
    2^8 * mat[1:(row + 2), 1:(col + 2)] +
    2^7 * mat[1:(row + 2), 2:(col + 3)] +
    2^6 * mat[1:(row + 2), 3:(col + 4)] +
    2^5 * mat[2:(row + 3), 1:(col + 2)] +
    2^4 * mat[2:(row + 3), 2:(col + 3)] +
    2^3 * mat[2:(row + 3), 3:(col + 4)] +
    2^2 * mat[3:(row + 4), 1:(col + 2)] +
    2^1 * mat[3:(row + 4), 2:(col + 3)] +
    2^0 * mat[3:(row + 4), 3:(col + 4)]
  return(list(matrix(rules[indices + 1], nrow = row + 2, ncol = col + 2),
              rules[ifelse(bg, 512, 1)]))
}

# part 1 ------------------------------------------------------------------

state <- purrr::reduce(1:2, iterate, rules, .init = list(input, FALSE))
part1 <- sum(state[[1]])

# part 2 ------------------------------------------------------------------

state <- purrr::reduce(1:50, iterate, rules, .init = list(input, FALSE))
part2 <- sum(state[[1]])
