(ns abagile.aoc.ocr
  (:require
    [clojure.string :as cs]))

(defn parse
  [s]
  (let [cols (->> s (re-find #"[.#]+\n") count dec)
        elms (->> s (re-seq #"[.#]") (map {"." 0 "#" 1}))
        grid (into {} (map-indexed #(vector [(quot %1 cols) (rem %1 cols)] %2) elms))]
     (with-meta grid {:dim [cols (quot (count elms) cols)]})))

(defn draw
  [grid]
  (let [width  (->> grid (map first) (apply max) inc)
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
