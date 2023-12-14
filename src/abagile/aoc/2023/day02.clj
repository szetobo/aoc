(ns abagile.aoc.2023.day02
  (:require
   [abagile.aoc.util :as util]))

(def input (->> (util/read-input-split "2023/day02.txt" #"\n")
                (map #(->> (re-seq #"(\d+) (red|blue|green)" %)
                           (map rest)
                           (map (fn [x] (map read-string x)))
                           (reduce (fn [m [n c]] (merge-with max m {c n})) {'red 0 'green 0 'blue 0})))))

(defn part1
  []
  (time (->> input
             (keep-indexed (fn [idx {:syms [red green blue]}]
                             (when (and (<= red 12) (<= green 13) (<= blue 14)) (inc idx))))
             (reduce +))))


(defn part2
  []
  (time (->> input
             (map (fn [{:syms [red green blue]}] (* red green blue)))
             (reduce +))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
