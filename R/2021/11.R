library(tidyverse)

input <- readLines('~/proj/aoc/resources/2021/day11.txt') %>%
  strsplit("") %>% lapply(as.integer) %>% do.call(what = rbind)

adjacent_8 <- function(pt, mat) {
  x <- pt[1]
  y <- pt[2]
  z <- expand.grid(row = x + (-1:1), col = y + (-1:1))
  z <- z[!(z[, 1] == x & z[, 2] == y), ]
  z <- z[z[, 1] > 0 & z[, 1] <= nrow(mat) & z[, 2] > 0 & z[, 2] <= ncol(mat), ]
  return(z)
}

iterate <- function(res, steps_) {
  flashed <- res[[1]]
  mat <- res[[2]] + 1
  repeat {
    flashes <- which(mat > 9, arr.ind = TRUE)
    if (nrow(flashes) == 0) break

    mat[mat > 9] <- -Inf
    flashed <- flashed + nrow(flashes)

    nbrs <- apply(flashes, 1, adjacent_8, mat) %>% do.call(what = rbind)
    for (i in seq_len(nrow(nbrs)))
      mat[nbrs[[i, 1]], nbrs[[i, 2]]] <-  mat[nbrs[[i, 1]], nbrs[[i, 2]]] + 1
  }
  mat[mat == -Inf] <- 0
  return(list(flashed, mat))
}

part1 <- Reduce(iterate, 1:100, list(0, input), accumulate = FALSE)[[1]]

all_flash <- function(mat) {
  step <- 0
  repeat {
    step <- step + 1
    mat <- iterate(list(0, mat), step)[[2]]
    if (all(mat == 0)) return(step)
  }
}

part2 <- all_flash(input)
