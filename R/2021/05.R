library(tidyverse)

raw <-
  readLines('~/proj/aoc/resources/2021/day05.txt') %>%
  stringr::str_replace(' -> ', ',')

input <-
  scan(text = raw,
              sep = ',',
              what = list(x0 = integer(), y0 = integer(),
                          x1 = integer(), y1 = integer()))

straight <- which(input$x0 == input$x1 | input$y0 == input$y1)

pts <- purrr::pmap(input, \(x0, y0, x1, y1) { cbind(x0:x1, y0:y1) })

straight_pts <- do.call(rbind, pts[straight])
part1 <- nrow(unique(straight_pts[duplicated(straight_pts),]))

all_pts <- do.call(rbind, pts)
part2 <- nrow(unique(all_pts[duplicated(all_pts),]))
