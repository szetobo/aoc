library(tidyverse)

input <- readLines('~/proj/aoc/resources/2021/day09.txt') %>%
  strsplit("") %>% lapply(as.integer) %>% do.call(what = rbind)

row <- nrow(input)
col <- ncol(input)

local_minima <-
  cbind(input[, -col] < input[, -1], TRUE) &
  cbind(TRUE, input[, -1] < input[, -col]) &
  rbind(input[-row, ] < input[-1, ], TRUE) &
  rbind(TRUE, input[-1, ] < input[-row, ])

part1 <- sum(input[local_minima] + 1)

low_pts <- which(local_minima, arr.ind = TRUE)

adjacent_4 <- function(pt, mat) {
  x <- pt[1]
  y <- pt[2]
  z <- rbind(c(x - 1, y), c(x, y - 1), c(x, y + 1), c(x + 1, y))
  z <- z[z[, 1] > 0 & z[, 1] <= nrow(mat) & z[, 2] > 0 & z[, 2] <= ncol(mat), ]
  return(z)
}

basins <- function(pts, res = matrix(nrow = 0, ncol = 2)) {
  if (nrow(pts) == 0) return(res)
  nbrs <- adjacent_4(pts[1,], input)
  nbrs <- nbrs[apply(nbrs, 1, \(x) input[x[1], x[2]] < 9), , drop = FALSE]
  nbrs <- nbrs[!duplicated(rbind(nbrs, res, pts), fromLast = TRUE)[1:nrow(nbrs)], , drop = FALSE]
  res <- rbind(res, pts[1,])
  pts <- rbind(pts[-1,], nbrs)
  basins(pts, res)
}

part2 <-
  sapply(1:nrow(low_pts), \(r) low_pts[r,, drop = FALSE] %>% basins %>% nrow) %>%  sort %>% tail(3) %>% prod
