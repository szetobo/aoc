library(tidyverse)

input <- read_file("~/proj/aoc/resources/2022/day01.txt") %>%
  strsplit("\n\n") %>% do.call(what = cbind)

calories <- strsplit(input, "\n") %>% lapply(as.integer) %>% sapply(sum)

part1 <- calories %>% max
part2 <- calories %>% sort %>% tail(3) %>% sum
