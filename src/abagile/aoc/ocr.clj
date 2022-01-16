(ns abagile.aoc.ocr)

(defn parse
  ([s] (parse s {"." 0 "#" 1}))
  ([s char-map]
   (let [char-re (re-pattern (str "[" (apply str (keys char-map)) "]"))
         rows    (->> s (re-seq #"\n") count)
         elms    (->> s (re-seq char-re) (map char-map))
         cols    (quot (count elms) rows)
         grid    (into {} (map-indexed #(vector [(quot %1 cols) (rem %1 cols)] %2) elms))]
     (with-meta grid {:dim [rows cols]}))))

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
