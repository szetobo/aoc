(ns abagile.aoc.2021.day25
  (:gen-class)
  (:require
    [abagile.aoc.ocr :as ocr]
    [abagile.aoc.util :as util]))

(def sample (util/read-input "2021/day25.sample.txt"))
(def input  (util/read-input "2021/day25.txt"))

(defn parse
  [s]
  (ocr/parse s {">" \> "v" \v "." \.}))

(defn next-pos
  [grid [rows cols] [row col]]
  (case (grid [row col])
    \> [row (mod (inc col) cols)]
    \v [(mod (inc row) rows) col]))

(defn step
  [dim grid]
  (loop [grid grid ch \>]
    (let [moves (->> grid
                     (filter #(-> % val #{ch}))
                     (map first)
                     (filter #(= (grid (next-pos grid dim %)) \.)))
          grid' (->> (reduce #(-> %1
                                  (assoc (next-pos grid dim %2) ch)
                                  (assoc %2 \.))
                       grid moves))]
      (if (= ch \v) grid' (recur grid' \v)))))

(comment
  (count sample)
  (parse sample)
  (count input)
  (:dim (meta (parse input))))

(defn part1
  []
  (time
    (let [grid (parse input) dim (-> grid meta :dim)]
      (loop [n 1 iter (partition 2 1 (iterate (partial step dim) grid))]
        (let [[g1 g2] (first iter)]
          (cond
            (> n 1000) :fail
            (= g1 g2) n
            :else (recur (inc n) (drop 1 iter))))))))

(defn part2
  [])

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
