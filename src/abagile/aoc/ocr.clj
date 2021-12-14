(ns abagile.aoc.ocr
  (:require
    [clojure.string :as cs]))

(defn draw
  [grid]
  (let [width  ( ->> grid (map first) (apply max) inc)
        height (->> grid (map second) (apply max) inc)]
    (->> (for [y (range height) x (range width)]
           (if (grid [x y]) \# \.))
         (partition width)
         (map #(apply str %)))))

(def ->str
  {["####..##..#..#..##..#..#.###..####..##."
    "#....#..#.#.#..#..#.#.#..#..#....#.#..#"
    "###..#....##...#....##...###....#..#..."
    "#....#.##.#.#..#....#.#..#..#..#...#.##"
    "#....#..#.#.#..#..#.#.#..#..#.#....#..#"
    "#.....###.#..#..##..#..#.###..####..###"]
   "FGKCKBZG"})

(defn -print
  [grid]
  (->> grid draw (cs/join "\n") println))
