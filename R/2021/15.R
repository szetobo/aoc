library(tidyverse)

# import -------------------------------------------------------------------

input <- readLines('~/proj/aoc/resources/2021/day15.txt') %>%
  strsplit("") %>% lapply(as.integer) %>% do.call(what = rbind)

PriorityQueue <- function() {
  keys <- values <- NULL
  insert <- function(key, value) {
    ord <- findInterval(key, keys)
    keys <<- append(keys, key, ord)
    values <<- append(values, value, ord)
  }
  pop <- function() {
    head <- list(key=keys[1],value=values[[1]])
    values <<- values[-1]
    keys <<- keys[-1]
    return(head)
  }
  empty <- function() length(keys) == 0
  environment()
}

adjacent_4 <- function(pt, mat) {
  x <- pt[1]
  y <- pt[2]
  z <- rbind(c(x - 1, y), c(x, y - 1), c(x, y + 1), c(x + 1, y))
  z <- z[z[, 1] > 0 & z[, 1] <= nrow(mat) & z[, 2] > 0 & z[, 2] <= ncol(mat), ]
  return(z)
}

dijkstra <- function(nbrs_dst, pt = c(1, 1)) {
  pq <- PriorityQueue()
  pq$insert(0, str_c(pt[1], pt[2], sep = ':'))
  visited <- matrix(Inf, nrow = nrow(nbrs_dst), ncol = ncol(nbrs_dst))
  repeat {
    if (pq$empty()) return(visited)
    res <- pq$pop()
    pos <- strsplit(res$value, ':') %>% lapply(as.integer) %>% unlist
    if (res$key >= visited[pos[1], pos[2]]) next
    nbrs <- adjacent_4(pos, nbrs_dst)
    nbrs <- nbrs[apply(nbrs, 1, \(x) visited[x[1], x[2]] > (nbrs_dst[x[1], x[2]] + res$key)), , drop = FALSE]
    for (i in seq_len(nrow(nbrs))) {
      pq$insert(nbrs_dst[nbrs[i, 1], nbrs[i, 2]] + res$key,
                str_c(nbrs[i, 1], nbrs[i, 2], sep = ':'))
    }
    visited[pos[1], pos[2]] <- res$key
  }
}

# part 1 ------------------------------------------------------------------

part1 <- dijkstra(input)[100, 100]

# part 2 ------------------------------------------------------------------

big_map <- rbind(cbind(input,     input + 1, input + 2, input + 3, input + 4),
                 cbind(input + 1, input + 2, input + 3, input + 4, input + 5),
                 cbind(input + 2, input + 3, input + 4, input + 5, input + 6),
                 cbind(input + 3, input + 4, input + 5, input + 6, input + 7),
                 cbind(input + 4, input + 5, input + 6, input + 7, input + 8))
big_map <- (big_map - 1) %% 9 + 1

part2 <- dijkstra(big_map)[500, 500]
