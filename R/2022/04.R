library(tidyverse)

input <- read_lines("~/proj/aoc/resources/2022/day04.txt") %>%
  strsplit("[-,]") %>% lapply(as.integer) %>% do.call(what = rbind)

fully_overlapped <- \(x) {
  ((x[1] <= x[3]) & (x[4] <= x[2]) | (x[3] <= x[1]) & (x[2] <= x[4]))
}

partial_overlapped <- \(x) {
  ((x[1] <= x[3]) & (x[3] <= x[2])) | ((x[3] <= x[1]) & (x[1] <= x[4]))
}

part1 <- apply(input, 1, fully_overlapped) %>% sum
part2 <- apply(input, 1, partial_overlapped) %>% sum
