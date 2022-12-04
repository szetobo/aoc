library(tidyverse)

input <- read_lines("~/proj/aoc/resources/2022/day02.txt") %>%
  strsplit(" ") %>%
  do.call(what = rbind) %>%
  apply(c(1, 2), \(x) switch(x[1], A=,X=1,B=,Y=2,C=,Z=3))

part1 <- apply(input, 1, \(r) r[2] + ((r[2] - r[1] + 1) %% 3) * 3) %>% sum
part2 <- apply(input, 1, \(r) ((((r[1] + r[2] - 2) - 1) %% 3) + 1) + (r[2] - 1) * 3) %>% sum
