library(tidyverse)

input <- readLines('~/proj/aoc/resources/2021/day13.txt')

gap <- which(input == "")
pts <- readr::read_csv(I(input[1:(gap - 1)]), col_names = c('x', 'y'))
folds <-
  readr::read_delim(I(input[(gap + 1):length(input)]) , delim = "=", col_names = c('dir', 'pos')) %>%
  dplyr::mutate(dir = substring(dir, 12))

fst_dir <- folds$dir[1]
fst_pos <- folds$pos[1]

res1 <- pts %>%
  dplyr::mutate("{ fst_dir }" := ifelse(.data[[fst_dir]] < {{ fst_pos }},
                                        .data[[fst_dir]],
                                        2 * {{ fst_pos }} - .data[[fst_dir]]))

# pts[[folds$dir[1]]] <-
#   ifelse(pts[[folds$dir[1]]] < folds$pos[1],
#          pts[[folds$dir[1]]],
#          2 * folds$pos[1] - pts[[folds$dir[1]]])

part1 <- nrow(res1) - sum(duplicated(res1))

res2 <- pts
for (i in seq_along(folds$dir)) {
  fst_dir <- folds$dir[i]
  fst_pos <- folds$pos[i]
  res2 <- res2 %>%
    dplyr::mutate("{ fst_dir }" := ifelse(.data[[fst_dir]] < {{ fst_pos }},
                                          .data[[fst_dir]],
                                          2 * {{ fst_pos }} - .data[[fst_dir]]))
}

show <- matrix(FALSE, nrow = max(res2$y) + 1L, ncol = max(res2$x) + 1L)
for (i in seq_len(nrow(res2))) {
  show[res2$y[i] + 1L, res2$x[i] + 1L] <- TRUE
}
cat(ifelse(t(show), "#", " "), fill = ncol(show), sep = "")
