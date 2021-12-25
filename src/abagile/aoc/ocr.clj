(ns abagile.aoc.ocr)

(defn parse
  ([s] (parse s {"." 0 "#" 1}))
  ([s char-map]
   (let [char-re (re-pattern (str "[" (keys char-map) "]"))
         line-re (re-pattern (str char-re "+\\n"))
         cols    (->> s (re-find line-re) count dec)
         elms    (->> s (re-seq char-re) (map char-map))
         grid    (into {} (map-indexed #(vector [(quot %1 cols) (rem %1 cols)] %2) elms))]
     (with-meta grid {:dim [(quot (count elms) cols) cols]}))))

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
