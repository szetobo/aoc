library(tidyverse)

input <- scan(file = '~/proj/aoc/resources/2021/day08.txt',
              what = rep(list(character()), 15))

sort_string <- function(s) {
  strsplit(s, '') %>% unlist %>% sort %>% stringr::str_flatten()
}

patterns <- do.call(cbind, lapply(input[1:10], sapply, sort_string, USE.NAMES = F))
outputs <- do.call(cbind, lapply(input[12:15], sapply, sort_string, USE.NAMES = F))

part1 <- length(outputs[stringr::str_length(outputs) %in% c(2:4, 7)])

digit_freqs <- function(pattern) {
  wiring <- strsplit(pattern, split = '')
  segment_freqs <- unlist(wiring) %>% factor %>% table
  return(lapply(wiring, \(d) { stringr::str_flatten(sort(segment_freqs[d])) }) %>%  unlist)
}

standard_wiring <- c("abcefg", "cf", "acdeg","acdfg", "bcdf", "adbfg", "abdefg", "acf", "abcdefg", "abcdfg")
standard_freqs <- digit_freqs(standard_wiring)

to_num <- function(x) { sum(x * 10^((length(x) - 1):0)) }

to_digit <- function(output, patterns) {
  freqs <- digit_freqs(patterns)[which(patterns == output)]
  return(which(standard_freqs == freqs) - 1)
}

re_wire <- function(patterns, outputs) {
  sapply(1:nrow(outputs), \(i) { sapply(outputs[i,], to_digit, patterns[i,]) } %>% to_num)
}

part2 <- sum(re_wire(patterns, outputs))
