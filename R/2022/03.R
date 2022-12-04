library(tidyverse)

input <- read_lines("~/proj/aoc/resources/2022/day03.txt") %>%  strsplit("")

priority <- \(x) { which(c(letters, LETTERS) == x) }

part1 <- sapply(input, \(x) {
  M <- matrix(x, ncol = 2)
  M[M[,1] %in% M[,2],1] %>% first %>% priority
}) %>% sum

part2 <- sapply(split(input, ceiling(seq_along(input) / 3)), \(x) {
  badges <-x[[1]] %in% x[[2]] & x[[1]] %in% x[[3]]
  x[[1]][badges] %>% first %>% priority
}) %>% sum
