(ns abagile.aoc.2020.day3
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]))

(def input (->> (util/read-input-split-lines "2020/day3.txt")))

(defn count-of-trees [r d]
  (let [len (count (first input))]
    (->> (map #(nth %1 (mod %2 len)) 
              (take-nth d input)
              (range 0 (* r (count input)) r))
         (filter #(= \# %))
         count)))

(defn part1 []
  (time (count-of-trees 3 1)))

(defn part2 []
  (time (->> [[1 1] [3 1] [5 1] [7 1] [1 2]]
             (map #(apply count-of-trees %))
             (reduce *))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))
