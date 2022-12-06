(ns abagile.aoc.2022.day06)

(def input
  (->> "resources/2022/day06.txt" slurp seq))

(defn marker
  [input n]
  (->> input (partition n 1)
       (map-indexed #(when (= (count (set %2)) n) %1))
       (remove nil?) first (+ n)))

(defn part1
  []
  (marker input 4))

(defn part2
  []
  (marker input 14))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
