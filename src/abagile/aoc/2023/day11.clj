(ns abagile.aoc.2023.day11
  (:require
   [abagile.aoc.util :as util]
   [clojure.math.combinatorics :as comb]))

(def sample (->> (util/read-input-split "2023/day11-sample.txt" #"\n")
                 (map seq)))
(def input (->> (util/read-input-split "2023/day11.txt" #"\n")
                (map seq)))

(defn parse
  [input]
  (->> input
       (map-indexed (fn [r-idx row]
                      (map-indexed (fn [c-idx elm]
                                     [[r-idx c-idx] elm])
                                   row)))
       (apply concat)
       (into (sorted-map))))

(defn get-galaxy-cors
  [grid]
  (->> grid (filter #(-> % last #{\#})) (map first)))

(defn get-empty-cors
  [grid]
  (let [row  (->> grid (map #(get-in % [0 0])) (apply max))
        col  (->> grid (map #(get-in % [0 1])) (apply max))
        er   (for [r (range row) :when (every? #{\.} (->> grid (filter #(= r (get-in % [0 0]))) vals))] r)
        ec   (for [c (range col) :when (every? #{\.} (->> grid (filter #(= c (get-in % [0 1]))) vals))] c)]
    [er ec]))

(defn galaxy-distance
  ([[er ec] [r1 c1] [r2 c2]] (galaxy-distance 1 [er ec] [r1 c1] [r2 c2]))
  ([factor [er ec] [r1 c1] [r2 c2]]
   (let [rs (min r1 r2) re (max r1 r2)
         cs (min c1 c2) ce (max c1 c2)]
     (+ (util/manhattan-distance [r1 c1] [r2 c2])
        (* factor (count (filter #(<= rs % re) er)))
        (* factor (count (filter #(<= cs % ce) ec)))))))

(comment
  (->> sample parse get-galaxy-cors)
  (->> sample parse get-empty-cors)
  (galaxy-distance [[3 7] [2 5 8]] [0 3] [1 7])
  (galaxy-distance [[3 7] [2 5 8]] [5 1] [9 4])
  (->> input parse get-galaxy-cors)
  (->> input parse get-empty-cors)
  (comb/combinations (->> input parse get-galaxy-cors) 2))

(defn part1
  []
  (time (let [grid    (parse input)
              [er ec] (get-empty-cors grid)]
           (->> (comb/combinations (get-galaxy-cors grid) 2)
                (map #(apply galaxy-distance [er ec] %))
                (reduce +)))))

(defn part2
  []
  (time (let [grid    (parse input)
              [er ec] (get-empty-cors grid)]
           (->> (comb/combinations (get-galaxy-cors grid) 2)
                (map #(apply galaxy-distance 999999 [er ec] %))
                (reduce +)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
