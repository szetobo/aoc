(ns abagile.aoc.2015.day01
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]))

(def input (->> (util/read-input-split "2015/day01.txt" #"\n") first))

(defn part1
  []
  (time
    (let [{x \( y \)} (frequencies input)] (- x y))))

(defn part2
  []
  (time
    (loop [n 1 sum 0 [fst & rsts] input]
      (let [sum (+ sum (case fst \( 1 \) -1))]
        (if (= sum -1) n (recur (inc n) sum rsts))))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
