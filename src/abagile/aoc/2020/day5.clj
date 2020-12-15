(ns abagile.aoc.2020.day5
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]))

(def input (->> (util/read-input-split-lines "2020/day5.txt")))

(def bit-map {\B \1, \R \1, \F \0, \L \0})

(defn part1 []
  (time (->> input
             (map seq)
             (map #(map bit-map %))
             (map util/binary-val)
             (apply max))))

(defn part2 []
  (time (->> input
             (map seq)
             (map #(map bit-map %))
             (map util/binary-val)
             sort
             (partition 2 1)
             (filter #(let [[n1 n2] %] (not= 1 (- n2 n1))))
             ffirst
             (+ 1))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))
