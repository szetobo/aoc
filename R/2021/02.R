library(tidyverse)

input <- readr::read_delim("~/proj/aoc/resources/2021/day02.txt",
                           delim = ' ', col_names = c('dir', 'n'))

output <- input %>%
  dplyr::mutate(aim = cumsum(n * dplyr::case_when(dir == 'down' ~ 1,
                                                  dir == 'up'   ~ -1,
                                                  TRUE ~ 0))) %>%
  dplyr::filter(dir == 'forward') %>%
  dplyr::summarise(x = sum(n), y1 = dplyr::last(aim), y2 = sum(n * aim))

part1 <- prod(output[, c('x', 'y1')])

part2 <- prod(output[, c('x', 'y2')])
