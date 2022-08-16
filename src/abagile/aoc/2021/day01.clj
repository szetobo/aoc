;; # AOC 2021 day-01
(ns abagile.aoc.2021.day01
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [nextjournal.clerk :as clerk]))

(def input (->> (util/read-input-split-lines "2021/day01.txt")
                (map util/parse-int)))

;; ### sample of input
(clerk/example
  (count input)
  (take 10 input)
  (partition 2 1 input)
  (partition 3 1 input))

(defn count-if
  [pred coll]
  (->> (partition 2 1 coll)
       (filter (fn [[n1 n2]] (pred n2 n1)))
       count))

(defn part1
  []
  (time (count-if > input)))

(defn part2
  []
  (time (->> (partition 3 1 input)
             (map #(reduce + %))
             (count-if >))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
