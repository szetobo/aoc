(ns abagile.aoc.2022.day08)

(defn parse-grid
  [s]
  (let [cols (->> s (re-find #"\d+\n") count dec)
        elms (->> s (re-seq #"\d") (map read-string))
        grid (into {} (map-indexed #(vector [(quot %1 cols) (rem %1 cols)] %2) elms))]
     (with-meta grid {:dim [(quot (count elms) cols) cols]})))

(def input (->> (slurp "resources/2022/day08.txt") parse-grid))

(defn visible?
  [trees]
  (let [[rows cols] (-> trees meta :dim)]
    (for [row (range rows) col (range cols)
          :let [v (trees [row col])]
          :when (or (#{0 (dec rows)} row) (#{0 (dec cols)} col)
                    (->> (map #(trees [% col]) (range row))            (every? #(< % v)))
                    (->> (map #(trees [% col]) (range (inc row) rows)) (every? #(< % v)))
                    (->> (map #(trees [row %]) (range  col))           (every? #(< % v)))
                    (->> (map #(trees [row %]) (range (inc col) cols)) (every? #(< % v))))]
      [row col])))

(defn part1
  []
  (->> input visible? count))

(defn take-until
  [pred coll]
  (lazy-seq
    (when-let [coll (seq coll)]
      (let [x (first coll)]
        (cons x (when-not (pred x) (take-until pred (rest coll))))))))

(defn score
  [trees]
  (let [[rows cols] (-> trees meta :dim)]
    (for [row (range rows) col (range cols)
          :let [v (trees [row col])
                up    (->> (map #(trees [% col]) (range (dec row) -1 -1)) (take-until #(>= % v)) count)
                down  (->> (map #(trees [% col]) (range (inc row) rows))  (take-until #(>= % v)) count)
                left  (->> (map #(trees [row %]) (range (dec col) -1 -1)) (take-until #(>= % v)) count)
                right (->> (map #(trees [row %]) (range (inc col) cols))  (take-until #(>= % v)) count)]]
      (* up down left right))))

(defn part2
  []
  (->> input score (apply max)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
