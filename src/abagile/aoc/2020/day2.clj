(ns abagile.aoc.2020.day2
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]))

(def input (->> (util/read-input-split-lines "2020/day2.txt")))

(defn part1 []
  (time (->> input
            (map #(rest (re-matches #"(\d+)-(\d+)\s+(.):\s*(.*)$" %)))
            (filter #(let [[n1 n2 s1 s2] %
                           n1 (util/parse-int n1)
                           n2 (util/parse-int n2)
                           s1 (first s1)]
                       (<= n1 (get (frequencies s2) s1 0) n2)))
            count)))

(defn part2 []
  (time (->> input
            (map #(rest (re-matches #"(\d+)-(\d+)\s+(.):\s*(.*)$" %)))
            (filter #(let [[n1 n2 s1 s2] %
                           n1  (util/parse-int n1)
                           n2  (util/parse-int n2)
                           s1  (first s1)
                           len (count s2)
                           b1  (and (>= len n1) (= s1 (nth s2 (dec n1))))
                           b2  (and (>= len n2) (= s1 (nth s2 (dec n2))))]
                       (if b1 (not b2) b2)))
            count)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))
