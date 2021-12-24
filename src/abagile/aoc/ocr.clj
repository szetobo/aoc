(ns abagile.aoc.ocr)

(defn parse
  [s]
  (let [cols (->> s (re-find #"[.#]+\n") count dec)
        elms (->> s (re-seq #"[.#]") (map {"." 0 "#" 1}))
        grid (into {} (map-indexed #(vector [(quot %1 cols) (rem %1 cols)] %2) elms))]
     (with-meta grid {:dim [(quot (count elms) cols) cols]})))

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
